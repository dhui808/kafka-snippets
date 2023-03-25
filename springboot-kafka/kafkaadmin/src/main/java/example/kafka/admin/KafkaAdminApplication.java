package example.kafka.admin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;

public class KafkaAdminApplication {

    public static void main(String[] args) throws Exception {

        Admin admin = Admin.create(
                Map.of("bootstrap.servers", "localhost:9092")
        );

        printAllTopics(admin);
        
        NewTopic newTopic = new NewTopic("random-number", 2, (short)1);
        admin.createTopics(List.of(newTopic))
                .all()
                .get();
        printAllTopics(admin);
    }

    static void printAllTopics(Admin client) throws Exception {
        var topics = client.listTopics().names().get();
        System.out.println("Topics in the cluster:");
        topics.forEach(System.out::println);
        System.out.println();
    }
}
