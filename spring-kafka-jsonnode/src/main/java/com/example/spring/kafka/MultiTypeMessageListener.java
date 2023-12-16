package com.example.spring.kafka;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;

import com.fasterxml.jackson.databind.JsonNode;

public class MultiTypeMessageListener  {
    private final String id;

    private final String topic;

    public String getId() {
        return this.id;
    }

    public String getTopic() {
        return this.topic;
    }
	public MultiTypeMessageListener(String id, String topic) {
		this.id = id;
        this.topic = topic;
	}
	
	@KafkaListener(containerFactory = "greetingKafkaListenerContainerFactory", topicPartitions =
	{ @TopicPartition(topic = "#{__listener.topic}", partitions = { "0", "1" },
	     partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0"))
	})
	public void listenGroupGreeting( @Headers Map<String, String> header,
    		@Header(KafkaHeaders.OFFSET) long os,
    		@Header(KafkaHeaders.RECEIVED_PARTITION) int receivedpartition,
    		@Header(KafkaHeaders.GROUP_ID) String group,
    		JsonNode message) {

        System.out.println("Received Json Message in groupId='" + group + "'," + message + ", offset=" + os + ", receivedpartition=" + receivedpartition);
        System.out.println("msg=" + message.get("msg") + ", name=" + message.get("name"));
    }
}
