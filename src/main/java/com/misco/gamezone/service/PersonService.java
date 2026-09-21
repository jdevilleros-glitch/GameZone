/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Seller;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author USUARIO
 */
public class PersonService {
    private PersonDAO personDAO;
    private List<Person> persons;

    public PersonService(PersonDAO personDAO) {
        this.personDAO = personDAO;
        this.persons = personDAO.loadPersons();
    }
    
    public void registerCustomer(String name, String id, String cellphone, String email){
        Customer customer = new Customer(name, id, cellphone, email);
        persons.add(customer);
        personDAO.savePersons(persons);
    }
    
    public List<Customer> listCustomers(){
        List<Customer> customers = new ArrayList<>();
        
        for (Person person : persons){
            if (person instanceof Customer){
                customers.add((Customer) person);
            }
        }
        
        return customers;
    }
    
    public List<Seller> listSellers(){
        List<Seller> sellers = new ArrayList<>();
        
        for (Person person : persons){
            if (person instanceof Seller){
                sellers.add((Seller) person);
            }
        }
        
        return sellers;
    }
    
    
}
