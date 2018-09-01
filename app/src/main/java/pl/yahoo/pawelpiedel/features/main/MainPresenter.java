package pl.yahoo.pawelpiedel.features.main;

import javax.inject.Inject;

import pl.yahoo.pawelpiedel.data.DataManager;
import pl.yahoo.pawelpiedel.features.base.BasePresenter;
import pl.yahoo.pawelpiedel.injection.ConfigPersistent;

@ConfigPersistent
public class MainPresenter extends BasePresenter<MainMvpView> {

    private final DataManager dataManager;

    @Inject
    public MainPresenter(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Override
    public void attachView(MainMvpView mvpView) {
        super.attachView(mvpView);
    }
}
