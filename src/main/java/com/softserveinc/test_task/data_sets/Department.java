package com.softserveinc.test_task.data_sets;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Entity for department
 * 
 * @author Alexey Kopylov
 *
 */
@Data
@AllArgsConstructor
public class Department {
    private int id;
    private String name;
    private String district;
    
    public Department(String name, String district) {
        this.name = name;
        this.district = district;
    }
}
