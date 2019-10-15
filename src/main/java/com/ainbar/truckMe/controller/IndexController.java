package com.ainbar.truckMe.controller;

import com.ainbar.truckMe.service.BatchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class IndexController {

    private BatchService batchService;

    public IndexController(BatchService batchService) {
        this.batchService = batchService;
    }

    @GetMapping("clean")
    public String getRecords() {
        batchService.clean();
        return "Tc positions Cleaned Successfully";
    }

}
