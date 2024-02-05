package com.lukasz.auctionhouse.service;

import com.azure.communication.email.EmailClient;
import com.azure.communication.email.models.EmailMessage;
import com.azure.communication.email.models.EmailSendResult;
import com.azure.core.util.polling.PollResponse;
import com.azure.core.util.polling.SyncPoller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "email.service", havingValue = "true")
public class EmailService {
    @Value("${azure.email.domain}")
    private String domain;
    private static final String senderUsername = "DoNotReply";
    private static final String RESPONSE_QUEUE = "reportresponse";
    private EmailClient emailClient;

    @Autowired
    public EmailService(EmailClient emailClient){
        this.emailClient = emailClient;
    }

    public void sendEmail(String email){
        EmailMessage emailMessage = new EmailMessage()
                .setSenderAddress(String.format("%s@%s", senderUsername, domain))
                .setToRecipients(email)
                .setSubject("Auction house")
                .setBodyPlainText("Your report has been generated.");

        SyncPoller<EmailSendResult, EmailSendResult> poller = emailClient.beginSend(emailMessage);
        PollResponse<EmailSendResult> response = poller.waitForCompletion();
        System.out.println("Operation Id: " + response.getValue().getId());
    }

    @JmsListener(destination = RESPONSE_QUEUE, containerFactory = "jmsListenerContainerFactory")
    public void recieveRequest(String email){
        sendEmail(email);
    }

}
