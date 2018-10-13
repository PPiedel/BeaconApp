package pl.yahoo.pawelpiedel.features.information;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.place.Place;
import timber.log.Timber;

public class InformationService {
    private static final int DANGER_PLACE_DISTANCE = 6;
    private static final int STANDARD_PLACE_DISTANCE = 3;

    private List<Place> displayedPlaces = new ArrayList<>();

    @Inject
    public InformationService() {
    }

    public Observable<Place> handleWhetherToInformUserAboutPlace(Place place, double distance) {
        String placeType = place.getPlaceType();

        if (placeType.equals("DANGER") && distance < DANGER_PLACE_DISTANCE && !placeWasDisplayedToUser(place)) {
            return Observable.just(place);
        } else if (distance < STANDARD_PLACE_DISTANCE && !placeWasDisplayedToUser(place)) {
            return Observable.just(place);
        } else {
            return Observable.empty();
        }
    }

    public void savePlaceWasDisplayedToUser(Place place) {
        displayedPlaces.add(place);
    }

    private boolean placeWasDisplayedToUser(Place place) {
        Timber.d("Checking if place " + place + "was displayed before...");
        boolean wasDisplayedToUser = displayedPlaces.contains(place);
        Timber.d("Was displayed = " + wasDisplayedToUser);
        return wasDisplayedToUser;
    }
}
