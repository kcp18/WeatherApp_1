/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherapp_1;

import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.json.JSONArray;
import org.json.JSONObject;

public class DetailWeatherDocumentController extends FXMLDocumentController implements Initializable {

    @FXML
    private Text cityNameText;
   
    @FXML
    private Text weatherStateText;
    @FXML
    private Text tempText;
    
    
    public boolean tempType;
    public JSONObject cityJson;
    @FXML
    private ImageView weatherImageView;
    @FXML
    private VBox topVBox;
    @FXML
    private VBox centerVBox;
    @FXML
    private Text currentDayText;
    @FXML
    private ListView<JSONObject> hourlyListView;
    @FXML
    private ListView<JSONObject> dailyListView;
    
    private int listviewAddedNumber = 0;
    private int listviewAddedNumber_hourly = 0;
    private int testNum = 0;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
       FXMLLoader loader = new FXMLLoader(getClass().getResource("FXMLDocument.fxml")); 
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(DetailWeatherDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
       FXMLDocumentController controller = loader.getController();   
       cityJson = controller.getSelectedCity();
       tempType = controller.getTempType();
       setScene();
       setHourlyListView();
       setDailyListView();
       
       
    }

    public void setScene(){
        JSONObject current_observationJson = cityJson.getJSONObject("current_observation");
     
        
        
        String temp;
        if(tempType == true){
            int currentTempF = (int) Math.round(current_observationJson.getDouble("temp_f"));              
            temp= String.valueOf(currentTempF) + "°";
        }else{
            int currentTempC = (int) Math.round(current_observationJson.getDouble("temp_c"));              
            temp = String.valueOf(currentTempC) + "°";
        }
        
        
       String cityName = current_observationJson.getJSONObject("display_location").getString("full");
       String currWeatherState = cityJson.getJSONObject("current_observation").getString("weather");
       String currDate = cityJson.getJSONArray("hourly_forecast").getJSONObject(0).getJSONObject("FCTTIME").getString("weekday_name");
       currDate += ", " + cityJson.getJSONArray("hourly_forecast").getJSONObject(0).getJSONObject("FCTTIME").getString("month_name");
       currDate += " " + cityJson.getJSONArray("hourly_forecast").getJSONObject(0).getJSONObject("FCTTIME").getString("mday");
       String icon_url = current_observationJson.getString("icon_url");
       Image weatherImage = new Image(icon_url);
       weatherImageView.setImage(weatherImage);
       cityNameText.setText(cityName);
       currentDayText.setText(currDate);
       weatherStateText.setText(currWeatherState);
       tempText.setText(temp);
    }
    
    public void setHourlyListView(){
        
        JSONObject forecastJson = cityJson.getJSONObject("forecast");
        JSONObject txt_forecastJson = forecastJson.getJSONObject("txt_forecast");
        JSONArray forecastdayJsonArray = txt_forecastJson.getJSONArray("forecastday");
        JSONObject period0Json = forecastdayJsonArray.getJSONObject(0);
        String today = period0Json.getString("title");
       // currentDayText.setText(today);
        
        JSONArray hourlyForecastJsonArray = cityJson.getJSONArray("hourly_forecast");
        JSONArray twentyfourForecastJsonArray = new JSONArray();
        
        Iterator it = hourlyForecastJsonArray.iterator();
        int hoursInDay = 24;
        while (hoursInDay != 0 && it.hasNext()){
            twentyfourForecastJsonArray.put(it.next());
            hoursInDay--;
        }
        
               
        
        ObservableList<JSONObject> hourlyJsonlist = getObservableList(twentyfourForecastJsonArray);
        
        
        hourlyListView.setItems(hourlyJsonlist);
        hourlyListView.setOrientation(Orientation.HORIZONTAL);
        
        updateHourlyListViewCell();
    
    }
    
