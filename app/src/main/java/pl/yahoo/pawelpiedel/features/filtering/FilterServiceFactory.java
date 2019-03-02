package pl.yahoo.pawelpiedel.features.filtering;

public class FilterServiceFactory {

    private FilterServiceFactory() {
        //prevent from object creation
    }

    public static Filter createFilterService(FilterServiceType filterServiceType, double startRssi) {
        Filter service;
        switch (filterServiceType) {
            case KALMAN:
                service = new KalmanFilter(startRssi);
                break;
            default:
                service = new KalmanFilter(startRssi);
        }
        return service;
    }
}
