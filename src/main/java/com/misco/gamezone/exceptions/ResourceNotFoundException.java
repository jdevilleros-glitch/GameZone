/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.exceptions;

/**
 *
 * @author USUARIO
 *
 * Represents an error when a requested resource cannot be found.
 */
public class ResourceNotFoundException extends GameZoneException {

    /**
     * Creates an exception for a missing resource.
     *
     * @param resourceType type of resource
     * @param resourceId identifier of the missing resource
     */
    public ResourceNotFoundException(String resourceType, String resourceId) {
        super(
                resourceType + " with ID " + resourceId + " was not found.",
                "RESOURCE_NOT_FOUND"
        );
    }
}
