package com.wbd_soap.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;

import org.apache.ibatis.jdbc.ScriptRunner;

import javax.swing.plaf.nimbus.State;

public class Database {
    private Connection connection;
    public Database(){
        try {
            // For local connection
            this.connection = DriverManager.getConnection("jdbc:mysql://localhost:3307/wbd_soap", "root", "password");
            // For docker
//            this.connection = DriverManager.getConnection(String.format("jdbc:mysql://%s:%s/%s", System.getenv("MYSQL_HOST") ,System.getenv("MYSQL_PORT"), System.getenv("MYSQL_DATABASE")), System.getenv("MYSQL_USER"), System.getenv("MYSQL_PASSWORD"));
            System.out.println("Successfully connect to Database");

        } catch (Exception e){
            e.printStackTrace();
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

//    public Connection getConnection() {
//        return connection;
//    }

    // ===========
    // Logging
    // ===========
    public void insertLogDatabase(Logging log){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("INSERT INTO logging (description, ip, endpoint) VALUES ('%s', '%s', '%s');",
                    log.getDescription(), log.getIp(), log.getEndpoint());
            stmt.executeUpdate(q);
            System.out.println("Successfully inserting log to Database");
        } catch (Exception e){
            e.printStackTrace();
            System.out.println("Failed inserting log to Database");
        }
    }

    // ===========
    // Reference
    // ===========

    public Reference getReferenceWithID(int id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE id = %s;", id);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                Reference resultRef = new Reference(
                        res.getInt("id"),
                        res.getInt("anime_account_id"),
                        res.getInt("forum_account_id"),
                        res.getString("referral_code"),
                        res.getInt("point")
                        );
                return resultRef;
            } else {
                return null;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return null;
        }
    }

    public ArrayList<Reference> getAllReference(){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference");
            ResultSet res = stmt.executeQuery(q);
            ArrayList<Reference> refs = new ArrayList<Reference>();
            while (res.next()) {
                Reference tempRef = new Reference(
                        res.getInt("id"),
                        res.getInt("anime_account_id"),
                        res.getInt("forum_account_id"),
                        res.getString("referral_code"),
                        res.getInt("point")
                );
                refs.add(tempRef);
            }
            return refs;

        } catch (Exception e){
            System.out.println("Select operation Failed");
            return null;
        }
    }

    public ArrayList<Reference> getAllReferenceLimitOffset(Integer limit, Integer offset){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference LIMIT %s OFFSET %s", limit, offset);
            ResultSet res = stmt.executeQuery(q);
            ArrayList<Reference> refs = new ArrayList<Reference>();
            while (res.next()) {
                Reference tempRef = new Reference(
                        res.getInt("id"),
                        res.getInt("anime_account_id"),
                        res.getInt("forum_account_id"),
                        res.getString("referral_code"),
                        res.getInt("point")
                );
                refs.add(tempRef);
            }
            return refs;

        } catch (Exception e){
            System.out.println("Select operation Failed");
            return null;
        }
    }

    public Reference getReferenceWithReferralCode(String ref_code){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE referral_code = '%s';", ref_code);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                Reference resultRef = new Reference(
                        res.getInt("id"),
                        res.getInt("anime_account_id"),
                        res.getInt("forum_account_id"),
                        res.getString("referral_code"),
                        res.getInt("point")
                );
                return resultRef;
            } else {
                return null;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return null;
        }
    }

    public Reference getReferenceIDWithAnimeID(int anime_account_id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE anime_account_id = %s;", anime_account_id);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                Reference resultRef = new Reference(
                        res.getInt("id"),
                        res.getInt("anime_account_id"),
                        res.getInt("forum_account_id"),
                        res.getString("referral_code"),
                        res.getInt("point")
                );
                return resultRef;
            } else {
                System.out.println("There's no such row with anime_account_id: "+ anime_account_id);
                return null;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return null;
        }
    }

    public Reference getReferenceIDWithForumID(int forum_account_id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("SELECT * FROM reference WHERE forum_account_id = %s;", forum_account_id);
            ResultSet res = stmt.executeQuery(q);
            if (res.next()){
                Reference resultRef = new Reference(
                        res.getInt("id"),
                        res.getInt("anime_account_id"),
                        res.getInt("forum_account_id"),
                        res.getString("referral_code"),
                        res.getInt("point")
                );
                return resultRef;
            } else {
                System.out.println("There's no such row with forum_account_id: "+ forum_account_id);
                return null;
            }
        } catch (Exception e){
            System.out.println("Checking Failed");
            return null;
        }
    }

    public void insertReferenceDatabase(Reference reference){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("INSERT INTO reference(anime_account_id, forum_account_id, referral_code, point) VALUES (%s,%s,'%s',%s);",
                    reference.getAnimeAccountId(), reference.getForumAccountId() == null || reference.getForumAccountId() == 0? "NULL" : reference.getForumAccountId(), reference.getReferalCode(), reference.getPoint());
            stmt.executeUpdate(q);
            System.out.println("Successfully insert a new Reference data");
        } catch (Exception e){
            System.out.println("Failed inserting reference to Database");
        }

    }

    public void updateReferenceDatabase(Reference reference){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("UPDATE reference SET anime_account_id = %s, forum_account_id = %s, referral_code = '%s', point = %s WHERE id = %s;",
                    reference.getAnimeAccountId(), reference.getForumAccountId() == null || reference.getForumAccountId() == 0? "NULL" : reference.getForumAccountId(), reference.getReferalCode(),reference.getPoint(), reference.getId());
            stmt.executeUpdate(q);
            System.out.println("Successfully update Reference data with id: "+ reference.getId());
        } catch (Exception e){
            System.out.println("Failed updating reference in Database");
        }
    }

    public void deleteReferenceDatabase(int ref_id){
        try {
            Statement stmt = this.connection.createStatement();
            String q = String.format("DELETE FROM reference WHERE id = %s;", ref_id);
            stmt.executeUpdate(q);
            System.out.println("Successfully delete Reference data with id: "+ ref_id);
        } catch (Exception e){
            System.out.println("Failed deleting reference from Database");
        }
    }
}
