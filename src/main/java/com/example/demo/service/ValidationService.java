package com.example.demo.service;

import com.example.demo.model.ParsedMrzInfo;
import com.innovatrics.mrz.MrzParser;
import com.innovatrics.mrz.MrzRange;
import com.innovatrics.mrz.MrzRecord;
import org.springframework.stereotype.Service;

/**
 * Created by Kevin on 11/17/2018.
 */
@Service
public class ValidationService {

    public ParsedMrzInfo validateCode(String mrzCode) {
        final MrzRecord record = MrzParser.parse(mrzCode);
        ParsedMrzInfo parsedMrzInfo = new ParsedMrzInfo(
                mrzCode,
                record.code.name(),
                record.issuingCountry,
                record.surname,
                record.givenNames,
                record.documentNumber,
                record.nationality,
                record.dateOfBirth.toString(),
                record.sex.name(),
                record.expirationDate.toString(),
                getPersonalNumber(mrzCode));


//        String strYear = "20" +  String.format("%02d", year);
//        String strMonth =  String.format("%02d", month);
//        String strDay = String.format("%02d", day);
//        String strDate = strYear + "-" + strMonth + "-" + strDay;
//        return String.format("%02d", year) + "-" + String.format("%02d", month) + "-" + String.format("%02d",day) ;

        System.out.println("PARSED RESULT!!!");
        System.out.println(parsedMrzInfo.toString());
        return parsedMrzInfo;
    }

    private String getPersonalNumber(String mrzCode) {
        MrzParser parser = new MrzParser(mrzCode);
        return parser.parseString(new MrzRange(28, 42, 1));
    }

}
