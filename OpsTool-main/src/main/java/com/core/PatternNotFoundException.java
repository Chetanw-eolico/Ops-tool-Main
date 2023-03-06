package com.core;


public class PatternNotFoundException extends Exception {


    private static final long serialVersionUID = 1L;

	public PatternNotFoundException(Throwable thrwbl) {
        super(thrwbl);
    }

    public PatternNotFoundException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public PatternNotFoundException(String string) {
        super(string);
    }

    public PatternNotFoundException() {
    }
    
}