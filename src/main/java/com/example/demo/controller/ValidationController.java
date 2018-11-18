package com.example.demo.controller;

import com.example.demo.model.ParsedMrzInfo;
import com.example.demo.model.ParsingErrorCode;
import com.example.demo.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Kevin on 11/18/2018.
 */

@Controller
public class ValidationController {

    private final ValidationService validationService;

    @Autowired
    public ValidationController(ValidationService validationService) {
        this.validationService = validationService;
    }

    @ResponseBody
    @RequestMapping(value = "/validateCode", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> validateCode(@RequestParam("mrzCode") String mrzCode) {
        try{
            ParsedMrzInfo parsedMrzInfo = validationService.validateCode(mrzCode);
            return new ResponseEntity<>(parsedMrzInfo, HttpStatus.OK);
        } catch (Exception e) {
            ParsingErrorCode parsingErrorCode = new ParsingErrorCode("MRZ code could not be parsed correctly. " +
                    "Please enter or modify manually.", mrzCode, null, e);
            return new ResponseEntity<>(parsingErrorCode, HttpStatus.BAD_REQUEST);
        }
    }
}
