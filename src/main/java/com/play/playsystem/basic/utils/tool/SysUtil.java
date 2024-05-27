package com.play.playsystem.basic.utils.tool;

import org.springframework.beans.factory.annotation.Value;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class SysUtil {

    @Value("${server.port}")
    public String port;

    public static String getServerIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return  "localhost";
        }
    }
}
