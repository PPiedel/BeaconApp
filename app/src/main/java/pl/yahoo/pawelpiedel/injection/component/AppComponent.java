package pl.yahoo.pawelpiedel.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import pl.yahoo.pawelpiedel.data.beacon.BeaconManager;
import pl.yahoo.pawelpiedel.data.place.PlaceDataSource;
import pl.yahoo.pawelpiedel.features.information.InformationService;
import pl.yahoo.pawelpiedel.features.tts.TextToSpeechService;
import pl.yahoo.pawelpiedel.features.vibrations.VibrationsService;
import pl.yahoo.pawelpiedel.injection.ApplicationContext;
import pl.yahoo.pawelpiedel.injection.module.AppModule;

@Singleton
@Component(modules = AppModule.class)
public interface AppComponent {

    @ApplicationContext
    Context context();

    Application application();

    BeaconManager beaconmanager();

    PlaceDataSource placeDataSource();

    TextToSpeechService textToSpeechService();

    VibrationsService vibrationService();

    InformationService informationService();
}
