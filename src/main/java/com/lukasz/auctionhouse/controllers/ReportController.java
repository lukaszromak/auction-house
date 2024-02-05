package com.lukasz.auctionhouse.controllers;

import com.lukasz.auctionhouse.domain.Report;
import com.lukasz.auctionhouse.domain.ReportRequest;
import com.lukasz.auctionhouse.service.ReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;


@RestController
@RequestMapping("/api/reports")
@ConditionalOnProperty(name = "service.bus", havingValue = "true")
public class ReportController {

    private ReportService reportService;
    private static final String REQUEST_QUEUE = "reportrequest";
    private static final String RESPONSE_QUEUE = "reportresponse";
    private JmsTemplate jmsTemplate;

    @Autowired
    public ReportController(JmsTemplate jmsTemplate, ReportService reportService){
        this.jmsTemplate = jmsTemplate;
        this.reportService = reportService;
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Report> getReports(){
        return reportService.getAllReports();
    }

    @Operation(security = {@SecurityRequirement(name = "basicAuth")})
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    public ResponseEntity generateReport() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        jmsTemplate.convertAndSend(REQUEST_QUEUE, new ReportRequest(username));

        return ResponseEntity.ok("Started generating report.");
    }

}
