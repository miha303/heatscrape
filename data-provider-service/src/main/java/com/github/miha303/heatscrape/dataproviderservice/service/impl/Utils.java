// SPDX-License-Identifier: MIT
// Copyright © 2023 Miha Strohsack
package com.github.miha303.heatscrape.dataproviderservice.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class Utils {

    public static int readInputStreamWithTimeout(InputStream is, byte[] buffer, int timeoutMillis)
            throws IOException {
        int bufferOffset = 0;
        long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;

        while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < buffer.length) {
            int readLength = java.lang.Math.min(is.available(), buffer.length - bufferOffset);
            int readResult = is.read(buffer, bufferOffset, readLength);
            System.out.print(" " + readResult);
            if (readResult == -1) break;
            bufferOffset += readResult;
            try {
                Thread.sleep(timeoutMillis / 10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return bufferOffset;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] =
                    (byte)
                            ((Character.digit(s.charAt(i), 16) << 4)
                                    + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes, int len) {
        char[] hexChars = new char[len * 2];
        for (int j = 0; j < len; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static int getCRC(byte[] bytes) {
        int CRC = 0x0000ffff;
        int POLYNOMIAL = 0x0000a001;

        int i, j;
        for (i = 0; i < bytes.length; i++) {
            CRC ^= ((int) bytes[i] & 0x000000ff);
            for (j = 0; j < 8; j++) {
                if ((CRC & 0x00000001) != 0) {
                    CRC >>= 1;
                    CRC ^= POLYNOMIAL;
                } else {
                    CRC >>= 1;
                }
            }
        }
        return CRC;
    }

    public static BigDecimal decodeTemp(byte... bytes) {
        return BigDecimal.valueOf(b2is(bytes))
                .multiply(BigDecimal.valueOf(0.01))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public static int b2i(byte... bytes) {
        int a = ((bytes[0] & 0xFF) << 0);
        if (bytes.length > 1) a |= ((bytes[1] & 0xFF) << 8);
        if (bytes.length > 2) a |= ((bytes[2] & 0xFF) << 16);
        if (bytes.length > 3) a |= ((bytes[3] & 0xFF) << 24);
        return a;
    }

    public static int b2is(byte... bytes) {
        return (short) b2i(bytes);
    }

    public static String hex2str(byte... b) {
        return hex2str(b, 0, b.length - 1);
    }

    public static String hex2str(byte[] combined, int i, int j) {
        return new String(Arrays.copyOfRange(combined, i, j + 1), StandardCharsets.US_ASCII);
    }
}
