package pl.yahoo.pawelpiedel.features.main;

import pl.yahoo.pawelpiedel.data.place.Place;
import pl.yahoo.pawelpiedel.features.base.MvpView;

public interface MainMvpView extends MvpView {

    void showError(Throwable error);

    void onViewAttached();

    void showPlaceDetails(Place place);
}
