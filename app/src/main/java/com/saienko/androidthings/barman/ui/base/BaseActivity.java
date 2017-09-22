package com.saienko.androidthings.barman.ui.base;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import com.saienko.androidthings.barman.R;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:42
 */

public abstract class BaseActivity extends AppCompatActivity {
    private View rootView;

    protected View getRootView() {
        return rootView;
    }

    public void initUI() {
        rootView = findViewById(R.id.root);
        if (rootView == null){
            throw new IllegalStateException("root view can't be empty");
        }
    }

    public void showSnackBar(String message) {
        SnackBarImpl.showSnackBar(getRootView(), message);
    }

    public void showErrorSnackBar(String message) {
        SnackBarImpl.showErrorSnackBar(getRootView(), message);
    }

    public void showErrorSnackBar(Throwable throwable) {
        SnackBarImpl.showErrorSnackBar(getRootView(), throwable);
    }

//    private String getRandomName() {
//        RandomString gen = new RandomString(8, ThreadLocalRandom.current());
//        return gen.nextString();
//    }
}
