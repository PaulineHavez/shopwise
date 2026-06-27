package com.shopwise.exception;

public class AppointmentNotFoundException extends RuntimeException{
    public AppointmentNotFoundException() {  super("Appointment not found'");}
}