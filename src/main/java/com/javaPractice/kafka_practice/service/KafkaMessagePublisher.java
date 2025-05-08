package com.javaPractice.kafka_practice.service;

import com.javaPractice.kafka_practice.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class KafkaMessagePublisher {

    @Autowired
    private KafkaTemplate<String,Object> template;

    public void sendMessageToTopic(String message){
        CompletableFuture<SendResult<String, Object>> future = template.send("KafkaPractice-3", String.valueOf(3), message);
        future.whenComplete((result,ex)->{
            if(ex==null){
                System.out.println("Sent Message=["+message+
                        "] with offset=["+result.getRecordMetadata().offset()+"]"+ result.getRecordMetadata().partition());
            }
            else{
                System.out.println("Unable to send message=["+
                        message+"] due to: "+ex.getMessage());
            }
        });
    }

    public void sendEventsToTopic(Customer customer){
        try{
            CompletableFuture<SendResult<String, Object>> future = template.send("JSONTopic1", customer);
            future.whenComplete((result,ex)->{
                if(ex==null){
                    System.out.println("Sent Message=["+customer.toString()+
                            "] with offset=["+result.getRecordMetadata().offset()+"]");
                }
                else{
                    System.out.println("Unable to send message=["+
                            customer.toString()+"] due to: "+ex.getMessage());
                }
            });
        } catch (Exception e) {
            System.out.println("ERROR: "+e.getMessage());
        }

    }
}
