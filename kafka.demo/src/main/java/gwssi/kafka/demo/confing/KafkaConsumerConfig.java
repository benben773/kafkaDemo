package gwssi.kafka.demo.confing;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.config.ContainerProperties;

import gwssi.kafka.demo.service.KafkaBatchConsumer;


@Configuration
@EnableKafka
public class KafkaConsumerConfig {

	@Value("${spring.kafka.consumer.group-id}")
	private String group_id;
    @Bean
    public ConcurrentMessageListenerContainer<String, String> kafkaMessageListenerContainer(Environment environment) {
        return kafkaMessageListenerContainer(group_id, true, environment);
    }


    private ConcurrentMessageListenerContainer<String, String> kafkaMessageListenerContainer(String consumerGroupId, boolean batch, Environment environment) {
        Map<String, Object> kafkaProps = new HashMap<>();
        kafkaProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
	    System.setProperty("java.security.auth.login.config", "C:/kafka_client_jaas.conf"); 
	    kafkaProps.put("security.protocol", "SASL_PLAINTEXT");  
	    kafkaProps.put("sasl.mechanism", "PLAIN"); 
        kafkaProps.putAll(getKafkaCommonProperties(environment));

        ConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(kafkaProps, new StringDeserializer(), new StringDeserializer());
        ContainerProperties containerProperties = new ContainerProperties("t1234");
        containerProperties.setPollTimeout(3000);
        containerProperties.setMessageListener(new KafkaBatchConsumer(consumerGroupId));
        ConcurrentMessageListenerContainer<String, String> container = new ConcurrentMessageListenerContainer<>(consumerFactory, containerProperties);
        container.setConcurrency(3);
        return container;
    }

    protected Map<String, Object> getKafkaCommonProperties(Environment environment) {
        List<String> propKeys = Arrays.asList(
                CommonClientConfigs.BOOTSTRAP_SERVERS_CONFIG);
        return propKeys.stream().collect(Collectors.toMap(e -> e, e -> environment.getProperty("kafka." + e)));
    }
}
