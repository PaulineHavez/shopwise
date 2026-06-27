package com.shopwise.exception;

public class MerchantNotFoundException extends RuntimeException{
    public MerchantNotFoundException() {  super("Merchant not found'");}
}