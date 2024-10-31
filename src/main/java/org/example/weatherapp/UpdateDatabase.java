package org.example.weatherapp;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;

public class UpdateDatabase {
    private static Connection connection;

    public UpdateDatabase() throws SQLException {
        connection = DatabaseConnection.getConnection();
        System.out.println("Connection established!");
    }

    public static void saveSearchHistory(String city, String hour, Integer degrees_C, Integer humidity, Integer wind) {
        String sql = "INSERT INTO searchhistory (city, hour, degrees_C, humidity, wind) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city);
            statement.setString(2, hour);
            statement.setInt(3, degrees_C);
            statement.setInt(4, humidity);
            statement.setInt(5, wind);

            statement.executeUpdate();
            System.out.println("Search history saved to the database.");
        } catch (SQLException e) {
            System.out.println("Error saving search history: " + e.getMessage());
        }
    }

    public static void saveDetailedSearchHistory(String city, String hour, Integer degrees_C, Integer humidity, Integer wind, String date, Integer wind_degree) {
        String sql = "INSERT INTO detailedsearchhistory (city, hour, degrees_C, humidity, wind, degrees_F, date, wind_degree) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city);
            statement.setString(2, hour);
            statement.setInt(3, degrees_C);
            statement.setInt(4, humidity);
            statement.setInt(5, wind);
            statement.setNull(6, Types.INTEGER); // Set parameter 6 to null
            statement.setString(7, date);
            statement.setInt(8, wind_degree);

            statement.executeUpdate();
            System.out.println("Detailed search history saved to the database.");
        } catch (SQLException e) {
            System.out.println("Error saving detailed search history: " + e.getMessage());
        }
    }

    public static void saveEssentialInfo(String city, Integer degrees_C, String date, String hour) {
        String sql = "INSERT INTO essentialinfo (city, degrees_C, date, hour) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city);
            statement.setInt(2, degrees_C);
            statement.setString(3, date);
            statement.setString(4, hour);

            statement.executeUpdate();
            System.out.println("Essential info saved to the database.");
        } catch (SQLException e) {
            System.out.println("Error saving essential info: " + e.getMessage());
        }
    }
    public static void saveGeolocationData(String city, String location, BigDecimal latitude, BigDecimal longitude, String feels_like) {
        String sql = "INSERT INTO geolocationdata (city, location, latitude, longitude, feels_like) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, city);
            statement.setString(2, location);
            statement.setBigDecimal(3, latitude);
            statement.setBigDecimal(4, longitude);
            statement.setString(5, feels_like);

            statement.executeUpdate();
            System.out.println("Geolocation Data saved to the database.");
        } catch (SQLException e) {
            System.out.println("Error saving geolocation data: " + e.getMessage());
        }
    }

    public static void saveFavorites(List<Favorites> favorites) {

        String json = buildJson(favorites);

        String sql = "CALL UpdateFavorites(?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, json);

            statement.executeUpdate();

            System.out.println("Favorites data saved to the database.");
        } catch (SQLException e) {
            System.out.println("Error saving favorites data: " + e.getMessage());
        }
    }

    private static String buildJson(List<Favorites> favorites) {
        StringBuilder jsonBuilder = new StringBuilder("[");

        for (int i = 0; i < favorites.size(); i++) {
            Favorites fav = favorites.get(i);
            jsonBuilder.append("{")
                    .append("\"city\": \"").append(fav.getCity()).append("\", ")
                    .append("\"hour\": \"").append(fav.getHour()).append("\", ")
                    .append("\"degrees_C\": \"").append(fav.getDegrees_C()).append("\", ")
                    .append("\"humidity\": ").append(fav.getHumidity()).append(", ")
                    .append("\"wind\": ").append(fav.getWind())
                    .append("}");

            if (i < favorites.size() - 1) {
                jsonBuilder.append(", ");
            }
        }

        jsonBuilder.append("]");

        return jsonBuilder.toString();
    }
}

