package pl.yahoo.pawelpiedel.data.local;

import javax.inject.Inject;

public class EnvironmentConstantCalculationService {
    private static final double TEN = 10;

    @Inject
    public EnvironmentConstantCalculationService() {
    }

    public double getEnvironmentConstant(double knownDistance, int rssiAt1m, int rssi) {
        return ((double) rssiAt1m - rssi) / TEN * Math.log10(knownDistance);
    }
}
