package com.play.playsystem.basic.utils.tool;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Component
public class SysUtil {

    public static String port;

    public static String patternPath;

    @Value("${server.port}")
    public void setPort(String port) {
        SysUtil.port = port;
    }

    @Value("${file.pattern-path}")
    public void setPatternPath(String patternPath) {
        SysUtil.patternPath = patternPath;
    }

    public static String getServerIpAddress() {
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            return  "localhost";
        }
    }
}
