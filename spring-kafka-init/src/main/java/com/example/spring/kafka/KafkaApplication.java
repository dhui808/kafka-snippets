package com.example.spring.kafka;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.PartitionOffset;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.support.SendResult;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;

@SpringBootApplication
public class KafkaApplication {

    public static void main(String[] args) throws Exception {

        ConfigurableApplicationContext context = SpringApplication.run(KafkaApplication.class, args);

        MessageProducer producer = context.getBean(MessageProducer.class);
        MessageListener listener = context.getBean(MessageListener.class);
        /*
         * Sending a Hello World message to topic 'mytopic'. 
         * Must be received by both listeners with group foo
         * and bar with containerFactory fooKafkaListenerContainerFactory
         * and barKafkaListenerContainerFactory respectively.
         * It will also be received by the listener with
         * headersKafkaListenerContainerFactory as container factory.
         */
        producer.sendMessage("Hello, World!");
        listener.latch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to a topic with 5 partitions,
         * each message to a different partition. But as per
         * listener configuration, only the messages from
         * partition 0 and 3 will be consumed.
         */
        for (int i = 0; i < 5; i++) {
            producer.sendMessageToPartition("Hello To Partitioned Topic!", i);
        }
        listener.partitionLatch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'filtered' topic. As per listener
         * configuration,  all messages with char sequence
         * 'World' will be discarded.
         */
        producer.sendMessageToFiltered("Hello Earth!");
        producer.sendMessageToFiltered("Hello World!");
        listener.filterLatch.await(10, TimeUnit.SECONDS);

        /*
         * Sending message to 'greeting' topic. This will send
         * and received a java object with the help of
         * greetingKafkaListenerContainerFactory.
         */
        producer.sendGreetingMessage(new Greeting("Greetings", "World!"));
        listener.greetingLatch.await(10, TimeUnit.SECONDS);

        context.close();
    }

    @Bean
    public MessageProducer messageProducer() {
        return new MessageProducer();
    }

    @Bean
    public MessageListener messageListener() {
        return new MessageListener();
    }

    public static class MessageProducer {

        @Autowired
        private KafkaTemplate<String, String> kafkaTemplate;

        @Autowired
        private KafkaTemplate<String, Greeting> greetingKafkaTemplate;

        @Value(value = "${message.topic.name}")
        private String topicName;

        @Value(value = "${partitioned.topic.name}")
        private String partitionedTopicName;

        @Value(value = "${filtered.topic.name}")
        private String filteredTopicName;

        @Value(value = "${greeting.topic.name}")
        private String greetingTopicName;

