
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
 * Manages the persistence of customers and sellers in a text file.
 *
 * Provides operations to load and save person data.
 * Converts file operation failures into PersistenceException.
 *
 * @author USUARIO
 */
public class PersonDAO {

    private final String filePath;

    /**
     * Creates a PersonDAO using the specified file path.
     *
     * @param filePath path of the file used to store person data
     */
    public PersonDAO(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Loads customers and sellers from the data file.
     *
     * Returns an empty list if the file does not exist.
     * Preserves the original data format for both person types.
     *
     * @return a list containing the persons loaded from the file
     * @throws PersistenceException if the file cannot be read
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

                String[] data = line.split(";", -1);

                if (data.length != 5) {
                    continue;
                }

                if (data[0].equals("CUSTOMER")) {

                    Customer customer = new Customer(
                            data[2],
                            data[1],
                            data[3],
                            data[4]
                    );

                    persons.add(customer);

                } else if (data[0].equals("SELLER")) {

                    Seller seller = new Seller(
                            data[2],
                            data[1],
                            data[3],
                            data[4]
                    );

                    persons.add(seller);

                } else {
                    System.out.println(
                            "Invalid person type in persons file."
                    );
                }
            }

        } catch (IOException e) {
            throw new PersistenceException(
                    "Error reading persons from file: " + filePath,
                    e
            );
        }

        return persons;
    }

    /**
     * Saves the specified persons to the data file.
     *
     * Customers and sellers are stored according to their type.
     * Creates the parent directory when necessary.
     *
     * @param persons list of persons to save
     * @throws PersistenceException if the file cannot be written
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
            throw new PersistenceException(
                    "Error saving persons to file: " + filePath,
                    e
            );
        }
    }
}