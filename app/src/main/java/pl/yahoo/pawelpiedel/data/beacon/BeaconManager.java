package pl.yahoo.pawelpiedel.data.beacon;

import android.annotation.SuppressLint;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.features.distanceCalculation.DistanceCalculationService;
import pl.yahoo.pawelpiedel.features.filtering.FilterService;
import pl.yahoo.pawelpiedel.features.filtering.FilterServiceFactory;
import pl.yahoo.pawelpiedel.features.filtering.FilterServiceType;

import static pl.yahoo.pawelpiedel.features.distanceCalculation.Constants.KNOWN_DEVICES;

@Singleton
public class BeaconManager {
    private static final int TX_POWER = -70;

    private static Map<String, FilterService> deviceRssiFilterServices = new HashMap<>();

    private final RxBleClient rxBleClient;
    private final DistanceCalculationService distanceCalculationService;

    @Inject
    public BeaconManager(RxBleClient rxBleClient, DistanceCalculationService distanceCalculationService) {
        this.rxBleClient = rxBleClient;
        this.distanceCalculationService = distanceCalculationService;
    }

    @SuppressLint("MissingPermission")
    public Observable<ScanResult> getScanResult() {
        return rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                        .build()
        );
    }

    public boolean isKnownDevice(String macAddress) {
        return KNOWN_DEVICES.contains(macAddress);
    }

    public double getDistance(ScanResult scanResult, FilterServiceType filterServiceType) {
        double smoothedRssi = getSmoothedRssi(scanResult, filterServiceType);
        return distanceCalculationService.calculateDistance(smoothedRssi, TX_POWER);
    }

    private double getSmoothedRssi(ScanResult scanResult, FilterServiceType filterServiceType) {
        String macAddress = scanResult.getBleDevice().getMacAddress();

        if (!deviceRssiFilterServices.containsKey(macAddress)) {
            deviceRssiFilterServices.put(macAddress, FilterServiceFactory.createFilterService(filterServiceType));
        }

        return deviceRssiFilterServices.get(macAddress).getFilteredValue(scanResult.getRssi());
    }


}