        public void sendMessage(String message) {

            CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);
            future.whenComplete((result, ex) -> {

                if (ex == null) {
                    System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata()
                        .offset() + "]");
                } else {
                    System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
                }
            });
        }

        public void sendMessageToPartition(String message, int partition) {
            kafkaTemplate.send(partitionedTopicName, partition, null, message);
        }

        public void sendMessageToFiltered(String message) {
            kafkaTemplate.send(filteredTopicName, message);
        }

        public void sendGreetingMessage(Greeting greeting) {
            greetingKafkaTemplate.send(greetingTopicName, greeting);
        }
    }

    public static class MessageListener {

        private CountDownLatch latch = new CountDownLatch(3);

        private CountDownLatch partitionLatch = new CountDownLatch(2);

        private CountDownLatch filterLatch = new CountDownLatch(2);

        private CountDownLatch greetingLatch = new CountDownLatch(1);

        //Receive message from offset 0 until n every time this application is run
        @KafkaListener(groupId = "foo", containerFactory = "fooKafkaListenerContainerFactory", topicPartitions =
	    { @TopicPartition(topic = "${message.topic.name}", partitions = { "0", "1" },
	         partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0"))
	    })
        public void listenGroupFoo( @Headers Map<String, String> header,
        		@Header(KafkaHeaders.OFFSET) long os,
        		//@Header(KafkaHeaders.PARTITION) String partition,
        		@Header(KafkaHeaders.RECEIVED_PARTITION) int receivedpartition,
        		//@Header(KafkaHeaders.TOPIC) String topic,
        		@Header(KafkaHeaders.GROUP_ID) String group,
        		String message) {
        	Object offset = header.get("kafka_offset");
        	Long l = (Long)offset;
            System.out.println("Received Message in groupId='" + group + "'," + message + "offset=" + os + ", receivedpartition=" + receivedpartition);
            latch.countDown();
        }
        
        //Does not work. Not receiving message
        @KafkaListener(groupId = "bar", containerFactory = "barKafkaListenerContainerFactory", topicPartitions =
	    { @TopicPartition(topic = "${message.topic.name}", partitions = { "0", "1" })
	    })
        public void listenGroupBar( @Headers Map<String, String> header,
        		@Header(KafkaHeaders.OFFSET) long os,
        		//@Header(KafkaHeaders.PARTITION) String partition,
        		@Header(KafkaHeaders.RECEIVED_PARTITION) int receivedpartition,
        		//@Header(KafkaHeaders.TOPIC) String topic,
        		@Header(KafkaHeaders.GROUP_ID) String group,
        		String message) {

            System.out.println("Received Message in groupId='" + group + "'," + message + "offset=" + os+ ", receivedpartition=" + receivedpartition);
            latch.countDown();
        }
        
        //Receive message from offset n, except the first message is not received.
        @KafkaListener(groupId = "laker", containerFactory = "lakerKafkaListenerContainerFactory", topics = "${message.topic.name}")
        public void listenGroupLaker( @Headers Map<String, String> header,
        		@Header(KafkaHeaders.OFFSET) long os,
        		//@Header(KafkaHeaders.PARTITION) String partition,
        		@Header(KafkaHeaders.RECEIVED_PARTITION) int receivedpartition,
        		@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        		//@Header(KafkaHeaders.TOPIC) String topic,
        		@Header(KafkaHeaders.GROUP_ID) String group,
        		String message) {

            System.out.println("Laker listener, Received Message in groupId='" + group + "'," + message + "offset=" + os+ ", receivedpartition=" + receivedpartition + ", received_topic=" + topic);
            latch.countDown();
        }
        
        //Same group "laker", same partition and same topic, only one of the two listeners receives a message.
        //groupId annotation is redundant.
        @KafkaListener(containerFactory = "lakerKafkaListenerContainerFactory", topics = "${message.topic.name}")
        public void listenGroupLaker1( @Headers Map<String, String> header,
        		@Header(KafkaHeaders.OFFSET) long os,
        		//@Header(KafkaHeaders.PARTITION) String partition,
        		@Header(KafkaHeaders.RECEIVED_PARTITION) int receivedpartition,
        		@Header(KafkaHeaders.RECEIVED_TOPIC) String topic,
        		//@Header(KafkaHeaders.TOPIC) String topic,
        		@Header(KafkaHeaders.GROUP_ID) String group,
        		String message) {

            System.out.println("Laker1 listener, Received Message in groupId='" + group + "'," + message + "offset=" + os+ ", receivedpartition=" + receivedpartition + ", received_topic=" + topic);
            latch.countDown();
        }
        
        @KafkaListener(topics = "${message.topic.name}", containerFactory = "headersKafkaListenerContainerFactory")
        public void listenWithHeaders(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition) {
            System.out.println("Received Message: " + message + " from partition: " + partition);
            latch.countDown();
        }

        @KafkaListener(topicPartitions = @TopicPartition(topic = "${partitioned.topic.name}", partitions = { "0", "3" }), containerFactory = "partitionsKafkaListenerContainerFactory")
        public void listenToPartition(@Payload String message, @Header(KafkaHeaders.RECEIVED_PARTITION) int partition, @Header(KafkaHeaders.GROUP_ID) String group) {
            System.out.println("Received Message: " + message + " from partition: " + partition + ", groupId = " + group);
            this.partitionLatch.countDown();
        }

        @KafkaListener(topics = "${filtered.topic.name}", containerFactory = "filterKafkaListenerContainerFactory")
        public void listenWithFilter(String message) {
            System.out.println("Received Message in filtered listener: " + message);
            this.filterLatch.countDown();
        }

        @KafkaListener(topics = "${greeting.topic.name}", containerFactory = "greetingKafkaListenerContainerFactory")
        public void greetingListener(Greeting greeting) {
            System.out.println("Received greeting message: " + greeting);
            this.greetingLatch.countDown();
        }

    }

}
