package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.util.HashSet;
import java.util.Set;

@Service
public class EmployeeService {
    @Autowired
    EmployeeRepository employeeRepository;

    public Employee save(Employee employee){
      return employeeRepository.save(employee);
    }

    public Employee findByEmployeeId(long id){
        return employeeRepository.findOneById(id);
    }

    public Employee getEmployeeById(long employeeId) {
        return employeeRepository.findOneById(employeeId);
    }

    public void setEmployeeAvailability(Set<DayOfWeek> daysAvailable, long employeeId) {
        Employee employee = this.getEmployeeById(employeeId);

        if(employee != null) {
            Set<DayOfWeek> days = employee.getDaysAvailable();
            if (days == null) {
                days = new HashSet<>();
            }

            days.addAll(daysAvailable);
            employee.setDaysAvailable(days);
            this.save(employee);
        }
    }
}
