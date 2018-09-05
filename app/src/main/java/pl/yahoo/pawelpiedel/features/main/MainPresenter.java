package pl.yahoo.pawelpiedel.features.main;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.polidea.rxandroidble2.scan.ScanResult;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import pl.yahoo.pawelpiedel.data.DataManager;
import pl.yahoo.pawelpiedel.data.local.BeaconManager;
import pl.yahoo.pawelpiedel.features.base.BasePresenter;
import pl.yahoo.pawelpiedel.injection.ConfigPersistent;
import timber.log.Timber;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {
    private static final String BEACON_1_MAC_ADDRESS = "D0:F0:18:43:DD:65";
    private static final String BEACON_2_MAC_ADDRESS = "D0:F0:18:43:DD:72";
    private static final String BEACON_3_MAC_ADDRESS = "D0:F0:18:43:DD:68";


    private final DataManager dataManager;
    private final BeaconManager beaconManager;
    private Disposable subscription;
    private List<Integer> constants = new ArrayList<>();

    @Inject
    public MainPresenter(DataManager dataManager, BeaconManager beaconManager) {
        this.dataManager = dataManager;
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
                        subscription = beaconManager.getScanResult()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .filter(scanResult -> macAddressEquals(scanResult, BEACON_1_MAC_ADDRESS) ||
                                        macAddressEquals(scanResult, BEACON_2_MAC_ADDRESS) ||
                                        macAddressEquals(scanResult, BEACON_3_MAC_ADDRESS))
                                .subscribe(scanResult ->
                                        Timber.i("{ Scan result : " + scanResult +
                                                "\n device name : " + scanResult.getScanRecord().getDeviceName() +
                                                "\n tx power : " + constants.add(scanResult.getRssi()) +
                                                "\n tx power avg : " + sum() / (double) constants.size() +
                                                "'\n distance : " + beaconManager.getDistance(scanResult.getRssi()) + "}"
                                        ));
                    } else {
                        Timber.d("Required permissions not granted");
                    }
                });


    }

    private boolean macAddressEquals(ScanResult scanResult, String macAddress) {
        return scanResult.getBleDevice().getMacAddress().equals(macAddress);
    }

    private double sum() {
        double sum = 0;
        for (int i = 0, constantsSize = constants.size(); i < constantsSize; i++) {
            sum += constants.get(i);
        }
        Timber.d("Sum " + sum);
        return sum;
    }
}
