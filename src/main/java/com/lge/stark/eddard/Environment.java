package com.lge.stark.eddard;

import java.io.File;
import java.net.URISyntaxException;
import java.security.CodeSource;
import java.util.Enumeration;
import java.net.NetworkInterface;
import java.net.InetAddress;
import java.net.SocketException;

public class Environment {

    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(Environment.class);

    /**
     * @param mainClass
     * @return
     */
    public static <T> String getWorkingPath(java.lang.Class<T> mainClass) {
        CodeSource codeSource = mainClass.getProtectionDomain().getCodeSource();

        try {
            return (new File(codeSource.getLocation().toURI().getPath())).getParentFile().getPath();
        }
        catch (URISyntaxException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface networkInterface = en.nextElement();

                for (Enumeration<InetAddress> enumIpAddress = networkInterface.getInetAddresses(); enumIpAddress.hasMoreElements();) {

                    InetAddress inetAddress = enumIpAddress.nextElement();

                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress()) { return inetAddress
                            .getHostAddress().toString(); }
                }
            }
        }
        catch (SocketException e) {
            logger.error(e.getMessage(), e);
        }
        return null;
    }
}