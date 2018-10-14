package pl.yahoo.pawelpiedel.features.filtering;

public class FilterServiceFactory {

    private FilterServiceFactory() {
        //prevent from object creation
    }

    public static FilterService createFilterService(FilterServiceType filterServiceType, double startRssi) {
        FilterService service;
        switch (filterServiceType) {
            case KALMAN:
                service = new KalmanFilterService(startRssi);
                break;
            default:
                service = new KalmanFilterService(startRssi);
        }
        return service;
    }
}
