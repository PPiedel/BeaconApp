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
import pl.yahoo.pawelpiedel.data.place.Place;
import pl.yahoo.pawelpiedel.data.place.PlaceDataSource;
import pl.yahoo.pawelpiedel.features.base.BasePresenter;
import pl.yahoo.pawelpiedel.features.filtering.FilterServiceType;
import pl.yahoo.pawelpiedel.features.information.InformationService;
import pl.yahoo.pawelpiedel.features.tts.TextToSpeechService;
import pl.yahoo.pawelpiedel.features.vibrations.VibrationsService;
import pl.yahoo.pawelpiedel.injection.ConfigPersistent;
import pl.yahoo.pawelpiedel.util.MathUtil;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {
    private final BeaconManager beaconManager;
    private final InformationService informationService;
    private final TextToSpeechService textToSpeechService;
    private final VibrationsService vibrationsService;
    private final PlaceDataSource placeDataSource;

    @Inject
    public MainPresenter(BeaconManager beaconManager, InformationService informationService, TextToSpeechService textToSpeechService, VibrationsService vibrationsService, PlaceDataSource placeDataSource) {
        this.beaconManager = beaconManager;
        this.informationService = informationService;
        this.textToSpeechService = textToSpeechService;
        this.vibrationsService = vibrationsService;
        this.placeDataSource = placeDataSource;
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
                    double distance = MathUtil.round(beaconManager.getDistance(scanResult, FilterServiceType.KALMAN), 2);

                    hadleInformationAboutPlace(macAddress, distance);
                });
    }

    private void hadleInformationAboutPlace(String macAddress, double distance) {
        compositeDisposable.add(
                placeDataSource.getPlace(macAddress)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(place -> {
                            handleWhetherToInformUserAboutPlace(distance, place);
                        })
        );
    }

    private void handleWhetherToInformUserAboutPlace(double distance, Place place) {
        compositeDisposable.add(
                informationService.handleWhetherToInformUserAboutPlace(place, distance)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .filter(place1 -> !textToSpeechService.isSpeaking())
                        .subscribe(nearPlace -> {
                            vibrationsService.vibrate(500);
                            textToSpeechService.speak(place, distance);
                            informationService.savePlaceWasDisplayedToUser(place);
                        })
        );
    }
}
