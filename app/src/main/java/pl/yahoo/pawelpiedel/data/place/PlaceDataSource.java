package pl.yahoo.pawelpiedel.data.place;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.place.local.PlaceLocalDataSource;
import pl.yahoo.pawelpiedel.data.place.remote.PlaceRemoteDataSource;

public class PlaceDataSource {

    private final PlaceRemoteDataSource placeRemoteDataSource;
    private final PlaceLocalDataSource placeLocalDataSource;

    public PlaceDataSource(PlaceRemoteDataSource placeRemoteDataSource, PlaceLocalDataSource placeLocalDataSource) {
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
