package pl.yahoo.pawelpiedel.injection.module;

import android.app.Activity;
import android.content.Context;

import com.tbruyelle.rxpermissions.RxPermissions;

import dagger.Module;
import dagger.Provides;
import pl.yahoo.pawelpiedel.injection.ActivityContext;
import pl.yahoo.pawelpiedel.injection.PerActivity;

@Module
public class ActivityModule {

    private Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @Provides
    Activity provideActivity() {
        return activity;
    }

    @Provides
    @ActivityContext
    Context providesContext() {
        return activity;
    }

    @Provides
    @PerActivity
    RxPermissions provideRxPermissions() {
        return new RxPermissions(activity);
    }
}
