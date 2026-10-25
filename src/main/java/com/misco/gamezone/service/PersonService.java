/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.service;

import com.misco.gamezone.dao.PersonDAO;
import com.misco.gamezone.exceptions.BusinessRuleException;
import com.misco.gamezone.exceptions.InvalidDataException;
import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Seller;
import com.misco.gamezone.validation.PersonValidator;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides business operations related to customers and sellers.
 *
 * Manages customer registration and lists persons according to their role.
 * Uses PersonValidator to validate customer data and prevent duplicate IDs.
 *
 * @author USUARIO
 */
public class PersonService {

    private final PersonDAO personDAO;
    private List<Person> persons;

    /**
     * Creates a PersonService and loads the existing persons.
     *
     * @param personDAO DAO used to manage person persistence
     */
    public PersonService(PersonDAO personDAO) {
        this.personDAO = personDAO;
        this.persons = personDAO.loadPersons();
    }

    /**
     * Registers a new customer and saves the updated person list.
     *
     * Validates the customer information and verifies that the
     * identification code is not already registered.
     *
     * @param name name of the customer
     * @param id identification code of the customer
     * @param cellphone phone number of the customer
     * @param email email address of the customer
     * @throws InvalidDataException if customer information is invalid
     * @throws BusinessRuleException if the identification code already exists
     */
    public void registerCustomer(
            String name,
            String id,
            String cellphone,
            String email) {

        PersonValidator.validatePersonData(id, name, email, cellphone);

        persons = personDAO.loadPersons();

        PersonValidator.validateUniquePerson(persons, id);

        Customer customer = new Customer(name, id, cellphone, email);

        persons.add(customer);
        personDAO.savePersons(persons);
    }

    /**
     * Returns all customers registered in the system.
     *
     * Reloads the person list and filters records by the Customer type.
     *
     * @return a list containing all registered customers
     */
    public List<Customer> listCustomers() {

        persons = personDAO.loadPersons();

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
     * Reloads the person list and filters records by the Seller type.
     *
     * @return a list containing all registered sellers
     */
    public List<Seller> listSellers() {

        persons = personDAO.loadPersons();

        List<Seller> sellers = new ArrayList<>();

        for (Person person : persons) {
            if (person instanceof Seller) {
                sellers.add((Seller) person);
            }
        }

        return sellers;
    }
}