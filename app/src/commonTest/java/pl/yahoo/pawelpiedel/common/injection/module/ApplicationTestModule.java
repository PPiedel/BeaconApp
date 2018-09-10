package pl.yahoo.pawelpiedel.common.injection.module;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.yahoo.pawelpiedel.injection.ApplicationContext;

/**
 * Provides application-level dependencies for an app running on a testing environment This allows
 * injecting mocks if necessary.
 */
@Module
public class ApplicationTestModule {
    private final Application application;

    public ApplicationTestModule(Application application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return application;
    }
}
