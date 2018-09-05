package pl.yahoo.pawelpiedel.data.local;

import javax.inject.Inject;

public class CorrelationService {
    private static final double TEN = 10;

    @Inject
    public CorrelationService() {
    }

    public double getEnvironmentConstant(double knownDistance, int txPower, int rssi) {
        return ((double) txPower - rssi) / TEN * Math.log10(knownDistance);
    }
}
