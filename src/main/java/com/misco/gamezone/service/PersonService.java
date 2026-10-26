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

        PersonValidator.validatePersonData(
                id,
                name,
                email,
                cellphone
        );

        persons = personDAO.loadPersons();

        PersonValidator.validateUniquePerson(persons, id);

        Customer customer = new Customer(
                name,
                id,
                cellphone,
                email
        );

        persons.add(customer);

        personDAO.savePersons(persons);
    }

    /**
     * Returns all customers registered in the system.
     *
     * @return a list containing all registered customers
     */
    public List<Customer> listCustomers() {
        return listPersonsByType(Customer.class);
    }

    /**
     * Returns all sellers registered in the system.
     *
     * @return a list containing all registered sellers
     */
    public List<Seller> listSellers() {
        return listPersonsByType(Seller.class);
    }

    /**
     * Loads persons and returns only those matching
     * the specified subtype.
     *
     * @param <T> expected person subtype
     * @param type class representing the expected subtype
     * @return list of persons matching the specified type
     */
    private <T extends Person> List<T> listPersonsByType(Class<T> type) {

        persons = personDAO.loadPersons();

        List<T> filteredPersons = new ArrayList<>();

        for (Person person : persons) {

            if (type.isInstance(person)) {
                filteredPersons.add(type.cast(person));
            }
        }

        return filteredPersons;
    }
}