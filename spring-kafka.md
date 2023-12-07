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
