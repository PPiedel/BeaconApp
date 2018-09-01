package pl.yahoo.pawelpiedel.common.injection.component;

import javax.inject.Singleton;

import dagger.Component;
import pl.yahoo.pawelpiedel.common.injection.module.ApplicationTestModule;
import pl.yahoo.pawelpiedel.injection.component.AppComponent;

@Singleton
@Component(modules = ApplicationTestModule.class)
public interface TestComponent extends AppComponent {
}
