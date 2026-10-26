package com.misco.gamezone.dao;

import com.misco.gamezone.exceptions.PersistenceException;
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
 * Handles file persistence for customers and sellers.
 *
 * GRASP:
 * - Pure Fabrication: isolates persistence logic from domain entities.
 * - High Cohesion: focuses on loading and saving person data.
 * - Protected Variations: hides file access details behind a stable interface.
 */
public class PersonDAO {

    private final Path filePath;

    public PersonDAO(String filePath) {
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "File path cannot be null or empty.");
        }

        this.filePath = Path.of(filePath);
    }

    /**
     * Loads customers and sellers from the data file.
     *
     * @return persons loaded from the file
     * @throws PersistenceException if the file cannot be read
     */
    public List<Person> loadPersons() {
        List<Person> persons = new ArrayList<>();

        if (!Files.exists(filePath)) {
            return persons;
        }

        try (BufferedReader reader = Files.newBufferedReader(filePath)) {
            String line;

            while ((line = reader.readLine()) != null) {
                Person person = parsePerson(line);

                if (person != null) {
                    persons.add(person);
                }
            }
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error reading persons from file: " + filePath, e);
        }

        return persons;
    }

    /**
     * Converts a stored line into a person entity.
     *
     * @param line line read from the file
     * @return the person represented by the line, or null if invalid
     */
    private Person parsePerson(String line) {
        String[] data = line.split(";", -1);

        if (data.length != 5) {
            return null;
        }

        switch (data[0]) {
            case "CUSTOMER":
                return new Customer(data[2], data[1], data[3], data[4]);

            case "SELLER":
                return new Seller(data[2], data[1], data[3], data[4]);

            default:
                return null;
        }
    }

    /**
     * Saves the specified persons to the data file.
     *
     * @param persons persons to persist
     * @throws PersistenceException if the file cannot be written
     */
    public void savePersons(List<Person> persons) {
        if (persons == null) {
            throw new IllegalArgumentException(
                    "Persons list cannot be null.");
        }

        try {
            if (filePath.getParent() != null) {
                Files.createDirectories(filePath.getParent());
            }

            try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
                for (Person person : persons) {
                    if (person == null) {
                        continue;
                    }

                    String line = formatPerson(person);

                    if (line != null) {
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
        } catch (IOException e) {
            throw new PersistenceException(
                    "Error saving persons to file: " + filePath, e);
        }
    }

    /**
     * Converts a person entity into its stored representation.
     *
     * @param person person to convert
     * @return formatted line, or null if the person type is unsupported
     */
    private String formatPerson(Person person) {
        if (person instanceof Customer) {
            Customer customer = (Customer) person;

            return "CUSTOMER;"
                    + customer.getId() + ";"
                    + customer.getName() + ";"
                    + customer.getCellphone() + ";"
                    + customer.getEmail();
        }

        if (person instanceof Seller) {
            Seller seller = (Seller) person;

            return "SELLER;"
                    + seller.getId() + ";"
                    + seller.getName() + ";"
                    + seller.getCellphone() + ";"
                    + seller.getEmployeeCode();
        }

        return null;
    }
}