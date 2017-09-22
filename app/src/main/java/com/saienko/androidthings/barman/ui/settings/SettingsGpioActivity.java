package com.saienko.androidthings.barman.ui.settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;
import com.saienko.androidthings.barman.ui.settings.adapter.GpioAdapter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingsGpioActivity extends BaseListActivity {

    private GpioAdapter          adapter;
    private FloatingActionButton fabAddGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        initUI();
        getGpioList();
    }

    @Override
    public void initUI() {
        super.initUI();
        fabAddGpio = findViewById(R.id.fabAdd);
        fabAddGpio.setOnClickListener(view -> addGpio());

        initRecyclerView(findViewById(R.id.list));
    }

    private void getGpioList() {
        Single.fromCallable(DatabaseUtil::getGpios)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(gpioList -> {
                  getAdapter().addItems(gpioList);
                  if (!gpioList.isEmpty()) {
                      fabAddGpio.setVisibility(View.GONE);
                  } else {
                      fabAddGpio.setVisibility(View.VISIBLE);
                  }
              }, this::showErrorSnackBar);
    }

    @Override
    protected GpioAdapter getAdapter() {
        if (adapter == null) {
            adapter = new GpioAdapter(gpio -> ViewGpioActivity.start(SettingsGpioActivity.this, gpio));
        }
        return adapter;
    }

    private void addGpio() {
        Completable.fromCallable(() -> {
            AppDatabase.getDb().gpioDao().insertAll(Gpio.getRealGpios());
            getGpioList();
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    @Override
    protected void removeAll() {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().gpioDao().clear();
                    getGpioList();
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
