package com.eventpro.PaymentService;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.support.serializer.JsonSerializer;

import com.eventpr.SalesService.dto.SaleDTO;

@SpringBootApplication
public class PaymentServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceApplication.class, args);
	}
	
//	Kafka Consumer ----------------------------------------------------------------------------------------
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String kafkaBootsrap;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	
    @Bean
    public ConsumerFactory<String, SaleDTO> ticketSaleCreatedCF() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, this.kafkaBootsrap);
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, this.groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        
        JsonDeserializer<SaleDTO> deserializer = new JsonDeserializer<>(SaleDTO.class);
        deserializer.addTrustedPackages("com.eventpr.SalesService.dto");
        
        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), deserializer);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SaleDTO> ticketSaleCreatedKafkaListener() {
        ConcurrentKafkaListenerContainerFactory<String, SaleDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(this.ticketSaleCreatedCF());
        return factory;
    }
    
//	Kafka Producer ----------------------------------------------------------------------------------------
	
	@Value("${spring.kafka.bootstrap-servers}")
	private String springKafkaBootstrapServers;
	
	@Bean
	public ProducerFactory<String, SaleDTO> paymentResultPF() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, this.springKafkaBootstrapServers);
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
		return new DefaultKafkaProducerFactory<>(configProps);
	}

	@Bean
	public KafkaTemplate<String, SaleDTO> paymentResultKT() {
		return new KafkaTemplate<String, SaleDTO>(this.paymentResultPF());
	}
}
