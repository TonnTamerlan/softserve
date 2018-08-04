package com.softserveinc.test_task.db_service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * The class for creating and managing connection to the database
 * 
 * @author Alexey Kopylov
 *
 */
public class DBServiceMySQL {
    
    private static final String LOGIN = "root";
    private static final String PASSWORD = "root";
    private static final String USE_SSL = "false";
    private static final String SERVER_TIMEZONE = "UTC";
    

    private DBServiceMySQL() {
    }

    /**
     * Create connection to the database and the specified schema
     * 
     * @param schemaName - the name of specified schema
     * 
     * @return connection - the connection to the database and the specified schema
     * @throws ClassNotFoundException - if there is cannot find the class with the driver for the database
     * @throws SQLException -  if a database access error occurs
     *  
     */
    public static Connection getConnection(String schemaName) throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/" + schemaName;
        Properties properties = getProperties();
        Connection connection = DriverManager.getConnection(url.toString(), properties);
        return connection;
    }
    
    /**
     * Create connection to the database
     * 
     * @return connection - the connection to the database
     * @throws ClassNotFoundException - if there is cannot find the class with the driver for the database
     * @throws SQLException -  if a database access error occurs
     *  
     */
    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        return getConnection("");
    }
    
    
    
    /**
     * The method for creating schema with the specified name
     * 
     * @param schemaName - the desired name of the schema
     * @throws SQLException -  if something wrong with database access
     * @throws ClassNotFoundException - if there is cannot find the class with the driver for the database
     */
    public static void createSchema(String schemaName) throws SQLException, ClassNotFoundException {
        String sql = "CREATE SCHEMA IF NOT EXISTS " + schemaName + " CHARSET utf8";
        try (Connection connection = getConnection(); 
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }
    
    /**
     * The method for dropping schema with the specified name
     * 
     * @param schemaName - the desired name of the schema
     * @throws SQLException -  if something wrong with database access
     * @throws ClassNotFoundException - if there is cannot find the class with the driver for the database
     */
    public static void dropSchema(String schemaName) throws ClassNotFoundException, SQLException {
        String sql = "DROP SCHEMA IF EXISTS " + schemaName;
        try (Connection connection = getConnection(); 
                Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new SQLException("Cannot drop the schema with name \'" + schemaName +"\'", e);
        }
    }

    private static Properties getProperties() {
        Properties properties = new Properties();
        properties.setProperty("user", LOGIN);
        properties.setProperty("password", PASSWORD);
        properties.setProperty("useSSL", USE_SSL);
        properties.setProperty("serverTimezone", SERVER_TIMEZONE);

        return properties;
    }
    
}
