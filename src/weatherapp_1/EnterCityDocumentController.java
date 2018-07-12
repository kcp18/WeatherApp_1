/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package weatherapp_1;

import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import org.apache.commons.io.IOUtils;
import org.json.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import javafx.scene.control.Button;
import javafx.util.Callback;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Chanwoo Park
 */
public class EnterCityDocumentController implements Initializable {
    
    @FXML
    private TextField searchField;
    @FXML
    private ListView<JSONObject> listView;
    @FXML
    private Text noResultText;
    @FXML
    private Text searchingText;
    @FXML
    private Button cancelButton;
    @FXML
    private Button selectButton;
    
    public JSONObject cityApiJsonObject;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb)  {
                   
    }    
    
    public void typeOnSearchField() throws JSONException, MalformedURLException, IOException {
        
        listView.getItems().clear();
        noResultText.setVisible(false);

        // userInput
        String userInput = searchField.textProperty().getValue();
        userInput = userInput.trim();
        
        // Create a LinkedList to make a query string to search the city in the WeatherUnderground database     
        LinkedList<String> listTokensForCityName = new LinkedList<>();
        String spaceSymbol = "%20";
        String token = ""; 
        for(int i=0; i<userInput.length(); i++){
            if(userInput.charAt(i) != ' '){             // If there is no space, add characeters to token.
                token += userInput.charAt(i);
            }else{                                      //If there is a space
                listTokensForCityName.add(token);
                listTokensForCityName.add(spaceSymbol);
                token = "";
            }
        }
         
        listTokensForCityName.add(token);
        String queryRequest = "";
        Iterator it = listTokensForCityName.iterator();
        
        // finish making queryRequest String to search a city.
        while(it.hasNext()){
            queryRequest += (String) it.next();
        }
        
        if(!queryRequest.equals("")){  
            
            // use the autocomplete search of the WeatherUnderground to get the names of cities in a json format.
            JSONObject searchedCitiesJson = new JSONObject(IOUtils.toString(new URL("http://autocomplete.wunderground.com/aq?query="+queryRequest), Charset.forName("UTF-8")));
            
            // displayUserInput method is called to display the searched citie and display them.
            displayUserInput(searchedCitiesJson); 
            
        }else{
            //hide selectoinButton when the user deletes the input.
            selectButton.setVisible(false);
        }
        
    }
    
    public void displayUserInput(JSONObject userInputJsonObject){
     
        List<JSONObject> applicableCityJsonList = new ArrayList<>();  
        JSONArray cityDataJsonObjectArray = userInputJsonObject.getJSONArray("RESULTS");
        
        // applicableCityJsonList contains the jsonobjects that has the weather data 
        for(int i=0; i<cityDataJsonObjectArray.length(); i++){
            JSONObject cityJsonObject = cityDataJsonObjectArray.getJSONObject(i);  
            String restrictionCode = cityJsonObject.getString("tz");
            if(!restrictionCode.equals("MISSING")){
                applicableCityJsonList.add(cityJsonObject);
            }       
        }
         
        if(applicableCityJsonList.size() > 0){
            selectButton.setVisible(true);
        }else{
            selectButton.setVisible(false);
        }
 
        if(applicableCityJsonList.isEmpty()){
           noResultText.setVisible(true);
        }else{    
             // use CellFactory to display only texts/name of the JsonObjects.
             listView.setCellFactory(new Callback<ListView<JSONObject>, ListCell<JSONObject>>(){
                 
                 @Override
                 public ListCell<JSONObject> call(ListView<JSONObject> param) {
                     
                     ListCell<JSONObject> cell = new ListCell<JSONObject>() {
                        protected void updateItem(JSONObject t, boolean bln) {
                        super.updateItem(t, bln);
                        
                        if (t != null) {
                            String text = t.getString("name");
                            String country = t.getString("c");
                            
                            if(country.equals("US")){
                                text = text + ", United States";
                            }
                            
                            setText(text);
                           
                        }                     
                    }
                     };
                     
                     return cell;
                 }                          
             });
             
            listView.setItems(FXCollections.observableArrayList(applicableCityJsonList));
        }
    }
    
    @FXML
    public void cancelButtonClicked() throws IOException{
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
    
    @FXML
    public void selectButtonClicked() throws MalformedURLException, IOException {
       JSONObject selectedCity = (JSONObject) listView.getSelectionModel().getSelectedItem();
       String cityApiRequestString = (String) selectedCity.get("l") + ".json";
       
       //assign the selected and requested city's JsonObejct to the global variable cityApiJsonObject.
       cityApiJsonObject = new JSONObject(IOUtils.toString(new URL("http://api.wunderground.com/api/c697bbfa1025bf5a/forecast10day/hourly/conditions/"+cityApiRequestString), Charset.forName("UTF-8")));
       listView.getScene().getWindow().hide();
     
    }
    /*
    * getUserCity() is called to display in the main page.
    */
    public JSONObject getUserCity(){
        return cityApiJsonObject;
    }
    
}
