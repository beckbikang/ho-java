package cn.beckbi.config;

import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: ho-java
 * @description:
 * @author: bikang
 * @create: 2022-10-16 20:52
 */
@Configuration
public class KafkaConfig {

    @Value("${kafka.test.producer.bootstrap-servers}")
    private String  produceBootstrapServers;

    @Value("${kafka.test.producer.retries}")
    private int  produceRetries;

    @Value("${kafka.test.producer.acks}")
    private String produceAck;

    @Value("${kafka.test.producer.buffer-memory}")
    private long  produceBufferMemory;

    @Value("${kafka.test.producer.compression-type}")
    private String  produceCompressionType;

    @Value("${kafka.test.producer.batch-size}")
    private int produceBatchSize;

    @Value("${kafka.test.producer.properties.linger.ms}")
    private int produceLingerMs;

    @Value("${kafka.test.producer.keySerializer}")
    private String  produceKeySerializer;

    @Value("${kafka.test.producer.valueSerializer}")
    private String produceValueSerializer;

    @Value("${kafka.test.properties.jaas.enabled:false}")
    private boolean jaasEnabled;

    @Value("${kafka.test.properties.security.protocol:}")
    private String securityProtocol;

    @Value("${kafka.test.properties.sasl.mechanism:}")
    private String saslMechanism;

    @Value("${kafka.test.properties.sasl.jaas.config:}")
    private String jaasConfig;


    @Bean("producerFactory")
    public ProducerFactory<String, String> getProducerFactory(){
        Map<String, Object> producerMap = new HashMap<>(30);
        producerMap.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, produceKeySerializer);
        producerMap.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, produceValueSerializer);
        producerMap.put(ProducerConfig.BUFFER_MEMORY_CONFIG, produceBufferMemory);
        producerMap.put(ProducerConfig.COMPRESSION_TYPE_CONFIG, produceCompressionType);
        producerMap.put(ProducerConfig.BATCH_SIZE_CONFIG, produceBatchSize);
        producerMap.put(ProducerConfig.LINGER_MS_CONFIG, produceLingerMs);
        producerMap.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, produceBootstrapServers);
        producerMap.put(ProducerConfig.RETRIES_CONFIG, produceRetries);
        producerMap.put(ProducerConfig.ACKS_CONFIG, produceAck);
        if (jaasEnabled) {
            producerMap.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, securityProtocol);
            producerMap.put(SaslConfigs.SASL_MECHANISM, saslMechanism);
            producerMap.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        }
        return new DefaultKafkaProducerFactory<>(producerMap);
    }

    @Bean("testKafkaTemplate")
    public KafkaTemplate<String, String> tsidFrequencyKafkaTemplate(){
        return new KafkaTemplate<>(getProducerFactory());
    }
}
