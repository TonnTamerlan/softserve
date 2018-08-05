/**
 * 
 */
package com.softserveinc.test_task.dao;

import java.util.Map;

import com.softserveinc.test_task.data_sets.Company;
import com.softserveinc.test_task.data_sets.Department;

/**
 * The interface for working with specified company
 * 
 * @author Alexey Kopylov
 *
 */
public interface CompanyDAO {
    
    /**
     * The method show how many employees in 
     * company within the desired age bounds works in each department in specified district.
     * 
     * @param startAgeBound - start of age bound inclusively
     * @param endAgeBound - end of age bound inclusively
     * @param districtName - name of the district in which place departments with employees
     * @return The map with Departments in the specified district and the count of employees with age in the specify age bounds
     * @throws DAOException 
     */
    Map<Department, Integer> getCountOfEpmloyeesWithAgeInBoundsAndDepartmentInDistrict(
            int startAgeBound, int endAgeBound, String districtName) throws DAOException;
    
    
    /**
     * Get entity {@link Company} for the specified company
     * 
     * @return the company with its staff and departments
     * @throws DAOException
     */
    Company getCompany() throws DAOException;
    
    /**
     * Fill the company with fake data (staff and departments) 
     * 
     * @param staffNumbers - a number of person, which is desired to add like staff
     * @throws DAOException
     */
    void fillFakeData(int staffNumbers) throws DAOException;

}
