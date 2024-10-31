package org.example.weatherapp;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.request.HttpRequest;
import javafx.animation.PauseTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Line;
import javafx.util.Duration;
import org.json.JSONObject;
import javafx.scene.chart.XYChart;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// http://api.weatherapi.com/v1/current.json?key=8d061f7dee124630a4e142905242305&q=berlin&aqi=yes

public class Controller {
    private String countriesAndStates = "Afghanistan:AF,Albania:AL,Algeria:DZ,Andorra:AD,Angola:AO,Antigua and Barbuda:AG,Argentina:AR,Armenia:AM,Australia:AU,Austria:AT,Azerbaijan:AZ,Bahamas:BS,Bahrain:BH,Bangladesh:BD,Barbados:BB,Belarus:BY,Belgium:BE,Belize:BZ,Benin:BJ,Bhutan:BT,Bolivia:BO,Bosnia and Herzegovina:BA,Botswana:BW,Brazil:BR,Brunei:BN,Bulgaria:BG,Burkina Faso:BF,Burundi:BI,Cabo Verde:CV,Cambodia:KH,Cameroon:CM,Canada:CA,Central African Republic:CF,Chad:TD,Chile:CL,China:CN,Colombia:CO,Comoros:KM,Congo:CG,Congo (Democratic Republic of the):CD,Costa Rica:CR,Cote d'Ivoire:CI,Croatia:HR,Cuba:CU,Cyprus:CY,Czech Republic:CZ,Denmark:DK,Djibouti:DJ,Dominica:DM,Dominican Republic:DO,Ecuador:EC,Egypt:EG,El Salvador:SV,Equatorial Guinea:GQ,Eritrea:ER,Estonia:EE,Eswatini:SZ,Ethiopia:ET,Fiji:FJ,Finland:FI,France:FR,Gabon:GA,Gambia:GM,Georgia:GE,Germany:DE,Ghana:GH,Greece:GR,Grenada:GD,Guatemala:GT,Guinea:GN,Guinea-Bissau:GW,Guyana:GY,Haiti:HT,Honduras:HN,Hungary:HU,Iceland:IS,India:IN,Indonesia:ID,Iran:IR,Iraq:IQ,Ireland:IE,Israel:IL,Italy:IT,Jamaica:JM,Japan:JP,Jordan:JO,Kazakhstan:KZ,Kenya:KE,Kiribati:KI,Kuwait:KW,Kyrgyzstan:KG,Laos:LA,Latvia:LV,Lebanon:LB,Lesotho:LS,Liberia:LR,Libya:LY,Liechtenstein:LI,Lithuania:LT,Luxembourg:LU,Madagascar:MG,Malawi:MW,Malaysia:MY,Maldives:MV,Mali:ML,Malta:MT,Marshall Islands:MH,Mauritania:MR,Mauritius:MU,Mexico:MX,Micronesia:FM,Moldova:MD,Monaco:MC,Mongolia:MN,Montenegro:ME,Morocco:MA,Mozambique:MZ,Myanmar:MM,Namibia:NA,Nauru:NR,Nepal:NP,Netherlands:NL,New Zealand:NZ,Nicaragua:NI,Niger:NE,Nigeria:NG,North Korea:KP,North Macedonia:MK,Norway:NO,Oman:OM,Pakistan:PK,Palau:PW,Palestine State:PS,Panama:PA,Papua New Guinea:PG,Paraguay:PY,Peru:PE,Philippines:PH,Poland:PL,Portugal:PT,Qatar:QA,Romania:RO,Russia:RU,Rwanda:RW,Saint Kitts and Nevis:KN,Saint Lucia:LC,Saint Vincent and the Grenadines:VC,Samoa:WS,San Marino:SM,Sao Tome and Principe:ST,Saudi Arabia:SA,Senegal:SN,Serbia:RS,Seychelles:SC,Sierra Leone:SL,Singapore:SG,Slovakia:SK,Slovenia:SI,Solomon Islands:SB,Somalia:SO,South Africa:ZA,South Korea:KR,South Sudan:SS,Spain:ES,Sri Lanka:LK,Sudan:SD,Suriname:SR,Sweden:SE,Switzerland:CH,Syria:SY,Taiwan:TW,Tajikistan:TJ,Tanzania:TZ,Thailand:TH,Timor-Leste:TL,Togo:TG,Tonga:TO,Trinidad and Tobago:TT,Tunisia:TN,Turkey:TR,Turkmenistan:TM,Tuvalu:TV,Uganda:UG,Ukraine:UA,United Arab Emirates:AE,United Kingdom:GB,United States of America:US,Uruguay:UY,Uzbekistan:UZ,Vanuatu:VU,Vatican City:VA,Venezuela:VE,Vietnam:VN,Yemen:YE,Zambia:ZM,Zimbabwe:ZW,Alabama:US-AL,Alaska:US-AK,Arizona:US-AZ,Arkansas:US-AR,California:US-CA,Colorado:US-CO,Connecticut:US-CT,Delaware:US-DE,Florida:US-FL,Georgia:US-GA,Hawaii:US-HI,Idaho:US-ID,Illinois:US-IL,Indiana:US-IN,Iowa:US-IA,Kansas:US-KS,Kentucky:US-KY,Louisiana:US-LA,Maine:US-ME,Maryland:US-MD,Massachusetts:US-MA,Michigan:US-MI,Minnesota:US-MN,Mississippi:US-MS,Missouri:US-MO,Montana:US-MT,Nebraska:US-NE,Nevada:US-NV,New Hampshire:US-NH,New Jersey:US-NJ,New Mexico:US-NM,New York:US-NY,North Carolina:US-NC,North Dakota:US-ND,Ohio:US-OH,Oklahoma:US-OK,Oregon:US-OR,Pennsylvania:US-PA,Rhode Island:US-RI,South Carolina:US-SC,South Dakota:US-SD,Tennessee:US-TN,Texas:US-TX,Utah:US-UT,Vermont:US-VT,Virginia:US-VA,Washington:US-WA,West Virginia:US-WV,Wisconsin:US-WI,Wyoming:US-WY";
    public TextField searchValue;
    public Label city_name;
    public HBox elements_box;
    public Label _time;
    public ImageView weather_icon;
    public Label weather_temperature;
    public Label weather_type;
    public Label _humidity;
    public Label _wind;
    public Line _line;
    public ImageView flag_image;
    private String name;
    private List<String> favoriteCities;
    public BarChart<String,Number> bart_chart;
    private XYChart.Series<String, Number> series;
    public ChoiceBox<String> choice_box;
    private UpdateDatabase DBsaver;
    private boolean addedBlankSpace = false;

