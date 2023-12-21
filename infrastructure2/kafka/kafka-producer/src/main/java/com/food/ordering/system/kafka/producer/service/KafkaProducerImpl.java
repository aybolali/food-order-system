package com.food.ordering.system.kafka.producer.service;

import com.food.ordering.system.kafka.producer.exception.KafkaProducerException;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.io.Serializable;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

@Component
@Slf4j
public class KafkaProducerImpl<K extends Serializable, V extends SpecificRecordBase> implements KafkaProducer<K, V>{
    private final KafkaTemplate<K, V> kafkaTemplate;

    public KafkaProducerImpl(KafkaTemplate<K, V> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String topicName, K key, V message, BiConsumer<SendResult<K, V>, Throwable> callback) {
        log.info("sending message={} to topic {}", message, topicName);
        try {
            CompletableFuture<SendResult<K, V>> kafkaResultFuture = kafkaTemplate.send(topicName, key, message);
            kafkaResultFuture.whenComplete(callback);
        } catch (KafkaException e){
            log.error("error on kafka producer with key: {}, message: {} and exception: {}", key, message, e.getMessage());
            throw new KafkaProducerException("error with key: " + key + " and message " + message);
        }
    }

    @PreDestroy
    public void close(){
        if(kafkaTemplate != null){
            log.info("closing kafka producer!");
            kafkaTemplate.destroy();
        }
    }
}
