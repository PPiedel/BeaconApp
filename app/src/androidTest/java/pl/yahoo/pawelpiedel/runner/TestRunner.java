package pl.yahoo.pawelpiedel.runner;

import android.app.Application;
import android.content.Context;

import io.appflate.restmock.android.RESTMockTestRunner;
import pl.yahoo.pawelpiedel.BeaconApplication;

public class TestRunner extends RESTMockTestRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, BeaconApplication.class.getName(), context);
    }
}
