package com.example.spring.kafka;

import java.util.Map;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MultiTypeMessageListener  {

	private ObjectMapper objectMapper = new ObjectMapper();
	
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
	
	// groupdId is needed.
	@KafkaListener(groupId = "#{__listener.id}", topicPartitions =
	{ @TopicPartition(topic = "#{__listener.topic}", partitions = { "0", "1" },
	     partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0"))
	})
	public void listenGroupGreeting( @Headers Map<String, String> header,
    		@Header(KafkaHeaders.OFFSET) long os,
    		@Header(KafkaHeaders.RECEIVED_PARTITION) int receivedpartition,
    		@Header(KafkaHeaders.GROUP_ID) String group,
    		String message) {

        System.out.println("Received Json Message in groupId='" + group + "'," + message + ", offset=" + os + ", receivedpartition=" + receivedpartition);
        JsonNode node = null;
		try {
			node = objectMapper.readValue(message, JsonNode.class);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("msg=" + node.get("msg") + ", name=" + node.get("name"));
    }
}
