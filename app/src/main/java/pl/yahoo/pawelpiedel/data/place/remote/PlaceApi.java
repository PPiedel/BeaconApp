package pl.yahoo.pawelpiedel.data.place.remote;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.place.Place;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PlaceApi {
    @GET("/places")
    Observable<Place> getPlace(@Query(value = "mac") String macAddress);
}
