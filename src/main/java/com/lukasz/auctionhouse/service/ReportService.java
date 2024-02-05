package com.lukasz.auctionhouse.service;

import com.lukasz.auctionhouse.domain.Report;
import com.lukasz.auctionhouse.domain.ReportRequest;
import com.lukasz.auctionhouse.domain.User;
import com.lukasz.auctionhouse.exception.UserNotFoundException;
import com.lukasz.auctionhouse.repositories.ReportRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.Date;
import java.util.Optional;

@Service
@ConditionalOnProperty(name = "service.bus", havingValue = "true")
public class ReportService {
    private static final String REQUEST_QUEUE = "reportrequest";
    private static final String RESPONSE_QUEUE = "reportresponse";
    private BidService bidService;
    private UserService userService;
    private ReportRepository reportRepository;
    private JmsTemplate jmsTemplate;

    public ReportService (BidService bidService,
                          UserService userService,
                          ReportRepository reportRepository,
                          JmsTemplate jmsTemplate){
        this.bidService = bidService;
        this.userService = userService;
        this.reportRepository = reportRepository;
        this.jmsTemplate = jmsTemplate;
    }

    public List<Report> getAllReports(){
        return reportRepository.findAll();
    }

    private void generateReport(String requestedBy) throws InterruptedException {
        Optional<User> userOptional = userService.getByUsername(requestedBy);

        if(userOptional.isEmpty()){
            throw new UserNotFoundException(String.format("User with name %s not found", requestedBy));
        }

        User user = userOptional.get();
        Report report = new Report();
        report.setBidsPlaced(bidService.getHistoricCount());
        report.setGeneratedDate(new Date());
        report.setGeneratedBy(user);
        Report savedReport =  reportRepository.save(report);

        jmsTemplate.convertAndSend(RESPONSE_QUEUE, user.getEmail());
    }

    @JmsListener(destination = REQUEST_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void recieveRequest(ReportRequest reportRequest){
        try {
            generateReport(reportRequest.getRequestedBy());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
