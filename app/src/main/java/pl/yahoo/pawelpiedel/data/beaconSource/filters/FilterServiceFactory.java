package pl.yahoo.pawelpiedel.data.beaconSource.filters;

public class FilterServiceFactory {

    private FilterServiceFactory(){
        //prevent from instantiating
    }

    public static FilterService createFilterService(FilterServiceType filterServiceType) {
        FilterService service;
        switch (filterServiceType) {
            case KALMAN:
                service = new KalmanFilterService();
                break;
            default:
                service = new KalmanFilterService();
        }
        return service;
    }
}
