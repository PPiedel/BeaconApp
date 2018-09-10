package pl.yahoo.pawelpiedel.data.local.filters;

import javax.inject.Inject;

public class KalmanFilterService implements FilterService {
    private static final double PROCESS_NOISE_COVARIANCE = 0.065;
    private static final double MEASUREMENT_NOISE_COVARIANCE = 1.4;

    private double previousValue;
    private double previousEstimationErrorCovariance;

    @Inject
    public KalmanFilterService() {
        previousValue = 0;
        previousEstimationErrorCovariance = 0;
    }

    @Override
    public double getFilteredValue(int rssi) {
        double currentEstimationErrorCovariance = previousEstimationErrorCovariance + PROCESS_NOISE_COVARIANCE;

        double kalmanGain = currentEstimationErrorCovariance / (currentEstimationErrorCovariance + MEASUREMENT_NOISE_COVARIANCE);
        double currentValue = previousValue + kalmanGain * (rssi - previousValue);

        previousEstimationErrorCovariance = (1 - kalmanGain) * currentEstimationErrorCovariance;
        previousValue = currentValue;

        return currentValue;
    }
}
