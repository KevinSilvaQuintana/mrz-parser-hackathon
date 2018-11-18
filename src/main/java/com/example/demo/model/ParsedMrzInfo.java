package com.example.demo.model;

public class ParsedMrzInfo {

    // the first line of machine readable
    public final String mrzCode;
    public final String docType;  // P indicating a passport,  Position 1
    public final String issuingCountry;
    public final String familyName;
    public final String givenName;

    // the second line of machine readable
    public final String passportNumber;
    public final String nationality;
    public final String dateOfBirth;
    public final String gender;
    public final String expiryDate;
    public final String personalNumber;

    public ParsedMrzInfo(String mrzCode, String docType, String issuingCountry, String familyName, String givenName, String passportNumber, String nationality, String dateOfBirth, String gender, String expiryDate, String personalNumber) {
        this.mrzCode = mrzCode;
        this.docType = docType;
        this.issuingCountry = issuingCountry;
        this.familyName = familyName;
        this.givenName = givenName;
        this.passportNumber = passportNumber;
        this.nationality = nationality;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.expiryDate = expiryDate;
        this.personalNumber = personalNumber;
    }

    @Override
    public String toString() {
        return "ParsedMrzInfo{" +
                "mrzCode='" + mrzCode + '\'' +
                ", docType='" + docType + '\'' +
                ", issuingCountry='" + issuingCountry + '\'' +
                ", familyName='" + familyName + '\'' +
                ", givenName='" + givenName + '\'' +
                ", passportNumber='" + passportNumber + '\'' +
                ", nationality='" + nationality + '\'' +
                ", dateOfBirth='" + dateOfBirth + '\'' +
                ", gender='" + gender + '\'' +
                ", expiryDate='" + expiryDate + '\'' +
                ", personalNumber='" + personalNumber + '\'' +
                '}';
    }
}
