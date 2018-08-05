package com.softserveinc.test_task.data_sets;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * The entity for company
 * 
 * @author Alexey Kopylov
 *
 */

@Data
@AllArgsConstructor
public class Company {
    
    private String name;
    private List<Department> departments;
    private List<Staff> staff;

}
