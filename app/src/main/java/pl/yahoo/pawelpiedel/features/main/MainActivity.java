package pl.yahoo.pawelpiedel.features.main;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import javax.inject.Inject;

import butterknife.BindView;
import pl.yahoo.pawelpiedel.R;
import pl.yahoo.pawelpiedel.features.base.BaseActivity;
import pl.yahoo.pawelpiedel.features.common.ErrorView;
import pl.yahoo.pawelpiedel.injection.component.ActivityComponent;
import timber.log.Timber;

public class MainActivity extends BaseActivity implements MainMvpView, ErrorView.ErrorListener {

    @Inject
    MainPresenter mainPresenter;

    @BindView(R.id.view_error)
    ErrorView errorView;

    @BindView(R.id.progress)
    ProgressBar progressBar;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setSupportActionBar(toolbar);

        errorView.setErrorListener(this);


    }



    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void inject(ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    protected void attachView() {
        mainPresenter.attachView(this);
    }

    @Override
    protected void detachPresenter() {
        mainPresenter.detachView();
    }

    @Override
    public void showError(Throwable error) {
        errorView.setVisibility(View.VISIBLE);
        Timber.e(error, "There was an error retrieving the pokemon");
    }

    @Override
    public void onReloadData() {

    }
}
