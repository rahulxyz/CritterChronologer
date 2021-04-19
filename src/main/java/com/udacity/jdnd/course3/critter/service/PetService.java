package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.pet.PetDTO;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Pet save(Pet pet){
        Pet returnedPet= petRepository.save(pet);

        Customer customer = returnedPet.getCustomer();
        customer.addPet(returnedPet);
        customerRepository.save(customer);

        return returnedPet;
    }

    public Pet getPetById(long id){
        return petRepository.findOneById(id);
    }

    public List<Pet> getAllPet(){
        return petRepository.findAll();
    }


    public List<Pet> findAllByCustomerId(long ownerId) {
        return petRepository.findAllByCustomerId(ownerId);
    }
}

