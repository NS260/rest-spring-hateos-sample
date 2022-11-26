package com.mj.payroll;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super(String.format("Order with such id=%d was not found!",id));
    }
}
