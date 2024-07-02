package com.example.utils;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
public class MySnowFlakeGenerator {

    public static SnowflakeGenerator hultoolsSnowflakeGenerator;

    static {
        Long workerId;
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            String hostAddress = localHost.getHostAddress();
            int i = hostAddress.hashCode();
            workerId = Math.abs((long) (i % 32));
            log.info("机器号" + workerId);
            hultoolsSnowflakeGenerator = new SnowflakeGenerator(workerId, 0);
        } catch (UnknownHostException e) {
            log.error("无法生成workerId");
            System.exit(1);
        }

    }

    public static String next() {
        return hultoolsSnowflakeGenerator.next().toString();
    }
}
