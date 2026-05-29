package com.shopwise.exception;

public class MerchantNotFoundException extends RuntimeException{

    //évite d'écrire des try catch dans les controllers et services
    public MerchantNotFoundException() {  super("Merchant not found'");}
}