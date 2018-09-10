package pl.yahoo.pawelpiedel.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.polidea.rxandroidble2.RxBleClient;

import dagger.Module;
import dagger.Provides;
import pl.yahoo.pawelpiedel.data.local.filters.FilterService;
import pl.yahoo.pawelpiedel.data.local.filters.KalmanFilterService;
import pl.yahoo.pawelpiedel.injection.ApplicationContext;

import static pl.yahoo.pawelpiedel.Constants.PREF_FILE_NAME;

@Module(includes = {ApiModule.class})
public class AppModule {
    private final Application application;

    public AppModule(Application application) {
        this.application = application;
    }

    @Provides
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }

    @Provides
    @ApplicationContext
    SharedPreferences provideSharedPreference(@ApplicationContext Context context) {
        return context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    RxBleClient reactiveBeacons(@ApplicationContext Context context) {
        return RxBleClient.create(context);
    }

    @Provides
    FilterService filterService() {
        return new KalmanFilterService();
    }
}
