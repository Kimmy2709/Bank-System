package org.example.config;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
public class Conection {

    private static Connection connection;

    //CONEXION A LA BD PORQUE CON SOLO EL .PROPERTIES NO ME FUNCIONA X_x
    public static Connection getConnection() {
        if (connection == null) {
            try {
                Properties properties = new Properties();

                FileInputStream input = new FileInputStream("src/main/resources/templates/dbsconfig.properties");
                properties.load(input);

                String url = properties.getProperty("db.url");
                String username = properties.getProperty("db.username");
                String password = properties.getProperty("db.password");

                connection = DriverManager.getConnection(url, username, password);
                System.out.println("Conexión a la base de datos establecida.");
            } catch (IOException | SQLException e) {
                System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            }
        }
        return connection;
    }

}
