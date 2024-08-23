### Send and Consume Json String
	Topic is created when Producer publishes a message to a topic.
	
### Start Docker Kafka (using docker-compose.yml)
	Start Docker Desktop
	
	docker-compose up -d

### Verify Kafka Cluster
	docker-compose ps

### Start Kafka application

### Stop Docker Kafka
	docker-compose down

### Note ProducerConfig
```
06:56:02.572 [main] INFO  o.a.k.c.producer.ProducerConfig - ProducerConfig values: 
	acks = -1
	batch.size = 16384
	bootstrap.servers = [localhost:9092]
	buffer.memory = 33554432
	client.dns.lookup = use_all_dns_ips
	client.id = producer-1

ProducerConfig does not have allow.auto.create.topics

07:15:16.588 [main] INFO  o.a.k.clients.producer.KafkaProducer - [Producer clientId=producer-1] Instantiated an idempotent producer.
07:15:16.635 [main] INFO  o.a.kafka.common.utils.AppInfoParser - Kafka version: 3.3.2
07:15:16.638 [main] INFO  o.a.kafka.common.utils.AppInfoParser - Kafka commitId: b66af662e61082cb
07:15:16.638 [main] INFO  o.a.kafka.common.utils.AppInfoParser - Kafka startTimeMs: 1724411716634
07:15:16.893 [kafka-producer-network-thread | producer-1] WARN  o.apache.kafka.clients.NetworkClient - [Producer clientId=producer-1] Error while fetching metadata with correlation id 1 : {multitype=LEADER_NOT_AVAILABLE}
07:15:16.894 [kafka-producer-network-thread | producer-1] INFO  org.apache.kafka.clients.Metadata - [Producer clientId=producer-1] Cluster ID: COm-y85WSE6lgAWEv67sSA
07:15:16.895 [kafka-producer-network-thread | producer-1] INFO  o.a.k.c.p.i.TransactionManager - [Producer clientId=producer-1] ProducerId set to 1 with epoch 0
07:15:16.997 [kafka-producer-network-thread | producer-1] INFO  org.apache.kafka.clients.Metadata - [Producer clientId=producer-1] Resetting the last seen epoch of partition multitype-0 to 0 since the associated topicId changed from null to 1Aaxjg8DTpy4nDRhgV19mQ

```

### Note ConsumerConfig
```
06:56:03.259 [main] INFO  o.a.k.c.consumer.ConsumerConfig - ConsumerConfig values: 
	allow.auto.create.topics = true
	auto.commit.interval.ms = 5000
	auto.offset.reset = latest
	bootstrap.servers = [localhost:9092]
```

		   