    public Controller() {
        favoriteCities = new ArrayList<>();
        series = new XYChart.Series<>();
        favoriteCities.add(" ");
        try {
            DBsaver = new UpdateDatabase();
        } catch (SQLException e) {
            System.out.println("Error establishing database connection: " + e.getMessage());
        }
    }

    private JSONObject requireAPI(String city) {
        try {
            String url = "https://api.weatherapi.com/v1/current.json?key=8d061f7dee124630a4e142905242305&aqi=yes";
            HttpRequest request = Unirest.get(url);

            request = request.queryString("q", city);
            HttpResponse<JsonNode> json_response = request.asJson();
            return json_response.getBody().getObject();
        }
        catch (Exception e) {
            System.out.println("Can't connect to api.");
            e.printStackTrace();
            return null;
        }
    }

    private boolean containsName(String category) {
        for (XYChart.Data<String, Number> data : series.getData()) {
            if (data.getXValue().equals(category)) {
                return true;
            }
        }
        return false;
    }

    private void addChartData(String name,Integer degrees) {
        if (!containsName(name)) {
            series.getData().add(new XYChart.Data<>(name, degrees));
        }
    }

    private void printBarChart() {
        PauseTransition pause = new PauseTransition(Duration.millis(1000));
        pause.setOnFinished(event -> {
            bart_chart.getData().clear();
            bart_chart.getData().add(series);
        });
        pause.play();
    }

    private void updateOpacity() {
        elements_box.setOpacity(100.0);
        _line.setOpacity(100.0);
    }

    private String getCountryCode(String country) {
        String[] entries = countriesAndStates.split(",");
        for (String entry : entries) {
            String[] parts = entry.split(":");
            if (parts[0].equals(country)) {
                return parts[1];
            }
        }
        return null;
    }

