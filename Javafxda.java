package javafxda;

import java.sql.*;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Javafxda extends Application {

    private Connection conn;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        
        GridPane form = new GridPane();
        form.setPadding(new Insets(10));
        form.setHgap(10);
        form.setVgap(10);

        Label cityLabel = new Label("Enter City:");
        TextField cityTextField = new TextField();
        Label bankLabel = new Label("Enter Bank Name:");
        TextField bankTextField = new TextField();
        Button searchButton = new Button("Search");
        Button insertButton = new Button("Insert");
        form.add(cityLabel, 0, 0);
        form.add(cityTextField, 1, 0);
        form.add(bankLabel, 0, 1);
        form.add(bankTextField, 1, 1);
        form.add(searchButton, 1, 2);
        
        // Create scene
        Scene scene = new Scene(form, 500, 150);
        primaryStage.setTitle("ATM Locator");
        primaryStage.setScene(scene);
        primaryStage.show();

// Connect to database
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost/atm_locator", "root", "mysql");
            createTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Search button event
        searchButton.setOnAction(event -> {
            String city = cityTextField.getText();
            String bank = bankTextField.getText();
            searchATMs(city, bank);
        });
    }
    private void createTable()
    {
        try {
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS atmcenters (" +
                    "id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                    "name VARCHAR(255) NOT NULL, " +
                    "bank VARCHAR(255) NOT NULL, " +
                    "city VARCHAR(255) NOT NULL)";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void searchATMs(String city, String bank) {
        try {
            String sql = "SELECT * FROM atmcenters WHERE city = ? AND bank = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, city);
            pstmt.setString(2, bank);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("ATM Centers in " + city + " for " + bank + " bank:");
            while (rs.next()) {
                String name = rs.getString("name");
                System.out.println(name);
            }
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
}