    public void setDailyListView(){
        JSONObject forecastJson = cityJson.getJSONObject("forecast");
        JSONObject simpleforecastJson = forecastJson.getJSONObject("simpleforecast");
        JSONArray forecastdayJsonArray = simpleforecastJson.getJSONArray("forecastday");
        ObservableList<JSONObject> dailyJsonlist = getObservableList(forecastdayJsonArray);
        dailyJsonlist.remove(0);
        dailyListView.setItems(dailyJsonlist);
        
        updateDailyListViewCell();

        
    
    }
    
    
    public void updateHourlyListViewCell(){
        hourlyListView.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>(){
                @Override
                public ListCell<JSONObject> call(ListView<JSONObject> param) {
                    ListCell<JSONObject> cell = new ListCell<JSONObject>() {
                        
                       protected void updateItem(JSONObject t, boolean bln) {
                           
                            super.updateItem(t, bln);
                           
                            if (t != null) {
                               JSONObject FCTTIMEJson = t.getJSONObject("FCTTIME");
                               String hour = FCTTIMEJson.getString("civil");
                               Text hourText = new Text(hour);
                               hourText.setStyle("-fx-font: 19 arial;");
                               hourText.setWrappingWidth(90);
                               hourText.setTextAlignment(TextAlignment.CENTER);
                               
                               
                               JSONObject tempObject = t.getJSONObject("temp");                            
                               String temp = (tempType ? tempObject.getString("english") : tempObject.getString("metric")) + "°";
                               Text tempText = new Text(temp);
                               tempText.setStyle("-fx-font: 19 arial;");
                               tempText.setWrappingWidth(90);
                               tempText.setTextAlignment(TextAlignment.CENTER);
                               
                               
                               String icon_url = t.getString("icon_url");
                               ImageView imageViewIcon = new ImageView(icon_url); 
                               
                                                              
                               VBox vBox = new VBox();
                               vBox.getChildren().addAll(hourText,imageViewIcon, tempText);
                               vBox.setPadding(new Insets(2, 2, 2, 2));
                               setGraphic(vBox);
                               listviewAddedNumber_hourly++;

                            }                     
                        }
                    };
                    return cell;
                    
                    
                }                          
        });
    }


    public void updateDailyListViewCell(){
        
   
        dailyListView.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>(){
                 
                @Override
                public ListCell<JSONObject> call(ListView<JSONObject> param) {

                    ListCell<JSONObject> cell = new ListCell<JSONObject>() {
                        
                       protected void updateItem(JSONObject t, boolean bln) {
                           if(listviewAddedNumber < 10){
                            super.updateItem(t, bln);
                            
                           }
                           
                            if (t != null) {
                                                        
                               String weekday = t.getJSONObject("date").getString("weekday");
                               String icon_url = t.getString("icon_url");
                               
                               String highTemp = tempType ? (t.getJSONObject("high").getString("fahrenheit")) : (t.getJSONObject("high").getString("celsius"));
                               String lowTemp = tempType ? (t.getJSONObject("low").getString("fahrenheit")) : (t.getJSONObject("low").getString("celsius"));
                              
                               Text weekdayText = new Text(weekday);
                               Text highTempText = new Text(highTemp + "°");
                               Text lowTempText = new Text(lowTemp + "°");
                               ImageView imageViewIcon = new ImageView(icon_url); 
                               
                               HBox hBox = new HBox();
                               hBox.setPadding(new Insets(4, 4, 4, 4));
                               hBox.getChildren().addAll(weekdayText, imageViewIcon, lowTempText, highTempText);
                               hBox.setSpacing(10);
                               setGraphic(hBox);                             
                                
                              /*  
                               JSONObject FCTTIMEJson = t.getJSONObject("FCTTIME");
                               String hour = FCTTIMEJson.getString("civil");
                               Text hourText = new Text(hour);
                               hourText.setStyle("-fx-font: 19 arial;");
                               hourText.setWrappingWidth(90);
                               hourText.setTextAlignment(TextAlignment.CENTER);
                               
                               
                               JSONObject tempObject = t.getJSONObject("temp");                            
                               String temp = (tempType ? tempObject.getString("english") : tempObject.getString("metric")) + "°";
                               Text tempText = new Text(temp);
                               tempText.setStyle("-fx-font: 19 arial;");
                               tempText.setWrappingWidth(90);
                               tempText.setTextAlignment(TextAlignment.CENTER);
                               
                               
                               String icon_url = t.getString("icon_url");
                               ImageView imageViewIcon = new ImageView(icon_url); 
                               
                                                              
                               VBox vBox = new VBox();
                               vBox.getChildren().addAll(hourText,imageViewIcon, tempText);
                               vBox.setPadding(new Insets(2, 2, 2, 2));
                               setGraphic(vBox);*/

                            }                     
                        }
                    };
                    return cell;
                }                          
        });
   
    }
    
    public ObservableList<JSONObject> getObservableList(JSONArray jsonArray){
        ObservableList<JSONObject> list = FXCollections.observableArrayList();
        for(int i=0; i<jsonArray.length(); i++){
            list.add(jsonArray.getJSONObject(i));
        }
        return list;
    }
    
}
