// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.metricservice.service.impl;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.github.miha303.heatscrape.common.model.HeatingData;
import com.github.miha303.heatscrape.common.service.HeatingDataNotAvailableException;
import com.github.miha303.heatscrape.common.service.HeatingDataService;
import java.io.IOException;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class HeatingDataServiceImpl implements HeatingDataService {

    private static final Logger LOG = LoggerFactory.getLogger(HeatingDataServiceImpl.class);

    @Override
    @Cacheable(value = "heatingData", key = "1")
    public HeatingData getHeatingData() throws HeatingDataNotAvailableException {
        LOG.warn("No heating data from boiler is currently available");
        throw new HeatingDataNotAvailableException();
    }

    @Override
    @CachePut(value = "heatingData", key = "1")
    public HeatingData saveHeatingData(HeatingData heatingData) {
        LOG.debug("Saving new heating data obtained from boiler interface");
        return heatingData;
    }

    @Override
    public String transformToPrometheusMetrics(HeatingData heatingData) {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        SimpleModule module = new SimpleModule();
        module.addSerializer(
                boolean.class,
                new JsonSerializer<>() {
                    @Override
                    public void serialize(
                            Boolean value, JsonGenerator gen, SerializerProvider serializers)
                            throws IOException {
                        gen.writeNumber(value ? 1 : 0);
                    }
                });
        objectMapper.registerModule(module);
        Map<String, Object> map = objectMapper.convertValue(heatingData, new TypeReference<>() {});

        StringBuilder builder = new StringBuilder();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(entry.getKey()).append(" ").append(entry.getValue()).append("\n");
        }
        return builder.toString();
    }
}
