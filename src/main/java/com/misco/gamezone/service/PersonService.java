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
 * Provides the business operations related to customers and sellers. Manages
 * customer registration and the listing of persons by their role.
 *
 * @author USUARIO
 */
public class PersonService {

    private PersonDAO personDAO;
    private List<Person> persons;

    /**
     * Creates a PersonService using the specified PersonDAO and loads the
     * existing persons from the data file.
     *
     * @param personDAO the DAO used to manage person persistence
     */
    public PersonService(PersonDAO personDAO) {
        this.personDAO = personDAO;
        this.persons = personDAO.loadPersons();
    }

    /**
     * Registers a new customer and saves the updated person list.
     *
     * @param name the name of the customer
     * @param id the identification code of the customer
     * @param cellphone the phone number of the customer
     * @param email the email address of the customer
     */
    public void registerCustomer(String name, String id, String cellphone, String email) {
        Customer customer = new Customer(name, id, cellphone, email);
        persons.add(customer);
        personDAO.savePersons(persons);
    }

    /**
     * Returns all customers registered in the system.
     *
     * @return a list containing all customers
     */
    public List<Customer> listCustomers() {
        List<Customer> customers = new ArrayList<>();

        for (Person person : persons) {
            if (person instanceof Customer) {
                customers.add((Customer) person);
            }
        }

        return customers;
    }

    /**
     * Returns all sellers registered in the system.
     *
     * @return a list containing all sellers
     */
    public List<Seller> listSellers() {
        List<Seller> sellers = new ArrayList<>();

        for (Person person : persons) {
            if (person instanceof Seller) {
                sellers.add((Seller) person);
            }
        }

        return sellers;
    }

}
