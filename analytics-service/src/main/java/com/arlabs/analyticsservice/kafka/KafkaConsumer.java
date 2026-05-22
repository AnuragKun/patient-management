package com.arlabs.analyticsservice.kafka;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import patient.events.PatientEvent;

import java.math.BigDecimal;

@Slf4j
@Service
public class KafkaConsumer {

    private BigDecimal totalRevenue = BigDecimal.ZERO;

    @KafkaListener(topics = "billing-events", groupId = "analytics-service")
    public void consumeBillingEvent(String eventJson) {
        try {

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode = mapper.readTree(eventJson);

            BigDecimal amount = new BigDecimal(jsonNode.get("amount").asText());
            String type = jsonNode.get("type").asText();

            if ("PAYMENT".equals(type)) {
                totalRevenue = totalRevenue.add(amount);
                log.info("New Payment Received: ₹{}. Total Hospital Revenue: ₹{}.", amount, totalRevenue);
            } else if ("CHARGE".equals(type)) {
                log.info("New Charge Added: ₹{}. Waiting for patient to pay.", amount);
            }

        } catch (Exception e) {
            log.error("Error processing billing event", e);
        }
    }

//    @KafkaListener(topics = "patient", groupId = "analytics-service")
//    public void consumerEvent(byte[] event) {
//        try {
//            PatientEvent patientEvent = PatientEvent.parseFrom(event);
//            // ... perform any business related to analytics here
//            log.info("Received Patient Event: [Patient ID: {}, Patient Name: {}, Patient Email: {}]", patientEvent.getPatientId(), patientEvent.getName(), patientEvent.getEmail());
//        } catch (InvalidProtocolBufferException e) {
//            log.error("Error deserializing event {}", e.getMessage());
//        }
//  }
}
