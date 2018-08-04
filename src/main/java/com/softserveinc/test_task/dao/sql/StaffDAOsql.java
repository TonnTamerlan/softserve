package com.softserveinc.test_task.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.softserveinc.test_task.data_sets.Staff;

/**
 * The class for working with entity {@link Staff} using the database which supports standard SQL
 * 
 * @author Alexey Kopylov
 *
 */

class StaffDAOsql {
    
    
    // Fake first names
    private static final String[] FIRST_NAMES = { "Akexey", "Vladimir", "Anna", "Elena", "Alexander", "Victor", "Bogdan", "Olga",
            "Ivan", "Vasiliy" };
    
    // Fake last names
    private static final String[] LAST_NAMES = { "Petrenko", "Sidorenko", "Ivanko", "Kapustenko", "Pershevich", "Sovkun",
            "Borodavchenko", "Sokol", "Piven", "Pushkin" };
    
    // Min staff age
    private static final int MIN_AGE = 14;
    
    // Max staff age
    private static final int MAX_AGE = 80;
    
    private final Connection connection;
    
    public StaffDAOsql(Connection connection) {
        this.connection = connection;
    }

    
    /**
     * Add staff to the database
     * 
     * @param stuff - which is needed to add
     * @throws SQLException
     */
    void add(Staff stuff) throws SQLException {
        String sql = "INSERT INTO staff (name, age) values (?, ?)";
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            statement.setString(1, stuff.getName());
            statement.setInt(2, stuff.getAge());
            statement.executeUpdate();
        }
    }

    /**
     * Get list of all staff
     * 
     * @return the list of all staff
     * @throws SQLException
     */
    List<Staff> getAll() throws SQLException {
        List<Staff> staff = new ArrayList<>();
        String sql = "SELECT * FROM staff";
        
        try(PreparedStatement statement = connection.prepareStatement(sql)){
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                Staff current = new Staff(result.getInt("id"), result.getString("name"), result.getInt("age"));
                staff.add(current);
            }
            return staff;
        }
    }
    
    /**
     * Fill the table "staff" with fake data
     * 
     * @param fieldsNumber - a number of person which need to add as fake data
     * @throws SQLException
     */
    void fillFakeData(int fieldsNumber) throws SQLException {
        String sql = "INSERT INTO staff (name, age) values (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            Random random = new Random();
            for (int i = 0; i < fieldsNumber; i++) {
                String firstName = FIRST_NAMES[random.nextInt(FIRST_NAMES.length)];
                String lastName = LAST_NAMES[random.nextInt(LAST_NAMES.length)];
                int age = MIN_AGE + random.nextInt(MAX_AGE - MIN_AGE);
                statement.setString(1, lastName + " " + firstName);
                statement.setInt(2, age);
                statement.addBatch();
            }
            statement.executeLargeBatch();
        }
    }
    

}
