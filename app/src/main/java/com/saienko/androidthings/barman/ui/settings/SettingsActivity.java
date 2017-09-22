package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;
import java.util.concurrent.Callable;

public class SettingsActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        initUI();
    }

    @Override
    public void initUI() {
        super.initUI();
        Button btnMotors = findViewById(R.id.btnMotors);
        btnMotors.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), SettingsMotorActivity.class)));

        Button btnGpio = findViewById(R.id.btnGpio);
        btnGpio.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), SettingsGpioActivity.class)));

        Button btnDrinks = findViewById(R.id.btnCocktails);
        btnDrinks.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), SettingsCocktailsActivity.class)));

        Button btnGroups = findViewById(R.id.btnGroup);
        btnGroups.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), SettingsDrinkGroupsActivity.class)));

        Button btnComponent = findViewById(R.id.btnComponent);
        btnComponent.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), SettingsComponentActivity.class)));

        Button btnPositions = findViewById(R.id.btnPositions);
        btnPositions.setOnClickListener(
                view -> startActivity(new Intent(getApplicationContext(), SettingsPositionsActivity.class)));
        Single
                .fromCallable(new Callable<Boolean>() {
                    @Override
                    public Boolean call() throws Exception {
                        List<Component> components = DatabaseUtil.getComponents();
                        List<Position>  positions  = DatabaseUtil.getPositions();
                        List<Motor>     motors     = DatabaseUtil.getMotors();
                        if (components.size() > motors.size() || positions.size() > motors.size()) {
                            return false;
                        }

                        for (Position position : positions) {
                            if (position.getComponent() == null || position.getMotor() == null) {
                                return false;
                            }
                        }
                        return true;
                    }
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean isOk) throws Exception {
                        btnPositions.setBackgroundColor(getColor(isOk ? R.color.lighter_gray : R.color.colorAccent));
                    }
                });
//                .subscribe(new CompletableObserver() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        btnPositions.setBackgroundColor(getColor(R.color.lighter_gray));
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        btnPositions.setBackgroundColor(getColor(R.color.colorAccent));
//                    }
//                });
    }

}
