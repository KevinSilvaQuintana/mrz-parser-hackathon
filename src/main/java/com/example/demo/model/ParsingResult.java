package com.example.demo.model;

public class ParsingResult {

    public final float confidence;
    public final String mrzCode;

    public ParsingResult(float confidence, String mrzCode) {
        this.confidence = confidence;
        this.mrzCode = mrzCode;
    }
}
