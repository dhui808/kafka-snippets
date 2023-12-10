### Start Docker Kafka (using docker-compose1.yml)
	Start Docker Desktop
	
	docker-compose up -d

### Verify Kafka Cluster
	docker-compose ps
	
### Scripts that init Kafka
	  init-kafka:
		image: confluentinc/cp-kafka:7.4.1
		depends_on:
		  - kafka
		entrypoint: [ '/bin/sh', '-c' ]
		command: |
		  "
		  # blocks until kafka is reachable
		  kafka-topics --bootstrap-server kafka:29092 --list

		  echo -e 'Creating kafka topics'
		  kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists --topic mytopic --replication-factor 1 --partitions 1
		  kafka-topics --bootstrap-server kafka:29092 --create --if-not-exists --topic my-topic-2 --replication-factor 1 --partitions 1

		  echo -e 'Successfully created the following topics:'
		  kafka-topics --bootstrap-server kafka:29092 --list
		  "

### Start Kafka application

### Stop Docker Kafka
	docker-compose down
	
### Spring Bean RecordMessageConverter is needed
	multiTypeConverter()
	
	No need for multiTypeConsumerFactory and multiTypeKafkaListenerContainerFactory
	
### Start Kafka Docker (using docker-compose2.yml)
	Create Kafka Topic from Docker Command line instead of using init-kafka service in docker-compose
	docker-compose exec kafka kafka-topics --create --topic mytopic --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
	docker-compose exec kafka kafka-topics --create --topic partitioned --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
	docker-compose exec kafka kafka-topics --create --topic filtered --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
	docker-compose exec kafka kafka-topics --create --topic greeting --partitions 1 --replication-factor 1 --bootstrap-server kafka:9092
	
### Another way of creating Kafka Topics
	Using KafkaTopicConfig.java
	   