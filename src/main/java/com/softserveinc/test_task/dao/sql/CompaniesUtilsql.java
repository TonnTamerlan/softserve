package com.softserveinc.test_task.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

import com.softserveinc.test_task.dao.DAOException;
import com.softserveinc.test_task.dao.db_service.DBServiceMySQL;
import com.softserveinc.test_task.data_sets.Department;
import com.softserveinc.test_task.data_sets.StaffNumberInDepartment;

/**
 * The class presents utilities for working with more than one companies
 * 
 * @author Alexey Kopylov
 *
 */
public class CompaniesUtilsql {
    
    private CompaniesUtilsql() {}
    
    
    /**
     * Get information from two companies about number of employees with age
     * in age bounds and works in departments which place in specified district 
     * 
     * @param startAge - the start of age bounds inclusively
     * @param endAge - the end of age bounds inclusively
     * @param district - the specified district
     * @param schemaOne - the schema with first company data
     * @param schemaTwo - the schema with second company data
     * @return The list of departments with number of employees. The method doesn't group departments 
     * with equals names. If companies have departments with
     * equals names, then this department will be as two different departments 
     * @throws DAOException
     */
    public static List<StaffNumberInDepartment> getNumbersOfEmployeesWithAgeInBoundsAndPlaceInDistrict(int startAge,
            int endAge, String district, String schemaOne, String schemaTwo) throws DAOException {
        
        List<StaffNumberInDepartment> result = new ArrayList<>();
        
        String sql = generateSQLqueryForCountingNumbersOfEmployees(schemaOne, schemaTwo);
        
        try (Connection connection = DBServiceMySQL.getConnection(); 
                PreparedStatement statement = connection.prepareStatement(sql)){
            
            // for schema one
            statement.setString(1, district);
            statement.setInt(2, startAge);
            statement.setInt(3, endAge);
            
            // for schema two
            statement.setString(4, district);
            statement.setInt(5, startAge);
            statement.setInt(6, endAge);
            
            
            ResultSet resultSet = statement.executeQuery();
            while(resultSet.next()) {
                int departmentId = resultSet.getInt("id");
                String departmentName = resultSet.getString("name");
                String districtName = resultSet.getString("district");
                int numberOfStaff = resultSet.getInt("staff");
                Department department = new Department(departmentId, departmentName, districtName);
                result.add(new StaffNumberInDepartment(department, numberOfStaff));
            }
            return result;
        } catch (SQLException | ClassNotFoundException e) {

            String message = "Cannot get data with count of users with age in bounds"
                    + " and working in the specified district from the database";
            throw new DAOException(message, e);
        }
    }

    private static String generateSQLqueryForCountingNumbersOfEmployees(String schemaOne, String schemaTwo) {
        String result = "SELECT " + schemaOne +".departments.id, " + schemaOne +".departments.name, " + schemaOne +".departments.district, COUNT(" + schemaOne +".staff.id) AS staff " + 
                "FROM " + schemaOne +".departments " + 
                "RIGHT JOIN " + schemaOne + ".employees ON " + schemaOne + ".employees.id_department = " + schemaOne + ".departments.id " + 
                "RIGHT JOIN " + schemaOne + ".staff ON " + schemaOne + ".staff.id = " + schemaOne + ".employees.id_staff " + 
                "WHERE district = ? AND age BETWEEN ? AND ? " + 
                "GROUP BY " + schemaOne + ".departments.name " + 
                "UNION " + 
                "SELECT " + schemaTwo + ".departments.id, " + schemaTwo + ".departments.name, " + schemaTwo + ".departments.district, COUNT(" + schemaTwo + ".staff.id) AS staff " + 
                "FROM " + schemaTwo + ".departments " + 
                "RIGHT JOIN " + schemaTwo + ".employees ON " + schemaTwo + ".employees.id_department = " + schemaTwo + ".departments.id " + 
                "RIGHT JOIN " + schemaTwo + ".staff ON " + schemaTwo + ".staff.id = " + schemaTwo + ".employees.id_staff " + 
                "WHERE district = ? AND age BETWEEN ? AND ? " + 
                "GROUP BY " + schemaTwo + ".departments.name";

        return result;
    }

}
