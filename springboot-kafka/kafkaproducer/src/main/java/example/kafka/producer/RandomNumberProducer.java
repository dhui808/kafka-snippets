package example.kafka.producer;

import org.springframework.beans.factory.annotation.*;
import org.springframework.kafka.core.*;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.*;

import example.kafka.common.Bar;

import java.net.*;
import java.util.concurrent.*;

@Component
public class RandomNumberProducer {

  private static final int MIN = 10;
  private static final int MAX = 100_000;
  private static final String topic = "random-number";
  private static final String mytopic = "mytopic";
  
  @Autowired private KafkaTemplate<String, Object> kafkaTemplate;

  @Scheduled(fixedRate = 1000)
  public void produce() throws UnknownHostException {
    int random = ThreadLocalRandom.current().nextInt(MIN, MAX);
    int partition = random % 2 == 0? 0 : 1;
    partition = random % 3 == 0? 2 : partition;
    int id = 1;
    Bar bar = new Bar();
    bar.bar = "bar" + random;
    bar.name = "world";
    bar.id = id++;
    if (partition < 2 ) {
    	kafkaTemplate.send(topic, partition, "mykey-" + partition, String.valueOf(random));
    } else {
    	kafkaTemplate.send(mytopic, 0, "mykey-" + partition, bar);
    }
    // just for logging
    String hostName = InetAddress.getLocalHost().getHostName();
    System.out.println(String.format("%s produced %d partition %d", hostName, random, partition));
  }
}
