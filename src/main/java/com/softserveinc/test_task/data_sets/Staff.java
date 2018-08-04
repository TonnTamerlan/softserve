package com.softserveinc.test_task.data_sets;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Entity for staff
 * 
 * @author Alexey Koylov
 *
 */
@Data
@AllArgsConstructor
public class Staff {
    private int id;
    private String name;
    private int age;
    
    public Staff(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
}
