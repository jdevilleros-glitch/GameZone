/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.misco.gamezone.exceptions;

/**
 *
 * @author USUARIO
 * 
 * Represents an error caused by a violation of a business rule.
 */
public class BusinessRuleException extends GameZoneException {

    /**
     * Creates an exception for a violated business rule.
     *
     * @param message description of the violated rule
     */
    public BusinessRuleException(String message) {
        super(message, "BUSINESS_RULE_ERROR");
    }
}
