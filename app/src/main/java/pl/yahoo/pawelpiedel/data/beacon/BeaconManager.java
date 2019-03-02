package pl.yahoo.pawelpiedel.data.beacon;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.features.distance.DistanceCalculationService;
import pl.yahoo.pawelpiedel.features.filtering.Filter;
import pl.yahoo.pawelpiedel.features.filtering.FilterServiceFactory;
import pl.yahoo.pawelpiedel.features.filtering.FilterServiceType;
import timber.log.Timber;

import static pl.yahoo.pawelpiedel.features.distance.Constants.KNOWN_DEVICES;

@Singleton
public class BeaconManager {
    private static final int TX_POWER = -70;

    private static Map<String, Filter> devicesFilters = new HashMap<>();

    private final RxBleClient rxBleClient;
    private final DistanceCalculationService distanceCalculationService;

    @Inject
    public BeaconManager(RxBleClient rxBleClient, DistanceCalculationService distanceCalculationService) {
        this.rxBleClient = rxBleClient;
        this.distanceCalculationService = distanceCalculationService;
    }

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
        double distance = distanceCalculationService.calculateDistance(smoothedRssi, TX_POWER);

        Timber.d("\nMAC address : " + scanResult.getBleDevice().getMacAddress() +
                "\n smoothed rssi : " + smoothedRssi +
                "\n distance : " + distance);

        return distance;
    }

    private double getSmoothedRssi(ScanResult scanResult, FilterServiceType filterServiceType) {
        String macAddress = scanResult.getBleDevice().getMacAddress();
        if (!devicesFilters.containsKey(macAddress)) {
            devicesFilters.put(macAddress, FilterServiceFactory.createFilterService(filterServiceType, scanResult.getRssi()));
        }
        return devicesFilters.get(macAddress).getFilteredRssi(scanResult.getRssi());
    }
}
