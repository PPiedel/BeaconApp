package pl.yahoo.pawelpiedel.data.beaconSource;

import android.annotation.SuppressLint;

import com.polidea.rxandroidble2.RxBleClient;
import com.polidea.rxandroidble2.scan.ScanResult;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;
import pl.yahoo.pawelpiedel.data.beaconSource.filters.FilterService;
import pl.yahoo.pawelpiedel.data.beaconSource.filters.FilterServiceFactory;
import pl.yahoo.pawelpiedel.data.beaconSource.filters.FilterServiceType;

import static java.util.Arrays.asList;
import static pl.yahoo.pawelpiedel.data.beaconSource.Constants.BEACON_1_MAC_ADDRESS;
import static pl.yahoo.pawelpiedel.data.beaconSource.Constants.BEACON_2_MAC_ADDRESS;
import static pl.yahoo.pawelpiedel.data.beaconSource.Constants.BEACON_3_MAC_ADDRESS;
import static pl.yahoo.pawelpiedel.data.beaconSource.filters.FilterServiceType.KALMAN;

@Singleton
public class BeaconManager {
    private static final int TX_POWER = -70;
    private static final FilterServiceType FILTER_TYPE = KALMAN;
    private static final List<String> knownDevices = new ArrayList<>(asList(
            BEACON_1_MAC_ADDRESS,
            BEACON_3_MAC_ADDRESS,
            BEACON_2_MAC_ADDRESS)
    );
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
        return knownDevices.contains(macAddress);
    }

    public double getDistance(ScanResult scanResult, FilterServiceType filterServiceType) {
        double smoothedRssi = getSmoothedRssi(scanResult, filterServiceType);
        return distanceCalculationService.calculateDistance(scanResult.getRssi(), TX_POWER);
    }

    private double getSmoothedRssi(ScanResult scanResult, FilterServiceType filterServiceType) {
        String macAddress = scanResult.getBleDevice().getMacAddress();

        if (!deviceRssiFilterServices.containsKey(macAddress)) {
            deviceRssiFilterServices.put(macAddress, FilterServiceFactory.createFilterService(filterServiceType));
        }

        return deviceRssiFilterServices.get(macAddress).getFilteredValue(scanResult.getRssi());
    }


}
