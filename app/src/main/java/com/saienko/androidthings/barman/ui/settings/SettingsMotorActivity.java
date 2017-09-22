package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.Menu;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.service.CocktailService;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;
import com.saienko.androidthings.barman.ui.settings.adapter.MotorAdapter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingsMotorActivity extends BaseListActivity {

    private static final int REG_CODE_ADD_MOTOR  = 63;
    private static final int REG_CODE_EDIT_MOTOR = 64;
    private MotorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        initUI();
        getMotorList();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.list_motor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_clear) {
            clearAllMotors();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void clearAllMotors() {
        Single
                .fromCallable(DatabaseUtil::getMotors)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(motorList -> CocktailService.clearMotors(SettingsMotorActivity.this, motorList),
                           this::showErrorSnackBar);
    }

    @Override
    public void initUI() {
        super.initUI();
        FloatingActionButton fabAddMotor = findViewById(R.id.fabAdd);
        fabAddMotor.setOnClickListener(view -> showDialogAddMotor());
        initRecyclerView(findViewById(R.id.list));
    }


    private void getMotorList() {
        Single
                .fromCallable(DatabaseUtil::getMotors)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(motorList -> getAdapter().addItems(motorList), this::showErrorSnackBar);
    }

    @Override
    protected MotorAdapter getAdapter() {
        if (adapter == null) {
            adapter = new MotorAdapter(new MotorAdapter.OnItemListener() {

                @Override
                public void onDelete(Motor motor) {
                    showDeleteDialog(motor);
                }

                @Override
                public void onItemClick(Motor motor) {
                    editMotor(motor);
                }
            });
        }
        return adapter;
    }

    private void editMotor(Motor motor) {
        Single
                .fromCallable(DatabaseUtil::getFreeGpio)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gpioList -> AddMotorActivity
                        .start(SettingsMotorActivity.this, gpioList, motor, REG_CODE_EDIT_MOTOR));

    }

    private void showDeleteDialog(Motor motor) {
        CustomDialog.showDialog(this,
                                "Are you sure ?",
                                "Do you want to delete this item ?",
                                (dialogInterface, i) -> deleteItem(motor));
    }

    private void deleteItem(Motor motor) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().motorDao().delete(motor);
                    AppDatabase.getDb().positionDao().deleteByMotorId(motor.getMotorId());
                    getMotorList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void showDialogAddMotor() {
        Single
                .fromCallable(DatabaseUtil::getFreeGpio)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gpioList -> AddMotorActivity
                        .start(SettingsMotorActivity.this, gpioList, REG_CODE_ADD_MOTOR));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REG_CODE_ADD_MOTOR) {

                int  motorSpeed = data.getIntExtra(AddMotorActivity.EXTRA_MOTOR_SPEED, -1);
                Gpio gpio       = data.getParcelableExtra(AddMotorActivity.EXTRA_GPIO);
                saveMotor(new Motor(motorSpeed, gpio.getGpioId()));

            } else if (requestCode == REG_CODE_EDIT_MOTOR) {
                Motor motor = data.getParcelableExtra(AddMotorActivity.EXTRA_MOTOR);
                updateMotor(motor);
            }
        }
    }

    private void saveMotor(Motor motor) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().motorDao().insert(motor);
//                    Motor newMotor = AppDatabase.getDb().motorDao().getMotorByName(motor.getMotorName());
//                    AppDatabase.getDb().positionDao().insert(new Position(-1, newMotor.getMotorId()));
                    getMotorList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void updateMotor(Motor motor) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().motorDao().update(motor);
                    updatePosition(motor);
                    getMotorList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void updatePosition(Motor motor) {
        Completable.fromCallable(() -> {
            Position position = AppDatabase.getDb().positionDao().getByMotorId(motor.getMotorId());
            position.setMotorId(motor.getMotorId());
            AppDatabase.getDb().positionDao().update(position);
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    @Override
    protected void removeAll() {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().motorDao().clear();
                    getMotorList();
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
