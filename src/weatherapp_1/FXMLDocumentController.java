/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherapp_1;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONObject;

/**
 *
 * @author SurviveinAmerica
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    ListView<JSONObject> mainListView;
    @FXML
    private ToggleButton toggleButtonC;
    @FXML
    private ToggleButton toggleButtonF;   
    @FXML
    private Button editButton;
    
    private boolean editMode = false;
    
    public boolean typeOfTempFahrenheit;//when it is true, temp is Fahrenheit, else Celcius.
      
    public BorderPane rootPane;
    
    public List<JSONObject> userSelectedCityList = new ArrayList<JSONObject>();
    
    public static JSONObject selectedCity;
    
    public static boolean tempType; // used only for passing this value to DetailWeatherDoucment
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ToggleGroup toggleGroup = new ToggleGroup();
        toggleButtonC.setToggleGroup(toggleGroup);
        toggleButtonF.setToggleGroup(toggleGroup);
        
        //default to Fahrenheit
        toggleButtonF.setSelected(true);
        changeToFahrenheit();
        editButton.setVisible(false);
        
        
    }

    @FXML
    public void addCity() throws IOException{
      
        FXMLLoader loader = new FXMLLoader(EnterCityDocumentController.class.getResource("EnterCityDocument.fxml"));
        Scene scene = new Scene(loader.load());
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        
        EnterCityDocumentController controller = loader.getController();       
        JSONObject userSelectedCityJSONObject = controller.getUserCity();
        
        if(userSelectedCityJSONObject != null && !containsInListView(userSelectedCityJSONObject)){
            mainListView.getItems().add(userSelectedCityJSONObject);
        }
        
        updateListViewCell();
        stage.close();
       
    }
    
    @FXML
    public void changeToCelcious(){
       
        toggleButtonC.setSelected(true);
        typeOfTempFahrenheit = false;
        updateListViewCell();
        
    }
    
    @FXML
    public void changeToFahrenheit(){
         
         toggleButtonF.setSelected(true);
         typeOfTempFahrenheit = true;
         updateListViewCell();
         
    }
     
    @FXML
    private void itemDoubleClicked(MouseEvent event) throws IOException {
        
        if(event.getButton().equals(MouseButton.PRIMARY)){
            if(event.getClickCount() == 2){
            
                selectedCity = mainListView.getSelectionModel().getSelectedItem();
                tempType = typeOfTempFahrenheit;
                FXMLLoader loader = new FXMLLoader(DetailWeatherDocumentController.class.getResource("DetailWeatherDocument.fxml"));
     
                Scene scene = new Scene(loader.load());
                scene.getStylesheets().add("weatherapp_1/style_1.css");
                Stage stage = new Stage();
                
                stage.setScene(scene);                
                stage.showAndWait();
            }
            

            
            
            
            
            
        }
        
        
       
        
    }

    @FXML
    private void editButtonClicked(ActionEvent event) {
   
        if(editMode == false){
            editMode = true;
         // mainListView.setSelectionModel(new NoSelectionModel<JSONObject>());
            mainListView.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>(){
                 
                 @Override
                public ListCell<JSONObject> call(ListView<JSONObject> param) {

                    ListCell<JSONObject> cell = new ListCell<JSONObject>() {
                       protected void updateItem(JSONObject t, boolean bln) {

                           super.updateItem(t, bln);
                           if (t != null) {
                               JSONObject current_observationJson = (JSONObject) t.get("current_observation");
                               JSONObject display_locationJson = (JSONObject) current_observationJson.get("display_location");
                               String nameOfCity = display_locationJson.getString("city");
                               String temp;
                               if(typeOfTempFahrenheit == true){
                                   int currentTempF = (int) Math.round(current_observationJson.getDouble("temp_f"));              
                                   temp= String.valueOf(currentTempF) + "°";
                               }else{
                                   int currentTempC = (int) Math.round(current_observationJson.getDouble("temp_c"));              
                                   temp = String.valueOf(currentTempC) + "°";
                               }

                               Image minsImage = new Image("/icons/minus.png");
                               ImageView minusImageView = new ImageView();
                               minusImageView.setImage(minsImage);
                               minusImageView.setFitHeight(35);
                               minusImageView.setFitWidth(35);
                               Button deleteButton = new Button("", minusImageView); 
                               deleteButton.setOnAction(e -> {
                                   mainListView.getItems().remove(t);
                               });

                               Text cityNameText = new Text(nameOfCity);
                               cityNameText.setWrappingWidth(530);  //-100
                               cityNameText.setStyle("-fx-font: 30 arial;");

                               Text tempText = new Text(temp);
                               tempText.setStyle("-fx-font: 40 arial;");
                              // tempText.setFont(Font.font (null, 40));

                               String icon_url = current_observationJson.getString("icon_url");
                               ImageView imageViewIcon = new ImageView(icon_url); 

                               HBox hBox = new HBox();
                               hBox.getChildren().addAll(deleteButton, cityNameText,imageViewIcon, tempText);                  
                               hBox.setSpacing(10);
                               hBox.setPadding(new Insets(5, 15, 15, 15));
                               setGraphic(hBox);

                           }                     
                       }
                   };                    
                    return cell;
                }                          
            });
        }else{          
          //mainListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            updateListViewCell();
            editMode = false;        
        }
        
    }
    
    public void updateListViewCell(){
             
        if(mainListView.getItems().size() == 0){
            editButton.setVisible(false);            
        }else{
            editButton.setVisible(true);          
        }
        
        mainListView.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>(){
                 
                @Override
                public ListCell<JSONObject> call(ListView<JSONObject> param) {

                    ListCell<JSONObject> cell = new ListCell<JSONObject>() {
                        
                       protected void updateItem(JSONObject t, boolean bln) {
                           
                           super.updateItem(t, bln);
                            if (t != null) {
                                JSONObject current_observationJson = (JSONObject) t.get("current_observation");
                                JSONObject display_locationJson = (JSONObject) current_observationJson.get("display_location");
                                String nameOfCity = display_locationJson.getString("city");
                                String temp;
                                if(typeOfTempFahrenheit == true){
                                    int currentTempF = (int) Math.round(current_observationJson.getDouble("temp_f"));              
                                    temp= String.valueOf(currentTempF) + "°";
                                }else{
                                    int currentTempC = (int) Math.round(current_observationJson.getDouble("temp_c"));              
                                    temp = String.valueOf(currentTempC) + "°";
                                }

                                Text cityNameText = new Text(nameOfCity);
                                cityNameText.setWrappingWidth(630);
                                cityNameText.setStyle("-fx-font: 30 arial;");

                                Text tempText = new Text(temp);
                                tempText.setStyle("-fx-font: 40 arial;");
                               // tempText.setFont(Font.font (null, 40));

                                String icon_url = current_observationJson.getString("icon_url");
                                ImageView imageViewIcon = new ImageView(icon_url); 

                                HBox hBox = new HBox();
                                hBox.getChildren().addAll(cityNameText,imageViewIcon, tempText);
                                hBox.setSpacing(10);
                                hBox.setPadding(new Insets(5, 15, 15, 45));
                                setGraphic(hBox);

                            }                     
                        }
                    };
                    return cell;
                }                          
        });
    }
    
     public boolean containsInListView(JSONObject newJsonObject){
        ListIterator<JSONObject> it = mainListView.getItems().listIterator();
        while(it.hasNext()){
            JSONObject item = (JSONObject) it.next();
            JSONObject current_observationJSON = (JSONObject) item.get("current_observation");
            String station_id = current_observationJSON.getString("station_id");  
            JSONObject newObjectcurrent_observationJSON = (JSONObject) newJsonObject.get("current_observation");
            String new_station_id = newObjectcurrent_observationJSON.getString("station_id");    
            if(station_id.equals(new_station_id)){
                return true;
            }
        }
        
        return false;
    }    

    public JSONObject getSelectedCity() {
       return selectedCity;
    }
    
    public boolean getTempType(){
        return tempType ? true : false;
    }
    
}
