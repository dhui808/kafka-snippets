package com.example.spring.kafka;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.kafka.clients.admin.AdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SpringBootApplication
public class KafkaJsonStringApplication {
	
    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(KafkaJsonStringApplication.class, args);
        
        String topic = "myjsonstringtopic";
        
        MessageProducer producer = context.getBean(MessageProducer.class, topic);
        producer.sendMessages();
        
        // Create message listener
        context.getBean(MultiTypeMessageListener.class, "greetingGrp", topic);
        
        //Deliberate delay to let listener consume produced message before main thread stops
        Thread.sleep(5000);
        
        // delete topic
        KafkaAdmin kafkaAdmin = context.getBean(KafkaAdmin.class);
        Map<String, Object> props = kafkaAdmin.getConfigurationProperties();
        AdminClient ac = AdminClient.create(props);
        List<String> list = new ArrayList<String>();
        list.add(topic);
        
        ac.deleteTopics(list);
        
        context.close();
    }

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    MultiTypeMessageListener multiTypeMessageListener(String id, String topic) {
        return new MultiTypeMessageListener(id, topic);
    }
    
    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public MessageProducer messageProducer(String topic) {
        return new MessageProducer(topic);
    }

    public static class MessageProducer {

        @Autowired
        private KafkaTemplate<String, String> greetingKafkaTemplate;
        
        private String topic;

        public MessageProducer(String topic) {
        	this.topic = topic;
        }
        
        public void sendMessages() {

        	String jsonFile = "/my_res.json";
        	InputStream inputStream = MessageProducer.class.getResourceAsStream(jsonFile);
        	Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        	String result = s.hasNext() ? s.next() : "";
        	
        	System.out.println("Producer sent to topic:" + topic);
        	
        	greetingKafkaTemplate.send(topic, result);
        	
        	System.out.println("Producer sent message:" + result);
        }

    }

}
