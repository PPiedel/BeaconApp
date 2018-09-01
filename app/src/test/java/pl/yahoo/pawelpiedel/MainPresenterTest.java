package pl.yahoo.pawelpiedel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import pl.yahoo.pawelpiedel.data.DataManager;
import pl.yahoo.pawelpiedel.features.main.MainMvpView;
import pl.yahoo.pawelpiedel.features.main.MainPresenter;
import pl.yahoo.pawelpiedel.util.RxSchedulersOverrideRule;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Rule
    public final RxSchedulersOverrideRule overrideSchedulersRule = new RxSchedulersOverrideRule();

    @Mock
    MainMvpView mockMainMvpView;
    @Mock
    DataManager mockDataManager;
    private MainPresenter mainPresenter;

    @Before
    public void setUp() {
        mainPresenter = new MainPresenter(mockDataManager);
        mainPresenter.attachView(mockMainMvpView);
    }

    @After
    public void tearDown() {
        mainPresenter.detachView();
    }
}
