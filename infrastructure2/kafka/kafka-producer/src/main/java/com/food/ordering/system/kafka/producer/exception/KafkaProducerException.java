package com.food.ordering.system.kafka.producer.exception;

public class KafkaProducerException extends RuntimeException{
    public KafkaProducerException(String ms){
        super(ms);
    }
}
