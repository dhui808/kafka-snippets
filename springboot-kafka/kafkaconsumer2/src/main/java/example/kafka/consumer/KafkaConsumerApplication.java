package example.kafka.consumer;

import org.springframework.boot.*;
import org.springframework.boot.autoconfigure.*;

@SpringBootApplication
public class KafkaConsumerApplication {

  public static void main(String[] args) {
    SpringApplication.run(KafkaConsumerApplication.class, args);
  }
}
