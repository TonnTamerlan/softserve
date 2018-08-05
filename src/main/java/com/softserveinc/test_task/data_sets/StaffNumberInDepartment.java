package com.softserveinc.test_task.data_sets;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Class presents numbers of employees which work in the specified district
 * 
 * @author Alexey Kopylov
 *
 */
@Data
@AllArgsConstructor
public class StaffNumberInDepartment {
    
    private Department department;
    private int number;

}
