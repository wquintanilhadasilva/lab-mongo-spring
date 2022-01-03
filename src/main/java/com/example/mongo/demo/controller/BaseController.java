package com.example.mongo.demo.controller;

import com.example.mongo.demo.docs.Customer;
import com.example.mongo.demo.docs.SiafNC;
import com.example.mongo.demo.model.APIResponse;
import com.example.mongo.demo.model.CustomerUpdateDto;
import com.example.mongo.demo.repositories.CustomerRepository;
import com.example.mongo.demo.repositories.SiafNCRepository;
import lombok.AllArgsConstructor;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripper;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(value = "/api/v1")
@AllArgsConstructor
public class BaseController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SiafNCRepository siafNCRepository;
	
	@GetMapping(value = "/base")
    public ResponseEntity<APIResponse> readBaseResponse () {
        return ResponseEntity.ok(new APIResponse("API Running"));
    }

    @GetMapping(value = "/appcheck")
    public ResponseEntity<APIResponse> checkApp () {
        return ResponseEntity.ok(new APIResponse("API Running Checked"));
    }

    @GetMapping(value = "/customers")
    public ResponseEntity<List<Customer>> customers () {
        return ResponseEntity.ok(customerRepository.findAll());
    }

    @GetMapping(value = "/siafs")
    public ResponseEntity<List<SiafNC>> siafs () {
        return ResponseEntity.ok(siafNCRepository.findAll());
    }

    @PostMapping(value = "/customers" )
    public ResponseEntity<Customer> siafs (@RequestBody CustomerUpdateDto data) {
        var doc = customerRepository.findById(data.getId());
        Customer cust;
        if(doc.isPresent()){
            cust = doc.get();
            cust.setLastName(data.getLastName());
            customerRepository.save(cust);
            return ResponseEntity.ok(customerRepository.save(cust));
        } else {
            return ResponseEntity.notFound().build();
        }
//
//        Query query = new Query(new Criteria("id").is(data.getId()));
//        Update update = new Update().set("lastName", data.getLastName());
//        customerRepository.updateFirst(query, update, COLLECTION);
//
//	    customerRepository.
//        return ResponseEntity.ok(siafNCRepository.findAll());
    }

    @PostMapping("/api/pdf/extractText")
    public @ResponseBody ResponseEntity<String>
    extractTextFromPDFFile(@RequestParam("file") MultipartFile file) {
//        try {
//            JSONObject obj = new JSONObject();
//            obj.put("fileName", file.getOriginalFilename());
//            obj.put("text", "TBD");
//
//            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }

        try {
            // Load file into PDFBox class
            PDDocument document = PDDocument.load(file.getBytes());
            PDFTextStripper stripper = new PDFTextStripper();
            String strippedText = stripper.getText(document);

            // Check text exists into the file
            if (strippedText.trim().isEmpty()){
                strippedText = extractTextFromScannedDocument(document);
            }

            JSONObject obj = new JSONObject();
            obj.put("fileName", file.getOriginalFilename());
            obj.put("text", strippedText.toString());

            return new ResponseEntity<String>(obj.toString(), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractTextFromScannedDocument(PDDocument document)
            throws IOException, TesseractException {

        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        _tesseract.setDatapath("/usr/share/tessdata/");
        _tesseract.setLanguage("ita"); // choose your language

        for (int page = 0; page < document.getNumberOfPages(); page++)
        {
            BufferedImage bim = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // Create a temp image file
            File temp = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bim, "png", temp);

            String result = _tesseract.doOCR(temp);
            out.append(result);

            // Delete temp file
            temp.delete();

        }

        return out.toString();

    }

}
