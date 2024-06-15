package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.URL;

public class IPAdress {
    private static String privateIP = null;
    private static String publicIP = null;

    public static String getPrivateIP() throws IOException {
        if (privateIP == null) {
            try (Socket socket = new Socket()) {
                socket.connect(new InetSocketAddress("ifconfig.me", 80));
                privateIP = socket.getLocalAddress().getHostAddress();
            }
        }
        return privateIP;
    }

    public static String getPublicIP() throws IOException {
        if (publicIP == null) {
            String urlString = "http://checkip.amazonaws.com/";
            URL url = new URL(urlString);
            try (BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()))) {
                publicIP = br.readLine();
            }
        }
        return publicIP;
    }
}
