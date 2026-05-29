package com.shopwise.exception;

public class CustomerNotFoundException extends RuntimeException{

    //évite d'écrire des try catch dans les controllers et services
    public CustomerNotFoundException() {  super("Customer not found'");}
}