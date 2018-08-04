package com.softserveinc.test_task.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.softserveinc.test_task.data_sets.Department;

/**
 * The class for working with entity {@link Department} using the database which supports standard SQL
 * 
 * @author Alexey Kopylov
 *
 */
class DepartmentDAOsql {
    
    
    // Fake department names
    private static final String[] NAMES = {"Financial department", "Custom service department", "Technical department",
            "HR department", "Easy fun department", "Software development department", "Marketing department",
            "Accounting department"};
    
    // Fake districts
    private static final String[] DISTRICTS = {"KYIV", "LVIV", "DNIPRO", "NEW YORK", "LONDON", "TASHKENT", "BERLIN",
            "AMSTERDAM", "PARIS", "SIDNEY"};
    
    private final Connection connection;
    
    DepartmentDAOsql(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Add department to the database
     * 
     * @param department which is needed to add
     * @throws SQLException
     */
    void add(Department department) throws SQLException {
        String sql = "INSERT INTO departments (name, district) values (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, department.getName());
            statement.setString(2, department.getDistrict());
            statement.executeUpdate();
        }
    }

    /**
     * Get list of all departments
     * 
     * @return the list of all departments
     * @throws SQLException
     */
    List<Department> getAll() throws SQLException {
        List<Department> departments = new ArrayList<>();
        String sql = "SELECT * FROM departments";
        
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Department current = new Department(result.getInt("id"), result.getString("name"), result.getString("district"));
                departments.add(current);
            }
            return departments;
        }
    }
    
    /**
     * Fill the table "departments" with fake data
     * 
     * @throws SQLException
     */
    void fillFakeData() throws SQLException {
        String sql = "INSERT INTO departments (name, district) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            for (String district : DISTRICTS) {
                for (String name : NAMES) {
                    statement.setString(1, name);
                    statement.setString(2, district);
                    statement.addBatch();
                }
            }
            statement.executeLargeBatch();
        }
    }
    

}
