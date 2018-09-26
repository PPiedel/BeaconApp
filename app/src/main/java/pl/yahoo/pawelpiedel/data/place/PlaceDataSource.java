package pl.yahoo.pawelpiedel.data.place;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.place.local.PlaceLocalService;
import pl.yahoo.pawelpiedel.data.place.remote.PlaceRemoteService;

public class PlaceDataSource {

    private final PlaceRemoteService placeRemoteDataSource;
    private final PlaceLocalService placeLocalDataSource;

    public PlaceDataSource(PlaceRemoteService placeRemoteDataSource, PlaceLocalService placeLocalDataSource) {
        this.placeRemoteDataSource = placeRemoteDataSource;
        this.placeLocalDataSource = placeLocalDataSource;
    }

    public Observable<Place> getPlace(String macAddress) {
        Place place = placeLocalDataSource.getPlaceByMacAddress(macAddress);

        return place != null ?
                Observable.just(place) :
                placeRemoteDataSource.getPlace(macAddress)
                        .doOnNext(apiPlace -> placeLocalDataSource.savePlace(apiPlace, macAddress));
    }
}
