package com.aisawan.consumer;

import com.aisawan.model.SatelliteEntity;
import com.aisawan.repository.SatelliteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SatelliteConsumer {
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${spring.kafka.topics.satellite}")
    private String topicName;

    private final SatelliteRepository satelliteRepository;

//    private void sendToDLQ(Object value) {
//        try {
//            kafkaTemplate.send("aisawan-topic-dlq", value);
//            log.warn("Message has been sent to DLQ: {}", value);
//        } catch (Exception e) {
//            log.error("Error sending to Dlq! Data has been lost! ", e);
//        }
//    }

    @KafkaListener(topics = "#{'${spring.kafka.topics.satellite}'}", groupId = "group_id", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("--->>>Received value--->>>" + record.value());

        SatelliteEntity satellite = new SatelliteEntity();

        try {
            satellite = objectMapper.readValue(record.value(), SatelliteEntity.class);
            satelliteRepository.save(satellite);
            System.out.println("--->>>Data saved--->>>");
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Error occurred:" + e.getMessage());
        }

    }
}
