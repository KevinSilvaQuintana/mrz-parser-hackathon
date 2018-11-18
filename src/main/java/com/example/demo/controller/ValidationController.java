package com.example.demo.controller;

import com.example.demo.model.ParsedMrzInfo;
import com.innovatrics.mrz.MrzParser;
import com.innovatrics.mrz.MrzRange;
import com.innovatrics.mrz.MrzRecord;
import com.innovatrics.mrz.records.MRP;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Kevin on 11/18/2018.
 */

@Controller
public class ValidationController {

    @RequestMapping(value = "/validateCode", method = RequestMethod.GET, produces = "application/json")
    public ResponseEntity<ParsedMrzInfo> validateCode(@RequestParam("mrzCode") String mrzCode) {
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

        System.out.println(parsedMrzInfo.toString());
        return ResponseEntity.ok(parsedMrzInfo);
    }

    private String getPersonalNumber(String mrzCode) {
        MrzParser parser = new MrzParser(mrzCode);
        return parser.parseString(new MrzRange(28, 42, 1));
    }
}
