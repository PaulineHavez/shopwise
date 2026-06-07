package com.shopwise.exception;

public class AnavailableServiceException extends RuntimeException{
    public AnavailableServiceException() {  super("Anavailable service at this moment'");}
}