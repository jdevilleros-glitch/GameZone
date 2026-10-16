/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.exceptions;

/**
 *
 * @author USUARIO
 *
 * Base exception for application-specific errors in GameZone.
 */
public abstract class GameZoneException extends RuntimeException {

    private final String errorCode;

    /**
     * Creates an exception with a descriptive message.
     *
     * @param message description of the error
     */
    public GameZoneException(String message) {
        super(message);
        this.errorCode = null;
    }

    /**
     * Creates an exception with a message and error code.
     *
     * @param message description of the error
     * @param errorCode identifier of the error category
     */
    public GameZoneException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    /**
     * Returns the error code.
     *
     * @return error code
     */
    public String getErrorCode() {
        return errorCode;
    }
}
