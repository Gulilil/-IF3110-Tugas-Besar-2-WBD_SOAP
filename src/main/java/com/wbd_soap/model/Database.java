package com.wbd_soap.model;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private Connection connection;
    private String dbURL;
    private String dbUsername;
    private String dbPassword;

    public Database(){
        try {
//            Dotenv dotenv = Dotenv.load();
//            this.dbURL = dotenv.get("MYSQL_URL");
//            this.dbUsername = dotenv.get("MYSQL_USER");
//            this.dbPassword = dotenv.get("MYSQL_PASSWORD");
//            this.connection = DriverManager.getConnection(dbURL, dbUsername, dbPassword);
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/wbd_soap", "root", "password");
        } catch (Exception e){
            System.out.println("Failed connecting to MySQL Database");
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
