package pl.yahoo.pawelpiedel.data.local;

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
import pl.yahoo.pawelpiedel.data.local.filters.FilterService;
import pl.yahoo.pawelpiedel.data.local.filters.FilterServiceFactory;
import pl.yahoo.pawelpiedel.data.local.filters.FilterServiceType;

import static java.util.Arrays.asList;

@Singleton
public class BeaconManager {
    private static final int TX_POWER = -69;
    private static final String BEACON_1_MAC_ADDRESS = "D0:F0:18:43:DD:65";
    private static final String BEACON_2_MAC_ADDRESS = "D0:F0:18:43:DD:72";
    private static final String BEACON_3_MAC_ADDRESS = "D0:F0:18:43:DD:68";
    private static final List<String> knownDevices = new ArrayList<>(asList(
            BEACON_1_MAC_ADDRESS,
            BEACON_2_MAC_ADDRESS,
            BEACON_3_MAC_ADDRESS)
    );
    private static Map<String, FilterService> deviceRssiFilterServices = new HashMap<>();

    private final RxBleClient rxBleClient;
    private final DistanceService distanceService;

    @Inject
    public BeaconManager(RxBleClient rxBleClient, DistanceService distanceService) {
        this.rxBleClient = rxBleClient;
        this.distanceService = distanceService;
    }

    @SuppressLint("MissingPermission")
    public Observable<ScanResult> getScanResult() {
        return rxBleClient.scanBleDevices(
                new ScanSettings.Builder()
                        .setScanMode(ScanSettings.SCAN_MODE_BALANCED)
                        .build()
        );
    }

    public double getDistance(double rssi) {
        return distanceService.calculateDistance(rssi, TX_POWER);
    }

    public boolean isKnownDevice(String macAddress) {
        return knownDevices.contains(macAddress);
    }

    public double getSmoothedRssi(ScanResult scanResult, FilterServiceType filterServiceType) {
        String macAddress = scanResult.getBleDevice().getMacAddress();

        if (!deviceRssiFilterServices.containsKey(macAddress)) {
            deviceRssiFilterServices.put(macAddress, FilterServiceFactory.createFilterService(filterServiceType));
        }

        return deviceRssiFilterServices.get(macAddress).getFilteredValue(scanResult.getRssi());
    }
}
