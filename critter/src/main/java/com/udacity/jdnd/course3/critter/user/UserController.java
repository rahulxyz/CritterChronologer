package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.Utils;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    CustomerService customerService;

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PetService petService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        Customer customer = Utils.convertDTOToCustomerEntity(customerDTO);

        //convert List<petIds> to List<Pet>
        List<Long> petIds = customerDTO.getPetIds();
        List<Pet> petList;
        if(petIds == null){
            petList = new ArrayList<>();
        }else{
            petList = petIds.stream().map(id -> petService.getPetById(id)).collect(Collectors.toList());
        }
        customer.setPets(petList);

        customer = customerService.save(customer);
        customerDTO.setId(customer.getId());
        return customerDTO;
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        List<Customer> customerList = customerService.getAllCustomer();
        List<CustomerDTO> customerDTOList = customerList.stream().map(customer -> Utils.convertEntityToCustomerDTO(customer)).collect(Collectors.toList());
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable Long petId){

        //throw new UnsupportedOperationException();
        Pet pet = petService.getPetById(petId);
        if(pet == null){
            throw new UnsupportedOperationException("No pet found");
        }

        Customer customer = customerService.getCustomerByPets(pet);
        CustomerDTO customerDTO = Utils.convertEntityToCustomerDTO(customer);
        customerDTO.setPetIds(Arrays.asList(petId));
        return customerDTO;
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        Employee employee = Utils.convertDTOToEmployeeEntity(employeeDTO);
        employee = employeeService.save(employee);
        employeeDTO.setId(employee.getId());
        return employeeDTO;
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        Employee employee = employeeService.findByEmployeeId(employeeId);
        return Utils.convertEntityToEmployeeDTO(employee);
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        employeeService.setEmployeeAvailability(daysAvailable, employeeId);
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        List<Employee> employees = employeeService.findEmployeesBySkillAndDate(employeeDTO.getSkills(), employeeDTO.getDate());
        List<EmployeeDTO> employeeDTOs = employees.stream().map(e-> Utils.convertEntityToEmployeeDTO(e)).collect(Collectors.toList());
        return employeeDTOs;
    }

}
