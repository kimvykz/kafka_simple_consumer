package com.aisawan.consumer;

import com.aisawan.model.SatelliteEntity;
import com.aisawan.repository.SatelliteRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class SatelliteConsumer {
    private final SatelliteRepository satelliteRepository;

    @KafkaListener(topics = "#{'${spring.kafka.topics.satellite}'}", groupId = "group_id", containerFactory = "concurrentKafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, String> record) {
        ObjectMapper objectMapper = new ObjectMapper();
        log.info("--->>>Received value--->>>" + record.value());

        try {
            SatelliteEntity satellite = objectMapper.readValue(record.value(), SatelliteEntity.class);
            satelliteRepository.save(satellite);
        } catch (Exception e) {
            log.error(e.getMessage());
        }

    }
}
