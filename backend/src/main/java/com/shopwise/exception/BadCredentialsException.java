package com.shopwise.exception;


public class BadCredentialsException extends RuntimeException{

    //évite d'écrire des try catch dans les controllers et services
    public BadCredentialsException() {  super("The email doesn't exist or the password is incorrect'");}
}

