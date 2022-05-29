package com.company.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class DBConnection {
    private static Connection connection;
    private static Statement statement;

    public static void createTables(){
        if(statement!=null) {
            try {
                Scanner scanner = new Scanner(new File("src/db.txt"));
                String sql = "";
                while (scanner.hasNext()) {
                    while (!sql.contains(";")) {
                        sql += scanner.nextLine();
                    }
                    statement.execute(sql);
                    sql = "";
                }
                System.out.println(sql);
            } catch (FileNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void connect(String url){
        try {
            connection = DriverManager.getConnection(url);
            statement = connection.createStatement();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void disconnect(){
        if(connection!=null){
            try {
                connection.close();
                statement.close();
                statement = null;
                connection = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static Statement getStatement() {
        return statement;
    }

    public static Statement getNewStatement(){
        if(connection!=null) {
            try {
                return connection.createStatement();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
