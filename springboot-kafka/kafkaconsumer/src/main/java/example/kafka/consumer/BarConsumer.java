package example.kafka.consumer;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata;
import org.springframework.stereotype.Component;

import example.kafka.common.Bar;

@Component
@KafkaListener(topics = "mytopic")
public class BarConsumer {

  @Value("${message.processing.time}")
  private long processingTime;

  @KafkaHandler
  public void consumerPartition2(Bar bar, ConsumerRecordMetadata meta) throws UnknownHostException, InterruptedException {
    String hostName = InetAddress.getLocalHost().getHostName();
    System.out.println(
        String.format(
            "%s consumed %s partition %d", hostName, bar, meta.partition()));
    Thread.sleep(processingTime);
  }
}
