package com.example.demo.service;

import com.example.demo.model.ParsingResult;
import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.util.ArrayList;
import java.util.List;


//TODO: apply filter to image.
//TODO: if confidence is below threshold, highlight it in red.
@Service
public class ImageParsingService {

    private static final String IMAGES_DIR = System.getProperty("user.home") + File.separator + "Documents" + File.separator + "hackathonImages";
    public static final ParsingResult BAD_PARSING_RESULT = new ParsingResult(0, null);

    public ParsingResult parseImageFromBase64String(String imageAsBase64) throws IOException {
        String[] strings = imageAsBase64.split(",");
        String header = strings[0];
        File imagefile = new File(getImagesDir(IMAGES_DIR), "Image_" + DateTime.now().getMillis() + "." + getFileExtension(header));

        //convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(imagefile))) {
            outputStream.write(data);
        }
        String successMessage = "Created file: " + imagefile.toString();
        System.out.println(successMessage);

        return callGoogleCloudImageParsing(imagefile);
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

    public ParsingResult callGoogleCloudImageParsing(File file) throws IOException {
        List<AnnotateImageRequest> requests = new ArrayList<>();

        ByteString imgBytes = ByteString.readFrom(new FileInputStream(file));

        Image img = Image.newBuilder().setContent(imgBytes).build();
        Feature feat = Feature.newBuilder().setType(Feature.Type.DOCUMENT_TEXT_DETECTION).build();
        ImageContext imageContext = ImageContext.newBuilder()
                .addLanguageHints("fr")
                .build();
        AnnotateImageRequest request =
                AnnotateImageRequest.newBuilder()
                        .addFeatures(feat)
                        .setImageContext(imageContext)
                        .setImage(img)
                        .build();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            BatchAnnotateImagesResponse response = client.batchAnnotateImages(requests);
            List<AnnotateImageResponse> responses = response.getResponsesList();

            for (AnnotateImageResponse res : responses) {
                if (res.hasError()) {
                    System.out.printf("Error: %s\n", res.getError().getMessage());
                    return null;
                }

                float totalConfidence = 0;

                // For full list of available annotations, see http://g.co/cloud/vision/docs
                TextAnnotation annotation = res.getFullTextAnnotation();
                for (Page page : annotation.getPagesList()) {
                    String pageText = "";
                    for (Block block : page.getBlocksList()) {
                        String blockText = "";
                        for (Paragraph para : block.getParagraphsList()) {
                            String paraText = "";
                            for (Word word : para.getWordsList()) {
                                String wordText = "";
                                for (Symbol character : word.getSymbolsList()) {
                                    wordText = wordText + character.getText();
                                    System.out.format(
                                            "Char: %s (confidence: %f)\n",
                                            character.getText(), character.getConfidence());
                                }
                                System.out.format("Word text: %s (confidence: %f)\n\n", wordText, word.getConfidence());
                                paraText = String.format("%s %s", paraText, wordText);
                            }
                            // Output Example using Paragraph:
                            System.out.println("\nParagraph: \n" + paraText);
                            System.out.format("Paragraph Confidence: %f\n", para.getConfidence());
                            blockText = blockText + paraText;
                            totalConfidence = para.getConfidence();
                        }
                        pageText = pageText + blockText;
                    }
                }
                System.out.println("\nComplete annotation:");
                String parsedText = annotation.getText();
                System.out.println("BEFORE: ");
                System.out.println(parsedText);
                String[] split = parsedText.split("\n");
                // wrong MRZ
                if(split.length != 2) {
                    return BAD_PARSING_RESULT;
                }
                String firstPart = fixSpacingIssues(0, split);
                String secondPart = fixSpacingIssues(1, split);
                secondPart = secondPart.replace("O", "0");
                String fixedMrz = firstPart + "\n" + secondPart;
                System.out.println("AFTER: ");
                System.out.println(fixedMrz);
                return new ParsingResult(totalConfidence, fixedMrz);
            }
        }
        return BAD_PARSING_RESULT;
    }

    private String fixSpacingIssues(int i, String[] split) {
        return (split[i].length() <= 44) ? split[i].replace(" ", "<") : split[i].replace(" ", "");
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
}