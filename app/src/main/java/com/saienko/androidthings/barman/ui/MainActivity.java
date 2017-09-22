/*
 * Copyright 2016, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.saienko.androidthings.barman.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.Utils;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.ui.adapter.CocktailAdapter;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import com.saienko.androidthings.barman.ui.settings.SettingsActivity;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";

    private ImageButton  btnSettings;
    private RecyclerView rvCocktails;
//    private View         demoView;

//    private Button button1;

    private CocktailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
        getCocktailList();
        if (Utils.isThingsDevice(this)) {
            getDeviceList();
        }
    }


    private void getCocktailList() {
        Single.fromCallable(DatabaseUtil::getCocktails)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(this::showCocktails, this::showErrorSnackBar);

    }

    private void showCocktails(List<Cocktail> cocktailList) {
//        if (cocktailList.isEmpty()) {
//            demoView.setVisibility(View.VISIBLE);
//            rvCocktails.setVisibility(View.GONE);
//        } else {
//            demoView.setVisibility(View.GONE);
        rvCocktails.setVisibility(View.VISIBLE);
        rvCocktails.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new GridLayoutManager(this, 3);
        rvCocktails.setLayoutManager(layoutManager);
        rvCocktails.setAdapter(getAdapter());
        getAdapter().addItems(cocktailList);
//        }
    }

    private CocktailAdapter getAdapter() {
        if (adapter == null) {
            adapter = new CocktailAdapter(cocktail -> CocktailActivity.start(MainActivity.this, cocktail));
        }
        return adapter;
    }

    @Override
    public void initUI() {
        super.initUI();
        btnSettings = findViewById(R.id.settingsBtn);
        btnSettings.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingsActivity.class)));
//        button1 = findViewById(R.id.btnCocktail1);
//        button1.setOnClickListener(v -> createCocktail(getAdapter().getItems().get(0)));
//        demoView = findViewById(R.id.demo_view);
        rvCocktails = findViewById(R.id.rvCocktails);
    }

//    private void createCocktail(Cocktail cocktail) {
//        CocktailService.startCocktail(this, cocktail);
//    }

    void getDeviceList() {
        Log.d(TAG, "getDeviceList() called");
        PeripheralManagerService manager  = new PeripheralManagerService();
        List<String>             portList = manager.getGpioList();
        for (String port : manager.getGpioList()) {
            turnOffPort(port, manager);
        }
        if (portList.isEmpty()) {
            Log.i(TAG, "No GPIO port available on this device.");
        } else {
            Log.i(TAG, "List of available ports: " + portList);
        }
    }

    private void turnOffPort(String port, PeripheralManagerService manager) {
        Gpio mLedGpio = null;
        try {

            mLedGpio = manager.openGpio(port);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pins", e);
        }

        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            }
        }
    }
}
