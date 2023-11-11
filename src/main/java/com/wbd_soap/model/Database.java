package com.wbd_soap.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;

import org.apache.ibatis.jdbc.ScriptRunner;

public class Database {
    private Connection connection;
    public Database(){
        try {
            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://host.docker.internal:%s/%s", System.getenv("MYSQL_PORT"), System.getenv("MYSQL_DATABASE")), System.getenv("MYSQL_USER"), System.getenv("MYSQL_PASSWORD"));
            System.out.println("Successfully connect to Database");
        } catch (Exception e){
            System.out.println("Failed connecting to MySQL Database");
        }
    }

    public void setupDatabase(){
        ScriptRunner sr = new ScriptRunner(this.connection);
        try {
            Reader reader;
            reader = new BufferedReader(new FileReader("./src/main/java/com/wbd_soap/db/T01_Logging.sql"));
            sr.runScript(reader);
            reader = new BufferedReader(new FileReader("./src/main/java/com/wbd_soap/db/T02_Reference.sql"));
            sr.runScript(reader);
            System.out.println("Successfully setup database");

        } catch (Exception e){
            System.out.println(e);
            System.out.println("Failed to setup database");
        }

    }

    public Connection getConnection() {
        return connection;
    }
}
