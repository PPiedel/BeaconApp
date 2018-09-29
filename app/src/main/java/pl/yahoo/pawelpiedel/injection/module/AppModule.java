package pl.yahoo.pawelpiedel.injection.module;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.polidea.rxandroidble2.RxBleClient;
import com.readystatesoftware.chuck.ChuckInterceptor;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.yahoo.pawelpiedel.BuildConfig;
import pl.yahoo.pawelpiedel.data.place.local.PlaceLocalDataSource;
import pl.yahoo.pawelpiedel.data.place.remote.PlaceRemoteDataSource;
import pl.yahoo.pawelpiedel.features.filtering.FilterService;
import pl.yahoo.pawelpiedel.features.filtering.KalmanFilterService;
import pl.yahoo.pawelpiedel.data.place.PlaceDataSource;
import pl.yahoo.pawelpiedel.data.place.remote.BeaconApi;
import pl.yahoo.pawelpiedel.injection.ApplicationContext;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static pl.yahoo.pawelpiedel.Constants.PREF_FILE_NAME;

@Module(includes = {ApiModule.class})
public class AppModule {
    private final Application application;

    private final static String BASE_URL = "http://192.168.0.16:8080/";

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

    @Provides
    @Singleton
    BeaconApi provideApi(Retrofit retrofit) {
        return retrofit.create(BeaconApi.class);
    }

    @Provides
    @Singleton
    Retrofit provideRetrofit(OkHttpClient okHttpClient, Gson gson) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient(HttpLoggingInterceptor httpLoggingInterceptor,
                                     StethoInterceptor stethoInterceptor,
                                     ChuckInterceptor chuckInterceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(chuckInterceptor);
            httpClientBuilder.addInterceptor(httpLoggingInterceptor);
            httpClientBuilder.addNetworkInterceptor(stethoInterceptor);
        }
        return httpClientBuilder.build();
    }

    @Provides
    @Singleton
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor(message -> Timber.d(message));
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return loggingInterceptor;
    }

    @Provides
    @Singleton
    StethoInterceptor provideStethoInterceptor() {
        return new StethoInterceptor();
    }

    @Provides
    @Singleton
    ChuckInterceptor provideChuckInterceptor(@ApplicationContext Context context) {
        return new ChuckInterceptor(context);
    }

    @Provides
    @Singleton
    Gson provideGson() {
        return new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    @Provides
    @Singleton
    PlaceDataSource placeDataSource(PlaceLocalDataSource placeLocalDataSource, PlaceRemoteDataSource placeRemoteDataSource) {
        return new PlaceDataSource(placeRemoteDataSource, placeLocalDataSource);
    }

    @Provides
    @Singleton
    PlaceLocalDataSource placeLocalService(SharedPreferences sharedPreferences, Gson gson) {
        return new PlaceLocalDataSource(sharedPreferences, gson);
    }

    @Provides
    @Singleton
    PlaceRemoteDataSource placeRemoteService(BeaconApi beaconApi){
        return new PlaceRemoteDataSource(beaconApi);
    }

}
