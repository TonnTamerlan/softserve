package com.softserveinc.test_task.main;


import java.util.Date;

import com.softserveinc.test_task.dao.CompanyDAO;
import com.softserveinc.test_task.dao.db_service.DBServiceMySQL;
import com.softserveinc.test_task.dao.sql.CompaniesUtilsql;
import com.softserveinc.test_task.dao.sql.CompanyDAOsql;

/**
 * @author Alexey Kopylov
 *
 */
public class Main {

    public static void main(String[] args) throws Exception {
        
        // Create schemas and fill fake data for company one
        String companyName_1 = "Test company 1";
        String schemaName_1 = "test_1";

        System.out.println("Create schema for company '" +companyName_1 + "'");
        DBServiceMySQL.dropSchema(schemaName_1);
        DBServiceMySQL.createSchema(schemaName_1);
        
        System.out.println("Filling schema with fake data for company '" +companyName_1 + "'...");
        CompanyDAO companyDAO_1 = new CompanyDAOsql(companyName_1, schemaName_1);
        companyDAO_1.fillFakeData(1_000_000);
        System.out.println("The schema for company '" +companyName_1 + "' was filling");

        
        
        // Create schemas and fill fake data for company two
        String companyName_2 = "Test company 2";
        String schemaName_2 = "test_2";
        
        System.out.println("Create schema for company '" +companyName_2 + "'");
        DBServiceMySQL.dropSchema(schemaName_2);
        DBServiceMySQL.createSchema(schemaName_2);
        
        System.out.println("Filling schema with fake data for company '" +companyName_2 + "'...");
        CompanyDAO companyDAO_2 = new CompanyDAOsql(companyName_2, schemaName_2);
        companyDAO_2.fillFakeData(1_000_000);
        System.out.println("The schema for company '" +companyName_2 + "' was filling");
        
        
        // Create query in the database and print result
        System.out.println("Create query in the database...");
        // Print result in console
        System.out.println("----------------ANSWER----------------");
        CompaniesUtilsql.getNumbersOfEmployeesWithAgeInBoundsAndPlaceInDistrict(16, 32, "KYIV", schemaName_1, schemaName_2).forEach(item -> {
           System.out.println(item.getDepartment().getName() + ": " + item.getNumber());
        });
        
    }

}
