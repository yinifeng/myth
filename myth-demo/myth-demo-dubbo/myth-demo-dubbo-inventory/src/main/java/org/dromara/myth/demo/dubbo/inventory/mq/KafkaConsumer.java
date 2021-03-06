package org.dromara.myth.demo.dubbo.inventory.mq;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.dromara.myth.common.utils.LogUtil;
import org.dromara.myth.core.service.MythMqReceiveService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * KafkaConsumer.
 *
 * @author xiaoyu(Myth)
 */
@Component
@ConditionalOnProperty(prefix = "spring.kafka.consumer", name = "bootstrap-servers")
public class KafkaConsumer {

    /**
     * logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    private final MythMqReceiveService mythMqReceiveService;

    @Autowired
    public KafkaConsumer(MythMqReceiveService mythMqReceiveService) {
        this.mythMqReceiveService = mythMqReceiveService;
    }

    @KafkaListener(topics = {"inventory"})
    public void kafkaListener(ConsumerRecord<?, byte[]> record) {
        Optional<?> messages = Optional.ofNullable(record.value());
        if (messages.isPresent()) {
            byte[] msg = (byte[]) messages.get();
            LogUtil.debug(LOGGER, "接收到Myth分布式框架消息对象：{}", () -> msg);
            mythMqReceiveService.processMessage(msg);

        }
    }
}
