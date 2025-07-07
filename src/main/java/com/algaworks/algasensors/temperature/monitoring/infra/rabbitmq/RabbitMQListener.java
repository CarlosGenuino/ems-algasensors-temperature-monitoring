package com.algaworks.algasensors.temperature.monitoring.infra.rabbitmq;

import com.algaworks.algasensors.temperature.monitoring.api.model.TemperatureLogData;
import io.hypersistence.tsid.TSID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Map;

import static com.algaworks.algasensors.temperature.monitoring.infra.rabbitmq.RabbitMQConfig.MONITORING_PROCESS_QUEUE_NAME;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQListener {

    @RabbitListener(queues = MONITORING_PROCESS_QUEUE_NAME)
    public void handler(
            @Payload TemperatureLogData temperatureLogData,
            @Headers Map<String, Object> headers
    ){
        TSID sensorId = temperatureLogData.getSensorId();
        Double temperature = temperatureLogData.getValue();
        log.info("Temperature updated. SensorId {}, Temperature {}", sensorId, temperature);
        log.info("Headers: {}", headers);
    }
}
