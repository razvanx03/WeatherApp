package org.example.weatherapp;


public class Favorites { // pentru a crea un List<Favorites> care sa tina mai multe date
    private String city;
    private String hour;
    private Integer degrees_C;
    private Integer humidity;
    private Integer wind;

    public Favorites(String city, String hour, Integer degrees_C, Integer humidity, Integer wind) {
        this.city = city;
        this.hour = hour;
        this.degrees_C = degrees_C;
        this.humidity = humidity;
        this.wind = wind;
    }

    // Getter methods
    public String getCity() {
        return city;
    }

    public String getHour() {
        return hour;
    }

    public Integer getDegrees_C() {
        return degrees_C;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public Integer getWind() {
        return wind;
    }
}
