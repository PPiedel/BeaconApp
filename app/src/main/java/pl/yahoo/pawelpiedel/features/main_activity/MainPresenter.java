package pl.yahoo.pawelpiedel.features.main_activity;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.yahoo.pawelpiedel.data.beacon.BeaconManager;
import pl.yahoo.pawelpiedel.data.place.PlaceDataSource;
import pl.yahoo.pawelpiedel.features.base.BasePresenter;
import pl.yahoo.pawelpiedel.features.filtering.FilterServiceType;
import pl.yahoo.pawelpiedel.features.tts.TextToSpeechService;
import pl.yahoo.pawelpiedel.features.vibrations.VibrationsService;
import pl.yahoo.pawelpiedel.injection.ConfigPersistent;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final BeaconManager beaconManager;
    private final PlaceDataSource placeDataSource;
    private final TextToSpeechService textToSpeechService;
    private final VibrationsService vibrationsService;

    @Inject
    MainPresenter(BeaconManager beaconManager, PlaceDataSource placeDataSource, TextToSpeechService textToSpeechService, VibrationsService vibrationsService) {
        this.beaconManager = beaconManager;
        this.placeDataSource = placeDataSource;
        this.textToSpeechService = textToSpeechService;
        this.vibrationsService = vibrationsService;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
        mvpView.onViewAttached();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getBeaconsNearby(Activity activity) {
        RxPermissions rxPermissions = new RxPermissions(activity);
        rxPermissions
                .request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.BLUETOOTH)
                .subscribe(this::handlePermisionsResult);
    }

    private void handlePermisionsResult(Boolean granted) {
        if (granted) {
            compositeDisposable.add(subscribeBeaconsNearby());
        } else {
            Timber.d("Required permissions not granted");
        }
    }

    private Disposable subscribeBeaconsNearby() {
        return beaconManager.getScanResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(scanResult -> beaconManager.isKnownDevice(scanResult.getBleDevice().getMacAddress()))
                .subscribe(scanResult -> {
                    String macAddress = scanResult.getBleDevice().getMacAddress();

                    double distance = beaconManager.getDistance(scanResult, FilterServiceType.KALMAN);

                    if (distance < 10) {
                        getPlaceDetails(macAddress);
                    }
                });
    }

    private void getPlaceDetails(String macAddress) {
        compositeDisposable.add(
                placeDataSource.getPlace(macAddress)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(scanResult -> !textToSpeechService.isSpeaking())
                        .subscribe(place -> {
                                    this.mvpView.showPlaceDetails(place);
                                    this.textToSpeechService.speak(place.getName());
                                    this.vibrationsService.vibrate(500);
                                },
                                Timber::i)
        );
    }




}
