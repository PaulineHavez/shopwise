package com.shopwise.exception;

public class CustomerAccountAlreadyExistsException extends RuntimeException{
    public CustomerAccountAlreadyExistsException() {  super("Customer account already exists'");}
}