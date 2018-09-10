package pl.yahoo.pawelpiedel.data.local;

import javax.inject.Inject;

public class KalmanFilterService implements FilterService {
    private static final double VALUE_0 = 0;
    private static final double EST_ERR_COVARIANCE_0 = 0;
    private static final double PROCESS_NOISE_COVARIANCE = 0.065;
    private static final double MEASUREMENT_NOISE_COVARIANCE = 1.4;

    private double previousValue;
    private double previousEstimationErrorCovariance;

    @Inject
    public KalmanFilterService() {
        previousValue = VALUE_0;
        previousEstimationErrorCovariance = EST_ERR_COVARIANCE_0;
    }

    @Override
    public double getFilteredValue(int rssi) {
        double currentValue = previousValue;
        double currentEstimationErrorCovariance = previousEstimationErrorCovariance + PROCESS_NOISE_COVARIANCE;

        double kalmanGain = currentEstimationErrorCovariance / (currentEstimationErrorCovariance + MEASUREMENT_NOISE_COVARIANCE);
        currentValue = currentValue + kalmanGain * (rssi - currentValue);
        currentEstimationErrorCovariance = (1 - kalmanGain) * currentEstimationErrorCovariance;

        previousEstimationErrorCovariance = currentEstimationErrorCovariance;
        previousValue = currentValue;

        return currentValue;
    }
}
