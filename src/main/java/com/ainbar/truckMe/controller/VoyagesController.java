package com.ainbar.truckMe.controller;


import com.ainbar.truckMe.entities.Voyage;
import com.ainbar.truckMe.service.BatchService;
import com.ainbar.truckMe.service.VoyageService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/voyages")
@CrossOrigin(maxAge = 3600)
@Slf4j
public class VoyagesController {

    private JavaMailSender sender;
    private BatchService batchService;
    private VoyageService voyageService;

    public VoyagesController(BatchService batchService, VoyageService voyageService, JavaMailSender sender) {
        this.batchService = batchService;
        this.voyageService = voyageService;
        this.sender = sender;
    }

    @GetMapping("/email")
    @ResponseBody
    String home() {
        try {
            sendEmail();
            return "Email Sent!";
        } catch (Exception ex) {
            return "Error in sending email: " + ex;
        }
    }

    private void sendEmail() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo("kadar.hamza@gmail.com");
        helper.setText("How are you?");
        helper.setSubject("Hi");

        sender.send(message);
    }




    @GetMapping("/{idCamion}")
    public List<Voyage> getVoyagesByIdDevice(@PathVariable Long idCamion) {
        return voyageService.getVoyagesByIdCamion(idCamion);
    }

    @GetMapping("/{idCamion}/{date}")
    public List<Voyage> getVoyagesByIdDeviceAndDate(@PathVariable Long idCamion, @PathVariable String date) {
        return voyageService.getVoyagesByDevicetime(idCamion, date);
    }

    @GetMapping(value = "/{idCamion}/{date}/export", produces = "text/csv")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> exportVoyagesByIdDeviceAndDate(@PathVariable Long idCamion, @PathVariable String date, HttpServletRequest request) throws Exception {
        String filename = "voyages_" + idCamion + "_" + date + ".csv";


        List<Voyage> list = voyageService.getVoyagesByDevicetime(idCamion, date);
        if (list != null && !list.isEmpty()) {
            Writer writer = new FileWriter("/Users/mac/Documents/PROJECTS/truckMe/src/main/resources/" + filename);
            StatefulBeanToCsv<Voyage> writers = new StatefulBeanToCsvBuilder<Voyage>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(';')
                    .withOrderedResults(false)
                    .build();
            writers.write(list);
            writer.close();
            Resource resource = new ClassPathResource("/" + filename);
            String contentType = null;
            try {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            } catch (IOException ex) {
                log.info("Could not determine file type.");
            }
            // Fallback to the default content type if type could not be determined
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            System.out.println(resource.getFilename());
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } else return ResponseEntity.notFound().build();

    }

    @GetMapping("/all")
    public List<Voyage> getVoyages() {
        return batchService.getVoyages();
    }

    @GetMapping("/all/export")
    public void exportAllVoyages(HttpServletResponse response) throws Exception {
        String filename = "voyages_" + LocalDate.now() + ".csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Voyage> writer = new StatefulBeanToCsvBuilder<Voyage>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build();

        writer.write(batchService.getVoyages());
    }

    @GetMapping("/batch")
    public List<Voyage> calculVoyages() {
        return batchService.calculVoyages();
    }

}
