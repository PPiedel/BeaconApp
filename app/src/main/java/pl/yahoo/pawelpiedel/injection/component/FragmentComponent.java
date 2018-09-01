package pl.yahoo.pawelpiedel.injection.component;

import dagger.Subcomponent;
import pl.yahoo.pawelpiedel.injection.PerFragment;
import pl.yahoo.pawelpiedel.injection.module.FragmentModule;

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = FragmentModule.class)
public interface FragmentComponent {
}
