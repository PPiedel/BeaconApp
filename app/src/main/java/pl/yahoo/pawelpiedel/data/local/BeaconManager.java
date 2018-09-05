package pl.yahoo.pawelpiedel.data.local;

import android.annotation.SuppressLint;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

@Singleton
public class BeaconManager {
    private static final int TX_POWER = -69;

    private final RxBleClient rxBleClient;
    private final DistanceService distanceService;
    private final CorrelationService correlationService;

    @Inject
    public BeaconManager(RxBleClient rxBleClient, DistanceService distanceService, CorrelationService correlationService) {
        this.rxBleClient = rxBleClient;
        this.distanceService = distanceService;
        this.correlationService = correlationService;
    }

    @SuppressLint("MissingPermission")
    public Observable<ScanResult> getScanResult() {
        return rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                        .build()
        );
    }

    public double getDistance(int rssi) {
        return distanceService.calculateDistance(rssi, TX_POWER);
    }

    public double getEnvironmentConstant(int rssi) {
        return correlationService.getEnvironmentConstant(0.5, TX_POWER, rssi);
    }
}
