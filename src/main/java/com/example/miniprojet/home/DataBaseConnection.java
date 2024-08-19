package com.example.miniprojet.home;

import java.sql.Connection;
import java.sql.DriverManager;
public class DataBaseConnection {
    public Connection databaseLink;
    public Connection getConnection() {
        String databaseName = "javadb";
        String databaseUser = "root";
        String databasePassword="";
        String databaseUrl = "jdbc:mysql://localhost/"+databaseName;
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            databaseLink = DriverManager.getConnection(databaseUrl, databaseUser, databasePassword);

        }catch (Exception e){
            e.printStackTrace();

        }
        return databaseLink;


    }
}

