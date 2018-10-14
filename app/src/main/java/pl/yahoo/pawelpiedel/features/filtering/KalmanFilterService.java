package pl.yahoo.pawelpiedel.features.filtering;

import javax.inject.Inject;

public class KalmanFilterService implements FilterService {
    private static final double PROCESS_NOISE_COVARIANCE = 0.065;
    private static final double MEASUREMENT_NOISE_COVARIANCE = 1.4;

    private double previousRssi;
    private double previousEstimationErrorCovariance;

    @Inject
    public KalmanFilterService(double startRssi) {
        this.previousRssi = startRssi;
        previousEstimationErrorCovariance = 0;
    }

    @Override
    public double getFilteredRssi(int rssi) {
        double currentEstimationErrorCovariance = previousEstimationErrorCovariance + PROCESS_NOISE_COVARIANCE;

        double kalmanGain = currentEstimationErrorCovariance / (currentEstimationErrorCovariance + MEASUREMENT_NOISE_COVARIANCE);
        double currentRssi = previousRssi + kalmanGain * (rssi - previousRssi);

        previousEstimationErrorCovariance = (1 - kalmanGain) * currentEstimationErrorCovariance;
        previousRssi = currentRssi;

        return currentRssi;
    }
}
