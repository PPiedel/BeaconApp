package pl.yahoo.pawelpiedel.injection.component;

import dagger.Subcomponent;
import pl.yahoo.pawelpiedel.features.main.MainActivity;
import pl.yahoo.pawelpiedel.injection.PerActivity;
import pl.yahoo.pawelpiedel.injection.module.ActivityModule;

@PerActivity
@Subcomponent(modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(MainActivity mainActivity);
}