    private void updateGUIandDB(JSONObject cityInfo) {

        if (cityInfo == null || cityInfo.has("error")) {
            return;
        }

        // get values
        name = cityInfo.getJSONObject("location").getString("name");
        final Integer degrees = cityInfo.getJSONObject("current").getInt("temp_c");
        final String weatherCondition = cityInfo.getJSONObject("current").getJSONObject("condition").getString("text");
        final String imageURL = cityInfo.getJSONObject("current").getJSONObject("condition").getString("icon");
        final Image image = new Image("https:" + imageURL);

        String time = cityInfo.getJSONObject("location").getString("localtime");
        String date = time.substring(0,10);
        time = time.substring(11);

        final Integer wind = cityInfo.getJSONObject("current").getInt("wind_kph");
        final Integer wind_degree = cityInfo.getJSONObject("current").getInt("wind_degree");
        final Integer humidity = cityInfo.getJSONObject("current").getInt("humidity");

        final BigDecimal latitude = cityInfo.getJSONObject("location").getBigDecimal("lat");
        final BigDecimal longitude = cityInfo.getJSONObject("location").getBigDecimal("lon");
        final String location = cityInfo.getJSONObject("location").getString("country");

        // save to database
        UpdateDatabase.saveSearchHistory(name,time,degrees,humidity,wind);
        UpdateDatabase.saveDetailedSearchHistory(name,time,degrees,humidity,wind,date,wind_degree);
        UpdateDatabase.saveEssentialInfo(name,degrees,date,time);
        UpdateDatabase.saveGeolocationData(name,location,latitude,longitude,weatherCondition);

        // print values
        // BarChart
        addChartData(name,degrees);
        printBarChart();
        /////////////////////////////////////////////

        city_name.setText(name);
        flag_image.setImage(new Image("https://flagsapi.com/" + getCountryCode(location) + "/shiny/64.png"));
        _time.setText(time);
        weather_icon.setImage(image);
        weather_icon.setFitWidth(100);
        weather_icon.setFitHeight(100);
        weather_temperature.setText(degrees + " °C");
        weather_type.setText(weatherCondition);
        _humidity.setText(humidity.toString() + " %");
        _wind.setText(wind.toString() + " km/h");

        updateOpacity();
    }

    public void exportFavorites(ActionEvent actionEvent) {
        Adaptor serializer = new Adaptor();
        serializer.serializeToJson(favoriteCities);
    }

    public void importFavorites(ActionEvent actionEvent) {
        Adaptor deserializer = new Adaptor();
        var result = deserializer.deserializeFromJson("favorite_cities.json");
        if (result != null) {
            addedBlankSpace = true;
            choice_box.getItems().clear();
            favoriteCities = result;
            choice_box.getItems().addAll(favoriteCities);
        }
    }

    public void loadFavorites(ActionEvent actionEvent) {
        series.getData().clear();
        flag_image.setImage(null);
        for (var city : favoriteCities) {
            if (city.equals(" ")) {
                continue;
            }
            JSONObject cityInfo = requireAPI(city);
            name = cityInfo.getJSONObject("location").getString("name");
            Integer degrees = cityInfo.getJSONObject("current").getInt("temp_c");
            addChartData(name,degrees);
        }

        city_name.setText("Favorite cities displayed");
        printBarChart();
        _line.setOpacity(0);
        elements_box.setOpacity(0);
    }

    @FXML
    public void addToFavorites() {
        if (addedBlankSpace == false) {
            choice_box.getItems().add(" ");
            addedBlankSpace = true;
        }
        if(!favoriteCities.contains(name) && name != null) {
            favoriteCities.add(name);
            choice_box.getItems().add(name);
        }
    }

    public void saveFavToDB(ActionEvent actionEvent) {

        List<Favorites> favoritesObject = new ArrayList<>();

        for (var city : favoriteCities) {
            if (city.equals(" ")) {
                continue;
            }
            JSONObject cityInfo = requireAPI(city);
            String name = cityInfo.getJSONObject("location").getString("name");
            String hour = cityInfo.getJSONObject("location").getString("localtime").substring(11);
            Integer degrees_C = cityInfo.getJSONObject("current").getInt("temp_c");
            final Integer humidity = cityInfo.getJSONObject("current").getInt("humidity");
            final Integer wind = cityInfo.getJSONObject("current").getInt("wind_kph");

            Favorites item = new Favorites(name,hour,degrees_C,humidity,wind);

            favoritesObject.add(item);
        }
        UpdateDatabase.saveFavorites(favoritesObject);
    }

    public void clearChart(ActionEvent actionEvent) {
        //city_name.setText("City name");
        series.getData().clear();
        bart_chart.getData().clear();
    }

    public void clearFavorites(ActionEvent actionEvent) {
        choice_box.getItems().clear();
        favoriteCities.clear();
        addedBlankSpace = false;
        choice_box.getItems().addAll(favoriteCities);
    }

    public void handleChoiceBoxAction(ActionEvent actionEvent) {
        JSONObject cityInfo = requireAPI(choice_box.getValue());
        updateGUIandDB(cityInfo);
    }

    @FXML
    public void search(ActionEvent actionEvent) {
        String searchedCity = searchValue.getText();
        JSONObject cityInfo = requireAPI(searchedCity);

        updateGUIandDB(cityInfo);
    }
}