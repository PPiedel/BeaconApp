package pl.yahoo.pawelpiedel.features.main;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.polidea.rxandroidble2.scan.ScanResult;
import com.tbruyelle.rxpermissions.RxPermissions;

import javax.inject.Inject;

import io.reactivex.Notification;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.yahoo.pawelpiedel.data.local.BeaconManager;
import pl.yahoo.pawelpiedel.features.base.BasePresenter;
import pl.yahoo.pawelpiedel.injection.ConfigPersistent;
import timber.log.Timber;

import static pl.yahoo.pawelpiedel.data.local.filters.FilterServiceType.KALMAN;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final BeaconManager beaconManager;

    @Inject
    public MainPresenter(BeaconManager beaconManager) {
        this.beaconManager = beaconManager;
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
                .subscribe(granted -> {
                    if (granted) {
                        compositeDisposable.add(subscribeBeaconsNearby());
                    } else {
                        Timber.d("Required permissions not granted");
                    }
                });
    }

    @NonNull
    private Disposable subscribeBeaconsNearby() {
        return beaconManager.getScanResult()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(scanResult -> beaconManager.isKnownDevice(scanResult.getBleDevice().getMacAddress()))
                .doOnEach(this::logMacAddress)
                .subscribe(scanResult -> {
                            double smoothedRssi = beaconManager.getSmoothedRssi(scanResult, KALMAN);
                            Timber.d("\nSmoothed rssi : " + smoothedRssi +
                                    "\nDistance: " + beaconManager.getDistance(smoothedRssi));
                        }
                );
    }

    private void logMacAddress(Notification<ScanResult> scanResult) {
        Timber.d("\n\n " +
                "\nMAC address : " + scanResult.getValue().getBleDevice().getMacAddress() +
                "\nreceived rssi : " + scanResult.getValue().getRssi());
    }
}
