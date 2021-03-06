package test.service.Users;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;



@Configuration
public class KafkaProducerFactory {
	
	@Value("${bootstrap-servers}")
	private String bootStrapServers; 
	
	@Bean
	public Map<String,Object> producerConfig()
	{
		Map<String, Object> configProps = new HashMap<String, Object>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		return configProps;
	}
	
	@Bean
	public ProducerFactory<String,String> producerFactory()
	{
		return new DefaultKafkaProducerFactory<String, String>(producerConfig());
	}
	
	@Bean
	public KafkaTemplate<String,String> kafkaTemplate()
	{
		return new KafkaTemplate<String, String>(producerFactory());
	}

}
