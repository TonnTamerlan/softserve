package com.softserveinc.test_task.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.softserveinc.test_task.dao.CompanyDAO;
import com.softserveinc.test_task.dao.DAOException;
import com.softserveinc.test_task.dao.db_service.DBServiceMySQL;
import com.softserveinc.test_task.data_sets.Company;
import com.softserveinc.test_task.data_sets.Department;
import com.softserveinc.test_task.data_sets.Staff;


/**
 * The implementation of the interface {@link CompanyDAO} for working with database which supports standard SQL
 * 
 * @author Alexey Kopylov
 *
 */
public class CompanyDAOsql implements CompanyDAO {

    private final String companyName;
    private final String schemaName;
    
    public CompanyDAOsql(String companyName, String schemaName) throws DAOException {
        this.companyName = companyName;
        this.schemaName = schemaName;
        createTables();
    }
    
private void createTables() throws DAOException {
        
        String sqlCreateStaff = "CREATE TABLE IF NOT EXISTS staff (" 
                              + "id INT NOT NULL AUTO_INCREMENT,"
                              + "name VARCHAR(45) NOT NULL,"
                              + "age INT NOT NULL,"
                              + "PRIMARY KEY (id)," 
                              + "UNIQUE INDEX id_UNIQUE (id ASC));";
        
        String sqlCreateDepartmets = "CREATE TABLE IF NOT EXISTS departments ("
                                   + "id INT NOT NULL AUTO_INCREMENT,"
                                   + "name VARCHAR(45) NOT NULL,"
                                   + "district VARCHAR(45) NOT NULL,"
                                   + "PRIMARY KEY (id)," 
                                   + "UNIQUE INDEX id_UNIQUE (id ASC));";
        String sqlCreateEmployees = "CREATE TABLE IF NOT EXISTS employees (" 
                                  + "id_staff INT NOT NULL,"
                                  + "id_department INT NOT NULL," 
                                  + "UNIQUE INDEX id_staff_UNIQUE (id_staff ASC));";
        Connection connection = null;
        Statement statement = null;
        try {
            DBServiceMySQL.createSchema(schemaName);
            connection = DBServiceMySQL.getConnection(schemaName);
            statement = connection.createStatement();
            statement.addBatch(sqlCreateStaff);
            statement.addBatch(sqlCreateDepartmets);
            statement.addBatch(sqlCreateEmployees);
            statement.executeBatch();
        } catch (SQLException | ClassNotFoundException e) {
            String message = "Cannot create tables";
            throw new DAOException(message, e);
        } finally {
            if (statement != null) {
                try {
                    statement.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Override
    public Map<Department, Integer> getCountOfEpmloyeesWithAgeInBoundsAndDepartmentInDistrict(
            int startAgeBound, int endAgeBound, String districtName) throws DAOException {
        Map<Department, Integer> result = new HashMap<>();
        try (Connection connection = DBServiceMySQL.getConnection(schemaName)){

            DepartmentDAOsql departmentDAO = new DepartmentDAOsql(connection);
            List<Department> departments =  departmentDAO.getByDistrict(districtName);
            
            for(Department singleDeparment : departments) {
                int numberOfEmployees = countNumbersOfEmpoyeesWithAgeInBoundsInDepartment(connection, singleDeparment.getId(),
                        startAgeBound, endAgeBound);
                result.put(singleDeparment, numberOfEmployees);
            }
            return result;
        } catch (SQLException | ClassNotFoundException e) {

            String message = "Cannot get data with count of users with age in bounds"
                    + " and working in the specified district from the database ";
            throw new DAOException(message, e);
        }
        
    }
    
    
    
    private int countNumbersOfEmpoyeesWithAgeInBoundsInDepartment(Connection connection, int id, int startAgeBound,
            int endAgeBound) throws SQLException {
        String sql = "SELECT COUNT(*) FROM staff where id = ANY (select id_staff from employees where id_department = ?) and age >= ? and age <= ?";
        int result = -1;
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setInt(1, id);
            statement.setInt(2, startAgeBound);
            statement.setInt(3, endAgeBound);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            result = resultSet.getInt(1);
            return result;
        }
    }
    
    @Override
    public Company getCompany() throws DAOException {
        Company company = null;
        
        try (Connection connection = DBServiceMySQL.getConnection(schemaName)) {
            DepartmentDAOsql departmentDAO = new DepartmentDAOsql(connection);
            List<Department> departments = departmentDAO.getAll();
            
            StaffDAOsql staffDAO = new StaffDAOsql(connection);
            List<Staff> staff = staffDAO.getAll();
            
            company = new Company(companyName, departments, staff);
            
            return company;
        } catch (SQLException | ClassNotFoundException e) {
            String message = "Cannot get company";
            throw new DAOException(message, e);
        }
    }

    @Override
    public void fillFakeData(int staffNumbers) throws DAOException {
        try (Connection connection = DBServiceMySQL.getConnection(schemaName)) {
            connection.setAutoCommit(false);
            
            StaffDAOsql staffDAO = new StaffDAOsql(connection);
            staffDAO.fillFakeData(staffNumbers);
            connection.commit();

            DepartmentDAOsql departmentDAO = new DepartmentDAOsql(connection);
            departmentDAO.fillFakeData();
            connection.commit();
            
            List<Staff> staff = staffDAO.getAll();
            List<Department> departments = departmentDAO.getAll();
            
            fillEmployeesFakeData(connection, staff, departments);
            connection.commit();
            
        } catch (SQLException | ClassNotFoundException e) {
            String message = "Cannot fill schema";
            throw new DAOException(message, e);
        }
    }

    private void fillEmployeesFakeData(Connection connection, List<Staff> staff, List<Department> departments) throws SQLException {
        String sql = "INSERT INTO employees (id_staff, id_department) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)){
            Random random = new Random();
            for(int i = 0; i < staff.size(); i++) {
                Staff person = staff.get(i);
                statement.setInt(1, person.getId());
                statement.setInt(2, departments.get(random.nextInt(departments.size())).getId());
                statement.addBatch();
                if(i % 10000 == 0) {
                    statement.executeLargeBatch();
                }
            }
            statement.executeLargeBatch();
            
        }
        
    }

}
