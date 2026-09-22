/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.dao;

import com.misco.gamezone.model.Customer;
import com.misco.gamezone.model.Person;
import com.misco.gamezone.model.Seller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the persistence of customers and sellers in a text file. Provides
 * operations to load and save person data.
 *
 * @author USUARIO
 */
public class PersonDAO {

    private final String filePath;

    /**
     * Creates a PersonDAO using the specified file path.
     *
     * @param filePath the path of the file used to store person data
     */
    public PersonDAO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads customers and sellers from the data file.
     *
     * @return a list containing the persons loaded from the file
     */
    public List<Person> loadPersons() {
        List<Person> persons = new ArrayList<>();

        Path path = Path.of(filePath);

        if (!Files.exists(path)) {
            return persons;
        }

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                if (data.length != 5) {
                    continue;
                }

                if (data[0].equals("CUSTOMER")) {
                    Customer customer = new Customer(
                            data[2], // name
                            data[1], // id
                            data[3], // cellphone
                            data[4] // email
                    );

                    persons.add(customer);

                } else if (data[0].equals("SELLER")) {
                    Seller seller = new Seller(
                            data[2], // name
                            data[1], // id
                            data[3], // cellphone
                            data[4] // employeeCode
                    );

                    persons.add(seller);

                } else {
                    System.out.println("Invalid person type");
                }
            }
        } catch (IOException ex) {
            System.out.println("Error reading persons file");
        }

        return persons;

    }

    /**
     * Saves the list of persons in the data file. Customers and sellers are
     * stored according to their specific type.
     *
     * @param persons the list of persons to save
     */
    public void savePersons(List<Person> persons) {
        Path path = Path.of(filePath);

        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(path)) {

                for (Person person : persons) {

                    if (person instanceof Customer) {
                        Customer customer = (Customer) person;
                        String line = "CUSTOMER" + ";"
                                + customer.getId() + ";"
                                + customer.getName() + ";"
                                + customer.getCellphone() + ";"
                                + customer.getEmail();

                        writer.write(line);
                        writer.newLine();

                    } else if (person instanceof Seller) {
                        Seller seller = (Seller) person;
                        String line = "SELLER" + ";"
                                + seller.getId() + ";"
                                + seller.getName() + ";"
                                + seller.getCellphone() + ";"
                                + seller.getEmployeeCode();

                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error saving persons file");
        }
    }

}
