package example.kafka.consumer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

@Component
public class RandomNumberConsumer {

  @Value("${message.processing.time}")
  private long processingTime;

  @KafkaListener(topicPartitions =
      { @TopicPartition(topic = "random-number", partitions = { "0", "1" }),
        @TopicPartition(topic = "non-existent-topic2", partitions = "0")
      })
  public void consumer(String message, ConsumerRecordMetadata meta) throws UnknownHostException, InterruptedException {
    String hostName = InetAddress.getLocalHost().getHostName();
    System.out.println(
        String.format(
            "%s consumed %s partition %d", hostName, message, meta.partition()));
    Thread.sleep(processingTime);
  }
  
  @KafkaListener(topicPartitions =
      { @TopicPartition(topic = "random-number", partitions = { "2"})
      })
  public void consumerPartition2(String message, ConsumerRecordMetadata meta) throws UnknownHostException, InterruptedException {
    String hostName = InetAddress.getLocalHost().getHostName();
    System.out.println(
        String.format(
            "%s consumerPartition2 consumed %s partition %d", hostName, message, meta.partition()));
    Thread.sleep(processingTime);
  }
}
