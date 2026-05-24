package com.shopwise.customer.exception;

public class CustomerAlreadyExistsException extends RuntimeException{

    //évite d'écrire des try catch dans les controllers et services
    public CustomerAlreadyExistsException(String dataName) {  super("A customer with this'" + dataName + "' already exists");}
}