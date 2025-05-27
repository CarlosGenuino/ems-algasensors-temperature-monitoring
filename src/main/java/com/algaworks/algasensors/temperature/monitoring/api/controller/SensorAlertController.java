package com.algaworks.algasensors.temperature.monitoring.api.controller;

import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertInput;
import com.algaworks.algasensors.temperature.monitoring.api.model.SensorAlertOutput;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorAlert;
import com.algaworks.algasensors.temperature.monitoring.domain.model.SensorId;
import com.algaworks.algasensors.temperature.monitoring.domain.repository.SensorAlertRepository;
import io.hypersistence.tsid.TSID;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/sensors/{sensorId}/alert")
public class SensorAlertController {

    private final SensorAlertRepository repository;

    @GetMapping
    public SensorAlertOutput findByAlertBySensorId(@PathVariable TSID sensorId){
        var entity = getEntityOrThrowError(sensorId);

        return SensorAlertOutput.builder()
                .id(entity.getSensorId().getValue())
                .minTemperature(entity.getMinTemperature())
                .maxTemperature(entity.getMaxTemperature())
                .build();
    }

    private SensorAlert getSensorAlertOrDefault(TSID sensorId) {
        return repository.findById(new SensorId(sensorId))
                .orElse(new SensorAlert(new SensorId(sensorId), 0D, 0D));
    }

    @PutMapping
    public SensorAlertOutput createAlert(@PathVariable TSID sensorId, @RequestBody SensorAlertInput input){
        var entity = getSensorAlertOrDefault(sensorId);
        entity.setMinTemperature(input.getMinTemperature());
        entity.setMaxTemperature(input.getMaxTemperature());
        repository.save(entity);

        return SensorAlertOutput.builder()
                .id(entity.getSensorId().getValue())
                .minTemperature(entity.getMinTemperature())
                .maxTemperature(entity.getMaxTemperature())
                .build();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAlert(@PathVariable TSID sensorId){
        var entity = getEntityOrThrowError(sensorId);
        repository.delete(entity);
    }

    private SensorAlert getEntityOrThrowError(TSID sensorId) {
        return repository.findById(new SensorId(sensorId))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
