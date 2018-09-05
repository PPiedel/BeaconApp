package pl.yahoo.pawelpiedel.data.local;

import javax.inject.Inject;

public class DistanceService {
    private static final double TEN = 10.0;
    private static final double ENVIRONMENT_CONSTANT = 2.0;

    @Inject
    public DistanceService() {
    }

    public double calculateDistance(int rssi, int txCalibratedPower) {
        return Math.pow(10d, ((double) txCalibratedPower - rssi) / (TEN * ENVIRONMENT_CONSTANT));
    }
}
