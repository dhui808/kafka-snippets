### @KafkaListener vs @KafkaHandler
	When you use @KafkaListener at the class-level, you must specify @KafkaHandler at the method level.
	Need a @KafkaHandler method with a KafkaNull payload

### Spring @Header Map<String, String> header example
	kafka_offset=2, 
 	kafka_consumer=org.apache.kafka.clients.consumer.KafkaConsumer@6389602e, 
  	kafka_timestampType=CREATE_TIME, 
   	kafka_receivedPartitionId=0, 
	kafka_receivedTopic=mytopic, 
 	kafka_receivedTimestamp=1701950177628, 
  	kafka_groupId=foo

 	Issue: kafka_offset is Long instead of String! Why JVM allows this? (Spring is using bytecode 
  	manipulation to bypass static type check???)

### Reading the topic from the beginning each time the application starts
	reading-from-beginning-offset-0-of-topic-with-spring-kafka-spring-boot
 
	Starting with Spring Kafka 2.5.5, you can apply an initial offset to all assigned partitions:
 
  	@KafkaListener(id = "thing3", topicPartitions =
        { @TopicPartition(topic = "topic1", partitions = { "0", "1" },
             partitionOffsets = @PartitionOffset(partition = "*", initialOffset = "0"))
        })
	public void listen(ConsumerRecord<?, ?> record) {
	    ...
	}

 	Issue: if initialOffset is not specified, the customer does not receive the very first message!
	But if you specify initialOffset, every time the application starts, the consumer reads from
   	the beginning of the topic.
