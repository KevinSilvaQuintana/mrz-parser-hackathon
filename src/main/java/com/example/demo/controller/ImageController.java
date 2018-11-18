package com.example.demo.controller;

import com.example.demo.model.ParsedMrzInfo;
import com.example.demo.model.ParsingErrorCode;
import com.example.demo.model.ParsingResult;
import com.example.demo.service.ImageParsingService;
import com.example.demo.service.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController
public class ImageController {

    private final ValidationService validationService;
    private final ImageParsingService imageParsingService;

    @Autowired
    public ImageController(ValidationService validationService, ImageParsingService imageParsingService) {
        this.validationService = validationService;
        this.imageParsingService = imageParsingService;
    }

    @ResponseBody
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity uploadImage(@RequestBody String imageAsString) throws IOException {
        ParsingResult parsingResult = imageParsingService.parseImageFromBase64String(imageAsString);
        try{
            ParsedMrzInfo parsedMrzInfo = validationService.validateCode(parsingResult.mrzCode);
            parsedMrzInfo.setConfidence(parsingResult.confidence);
            return ResponseEntity.ok(parsedMrzInfo);
        } catch (Exception e) {
            ParsingErrorCode parsingErrorCode = new ParsingErrorCode("MRZ code could not be parsed correctly. " +
                    "Please enter or modify manually.", parsingResult.mrzCode, parsingResult.confidence, e);
            return new ResponseEntity(parsingErrorCode, HttpStatus.BAD_REQUEST);
        }
    }
}
