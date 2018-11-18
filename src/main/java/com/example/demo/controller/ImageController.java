package com.example.demo.controller;

import com.example.demo.service.ParsingService;
import com.example.demo.service.ValidationService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

@RestController
public class ImageController {

    private final ValidationService validationService;
    private final ParsingService parsingService;

    private static final String IMAGES_DIR = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "hackathonImages";

    @ResponseBody
    @RequestMapping(value = "/uploadImage", method = RequestMethod.POST, consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String uploadImage2(@RequestBody String imageAsString) throws IOException {
        String[] strings = imageAsString.split(",");
        String header = strings[0];
        File imagefile = new File(getImagesDir(IMAGES_DIR), "Image_" + DateTime.now().getMillis() + "." + getFileExtension(header));

        //convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imagefile))) {
            outputStream.write(data);
        }
        String successMessage = "Created file: " + imagefile.toString();
        System.out.println(successMessage);

        parsingService.parseFile(imagefile);

        return successMessage;
    }

    private String getFileExtension(String header) {
        String extension;
        //check image's extension
        switch (header) {
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default://should write cases for more images types
                extension = "jpg";
                break;
        }
        return extension;
    }


    public void test(String imageAsString) {

    }

    @Autowired
    public ImageController(ValidationService validationService, ParsingService parsingService) {
        this.validationService = validationService;
        this.parsingService = parsingService;
    }

    private File getImagesDir(String path) {
        File hackathonImagesDirectory = new File(path);
        if (hackathonImagesDirectory.exists()) {
            System.out.println(hackathonImagesDirectory + " already exists");
        } else if (hackathonImagesDirectory.mkdirs()) {
            System.out.println(hackathonImagesDirectory + " was created");
        } else {
            System.out.println(hackathonImagesDirectory + " was not created");
        }
        return hackathonImagesDirectory;
    }
}
