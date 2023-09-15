// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.dataproviderservice.service.impl;

import com.github.miha303.heatscrape.common.model.HeatingData;
import com.github.miha303.heatscrape.common.service.DataProviderService;
import com.github.miha303.heatscrape.dataproviderservice.Config;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Socket;
import java.util.Arrays;
import java.util.BitSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataProviderServiceImpl implements DataProviderService {

    private static final Logger LOG = LoggerFactory.getLogger(DataProviderServiceImpl.class);
    private static final String REMEHA_BOILER_DATA_COMMAND = "02FE010508020169AB03";
    private static final String REMEHA_ROOM_DATA_COMMAND = "02FE0205086601076B03";

    @Autowired private Config.ServiceConfig serviceConfig;

    @Override
    @SuppressWarnings("AddressSelection")
    public HeatingData pullData() {
        LOG.debug("Pulling data from boiler interface");
        HeatingData heatingData = null;
        try (Socket clientSocket = new Socket(serviceConfig.address(), serviceConfig.port())) {

            byte[] boilerData = executeCommand(REMEHA_BOILER_DATA_COMMAND, clientSocket);

            BigDecimal outFlowTemperature = Utils.decodeTemp(boilerData[0], boilerData[1]);
            BigDecimal returnFlowTemperature = Utils.decodeTemp(boilerData[2], boilerData[3]);
            int fanSpeed = Utils.b2i(boilerData[24], boilerData[25]);
            BigDecimal ionizationCurrent =
                    BigDecimal.valueOf(Utils.b2i(boilerData[26])).multiply(BigDecimal.valueOf(0.1));
            int power = Utils.b2i(boilerData[33]);
            BitSet bitSet = BitSet.valueOf(Arrays.copyOfRange(boilerData, 36, 37));
            boolean heatingRequired = bitSet.get(1);
            boolean dhwHeatingRequired = bitSet.get(7);
            int state = Utils.b2i(boilerData[40]);
            boolean flame = state == 2 || state == 3 || state == 4;

            byte[] roomData = executeCommand(REMEHA_ROOM_DATA_COMMAND, clientSocket);

            BigDecimal outsideTemperature = Utils.decodeTemp(roomData[0], roomData[1]);
            BigDecimal dwhTemperature = Utils.decodeTemp(roomData[2], roomData[3]);
            BigDecimal outflowTemperatureCircuitB = Utils.decodeTemp(roomData[6], roomData[7]);
            BigDecimal outflowTemperatureCircuitC = Utils.decodeTemp(roomData[8], roomData[9]);
            BigDecimal roomTemperatureB = Utils.decodeTemp(roomData[10], roomData[11]);
            BigDecimal roomTemperatureC = Utils.decodeTemp(roomData[14], roomData[15]);

            heatingData =
                    new HeatingData(
                            outFlowTemperature,
                            returnFlowTemperature,
                            fanSpeed,
                            ionizationCurrent,
                            power,
                            heatingRequired,
                            dhwHeatingRequired,
                            outsideTemperature,
                            dwhTemperature,
                            outflowTemperatureCircuitB,
                            outflowTemperatureCircuitC,
                            roomTemperatureB,
                            roomTemperatureC,
                            flame);
            LOG.debug("Pulling data from boiler interface finished");
        } catch (Exception e) {
            LOG.error("Error occurred while pulling data", e);
        }
        return heatingData;
    }

    private byte[] executeCommand(String command, Socket clientSocket) throws IOException {
        BufferedOutputStream out = new BufferedOutputStream(clientSocket.getOutputStream());
        out.write(Utils.hexStringToByteArray(command));
        out.flush();

        byte[] response = new byte[1024];
        int bytesRead =
                Utils.readInputStreamWithTimeout(clientSocket.getInputStream(), response, 1000);
        if (bytesRead < 1) throw new IOException("No response received");
        LOG.debug(Utils.bytesToHex(response, bytesRead));

        byte[] payload = Arrays.copyOfRange(response, 1, bytesRead - 3);
        int expectedCrc = Utils.b2i(Arrays.copyOfRange(response, bytesRead - 3, bytesRead - 1));
        int calculatedCrc = Utils.getCRC(payload);

        if (expectedCrc != calculatedCrc) {
            throw new IOException("Invalid CRC");
        }
        return Arrays.copyOfRange(response, 7, bytesRead - 3);
    }
}
