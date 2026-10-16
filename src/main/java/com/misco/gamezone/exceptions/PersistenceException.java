/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.exceptions;

/**
 *
 * @author USUARIO
 * 
 * Represents an error that occurs during data persistence operations.
 */
public class PersistenceException extends GameZoneException {

    /**
     * Creates an exception for a persistence failure.
     *
     * @param message description of the persistence error
     * @param cause original exception that caused the failure
     */
    public PersistenceException(String message, Throwable cause) {
        super(message, "PERSISTENCE_ERROR");
        initCause(cause);
    }
}
