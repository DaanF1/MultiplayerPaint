package client;

import java.util.regex.Pattern;

public class Regex {

    // This code is contributed by Aman Kumar.
    static boolean Validate_It(String IP) {
        // Regex expression for validating IPv4
        String regexIpv4 = "(([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])\\.){3}([0-9]|[1-9][0-9]|1[0-9][0-9]|2[0-4][0-9]|25[0-5])";

        // Regex expression for validating IPv6
        String regexIpv6 = "((([0-9a-fA-F]){1,4})\\:){7}([0-9a-fA-F]){1,4}";

        Pattern rPatternIpv4 = Pattern.compile(regexIpv4);
        Pattern rPatternIpv6 = Pattern.compile(regexIpv6);

        // Checking if it is a valid IPv4 addresses
        if (rPatternIpv4.matcher(IP).matches()) return true;

            // Checking if it is a valid IPv6 addresses
        else if (rPatternIpv6.matcher(IP).matches()) return true;

        // Return Invalid
        return false;
    }
}
