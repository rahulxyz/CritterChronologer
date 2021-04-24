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
import java.util.ArrayList;

@Service
@Transactional
public class PetService {

    @Autowired
    PetRepository petRepository;

    @Autowired
    CustomerRepository customerRepository;

    public Pet save(Pet pet){
        Pet returnedPet= petRepository.save(pet);

        //this.addPetToCustomer(pet,pet.getCustomer());
        Customer customer = returnedPet.getCustomer();
        customer.addPet(returnedPet);
        customerRepository.save(customer);

        return returnedPet;
    }

    public Pet getPetById(long id){
        return petRepository.findOneById(id);//.orElseThrow(()-> new ResourceNotFoundException("No Pet found"));
    }

    public List<Pet> getAllPet(){
        return petRepository.findAll();
    }


    public List<Pet> findAllByCustomerId(long ownerId) {
        return petRepository.findAllByCustomerId(ownerId);
    }

    public void addPetToCustomer(Pet pet, Customer customer){
        List<Pet> pets = customer.getPets();
        if (pets != null) {
            pets.add(pet);
        }else {
            pets = new ArrayList<Pet>();
            pets.add(pet);
        }

        customer.setPets(pets);
        customerRepository.save(customer);
    }


}

