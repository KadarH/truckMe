package com.ainbar.truckMe.controller;

import com.ainbar.truckMe.entities.Record;
import com.ainbar.truckMe.service.BatchService;
import com.ainbar.truckMe.service.RecordService;
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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/records")
@Slf4j
public class RecordController {

    private BatchService batchService;
    private RecordService recordService;

    public RecordController(BatchService batchService, RecordService recordService) {
        this.batchService = batchService;
        this.recordService = recordService;
    }

    @GetMapping("/{id}")
    public List<Record> getVoyagesByIdDevice(@PathVariable Integer id) {
        return recordService.findAllByDeviceid(id);
    }

    @GetMapping("/{idCamion}/{date}")
    public List<Record> getRecordsByIdDeviceAndDate(@PathVariable Integer idCamion, @PathVariable String date) {
        return recordService.getRecordByDeviceidAndDevicetime(idCamion, date);
    }

    @GetMapping(value = "/{idCamion}/{date}/export", produces = "text/csv")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<Resource> exportRecordsByIdDeviceAndDate(@PathVariable Integer idCamion,
                                                                   @PathVariable String date,
                                                                   HttpServletRequest request) throws Exception {
        String filename = "records_" + idCamion + "_" + date + ".csv";


        List<Record> list = recordService.getRecordByDeviceidAndDevicetime(idCamion, date);
        if (list != null && !list.isEmpty()) {
            Writer writer = new FileWriter("/Users/mac/Documents/PROJECTS/truckMe/src/main/resources/" + filename);
            StatefulBeanToCsv<Record> writers = new StatefulBeanToCsvBuilder<Record>(writer)
                    .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                    .withSeparator(';')
                    .withOrderedResults(false)
                    .build();
            writers.write(list);
            Resource resource = new ClassPathResource("/" + filename);
            writer.close();
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
    public List<Record> getRecords() {
        return batchService.getRecords();
    }

    @GetMapping("/all/export")
    public void exportAllRecords(HttpServletResponse response) throws Exception {
        String filename = "records_" + LocalDate.now() + ".csv";

        response.setContentType("text/csv");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + filename + "\"");

        //create a csv writer
        StatefulBeanToCsv<Record> writer = new StatefulBeanToCsvBuilder<Record>(response.getWriter())
                .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
                .withSeparator(';')
                .withOrderedResults(false)
                .build();

        writer.write(batchService.getRecords());
    }

    @GetMapping(value = "/batch", produces = {"application/json"})
    public @ResponseBody
    List<Record> calculRecords() {
        return batchService.calculRecords();
    }
}
