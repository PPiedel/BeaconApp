package pl.yahoo.pawelpiedel.features.distance;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

public class Constants {
    public static final String BEACON_1_MAC_ADDRESS = "D0:F0:18:43:DD:65";
    public static final String BEACON_2_MAC_ADDRESS = "D0:F0:18:43:DD:68";
    public static final String BEACON_3_MAC_ADDRESS = "D0:F0:18:43:DD:72";
    public static final List<String> KNOWN_DEVICES = new ArrayList<>(asList(
            BEACON_1_MAC_ADDRESS,
            BEACON_3_MAC_ADDRESS,
            BEACON_2_MAC_ADDRESS)
    );

    private Constants() {
    }
}
