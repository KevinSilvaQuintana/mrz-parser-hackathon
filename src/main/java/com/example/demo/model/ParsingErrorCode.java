package com.example.demo.model;

public class ParsingErrorCode {

    public final String error;
    public final String mrzCode;
    public final Float confidence;
    public final String exception;

    public ParsingErrorCode(String error, String mrzCode, Float confidence, Exception exception) {
        this.error = error;
        this.mrzCode = mrzCode;
        this.confidence = confidence;
        this.exception = exception.getMessage();
    }

    @Override
    public String toString() {
        return "ParsingErrorCode{" +
                "error='" + error + '\'' +
                ", mrzCode='" + mrzCode + '\'' +
                ", exception=" + exception +
                '}';
    }
}
