package pl.yahoo.pawelpiedel.features.information;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.place.Place;
import timber.log.Timber;

public class InformationService {
    private static final int DANGER_PLACE_FIRST_TIME_DISTANCE = 6;
    private static final int STANDARD_PLACE_FIRST_TIME_DISTANCE = 3;
    private static final int PLACE_SECOND_TIME_DISTANCE = 1;

    private Map<Place, Integer> displayedPlaces = new HashMap<>();

    @Inject
    public InformationService() {
    }

    public Observable<Place> handleWhetherToInformUserAboutPlace(Place place, double distance) {
        String placeType = place.getPlaceType();

        if (placeType.equals("DANGER") && distance < DANGER_PLACE_FIRST_TIME_DISTANCE && !placeWasDisplayedToUserTimes(place, 1)) {
            return Observable.just(place);
        } else if (distance < STANDARD_PLACE_FIRST_TIME_DISTANCE && !placeWasDisplayedToUserTimes(place, 1)) {
            return Observable.just(place);
        } else if (distance < PLACE_SECOND_TIME_DISTANCE && !placeWasDisplayedToUserTimes(place, 2)) {
            return Observable.just(place);
        } else {
            return Observable.empty();
        }
    }

    public void savePlaceWasDisplayedToUser(Place place) {
        Integer number = displayedPlaces.get(place);
        if (number == null) {
            displayedPlaces.put(place, 1);
        } else {
            displayedPlaces.put(place, number + 1);
        }

    }

    private boolean placeWasDisplayedToUserTimes(Place place, Integer howManyTimes) {
        Timber.d("Checking if place " + place + "was displayed before...");
        Integer number = displayedPlaces.get(place);
        Timber.d("Was displayed times = " + number);
        return number != null && number >= howManyTimes;
    }
}
