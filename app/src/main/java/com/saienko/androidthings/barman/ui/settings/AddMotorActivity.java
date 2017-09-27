package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.service.CocktailService;
import com.saienko.androidthings.barman.ui.InfoActivity;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import com.saienko.androidthings.barman.ui.settings.adapter.SelectGpioAdapter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class AddMotorActivity extends BaseActivity {

    public static final    String EXTRA_MOTOR_SPEED = "EXTRA_MOTOR_SPEED";
    public static final    String EXTRA_GPIO        = "EXTRA_GPIO";
    protected static final String EXTRA_MOTOR       = "EXTRA_MOTOR";

    private List<Gpio> gpioList;
    private Gpio       selectedGpio;
    private Motor      motor;
    private boolean    isNewMotor;
    private Spinner    spinnerGpio;
    private TextView   tvName;
    private EditText   etSpeed;
    private Button     btnSave;
    private Button     btnTest;

    public static void start(BaseActivity activity, int regCode) {
        Intent intent = new Intent(activity, AddMotorActivity.class);
        activity.startActivityForResult(intent, regCode);
    }

    public static void start(BaseActivity activity, Motor motor,
                             int regCode) {
        Intent intent = new Intent(activity, AddMotorActivity.class);
        intent.putExtra(EXTRA_MOTOR, motor);
        activity.startActivityForResult(intent, regCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motor);
        handleIntent();
        initUI();
        getFreeGpio();
    }

    private void getFreeGpio() {
        Single
                .fromCallable(DatabaseUtil::getFreeGpio)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(gpioList -> {
                    this.gpioList = gpioList;
                    initData();
                });
    }

    private void handleIntent() {
        if (getIntent().hasExtra(EXTRA_MOTOR)) {
            isNewMotor = false;
            motor = getIntent().getParcelableExtra(EXTRA_MOTOR);
        } else {
            isNewMotor = true;
        }
    }

    @Override
    public void initUI() {
        super.initUI();

        spinnerGpio = findViewById(R.id.spinnerGpio);
        tvName = findViewById(R.id.tvName);
        etSpeed = findViewById(R.id.etSpeed);
        btnSave = findViewById(R.id.btnSave);
        ImageButton minus = findViewById(R.id.minus);
        ImageButton plus  = findViewById(R.id.plus);
        btnTest = findViewById(R.id.btnTest);
        ImageButton imgInfo = findViewById(R.id.imgInfo);

        spinnerGpio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGpio = gpioList.get(i);
                showButtons(true);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // IGNORE
            }
        });

        btnSave.setOnClickListener(view -> {

            if (isNewMotor) {
                if (!TextUtils.isEmpty(etSpeed.getText().toString())) {
                    Intent intent = new Intent();
                    intent.putExtra(EXTRA_GPIO, selectedGpio);
                    intent.putExtra(EXTRA_MOTOR_SPEED, Integer.parseInt(etSpeed.getText().toString()));
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showSnackBar("Speed can't be empty");
                }
            } else {
                if (!TextUtils.isEmpty(etSpeed.getText().toString())) {
                    Intent intent = new Intent();
                    motor.setGpio(selectedGpio);
                    motor.setMotorSpeed(Integer.parseInt(etSpeed.getText().toString()));
                    intent.putExtra(EXTRA_MOTOR, motor);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showSnackBar("Speed can't be empty");
                }
            }
        });


        minus.setOnClickListener(view -> {
            int value = Integer.parseInt(etSpeed.getText().toString());
            if (value > 2) {
                value--;
                etSpeed.setText(String.valueOf(value));
            }
        });

        plus.setOnClickListener(view -> {
            int value = Integer.parseInt(etSpeed.getText().toString());
            value++;
            etSpeed.setText(String.valueOf(value));
        });


        btnTest.setOnClickListener(view -> {
            if (isNewMotor) {
                if (!TextUtils.isEmpty(etSpeed.getText().toString())) {
                    Motor newMotor = new Motor(
                            Integer.parseInt(etSpeed.getText().toString()),
                            selectedGpio.getGpioId());
                    newMotor.setGpio(selectedGpio);
                    CocktailService.motorTest(AddMotorActivity.this, newMotor);
                } else {
                    showSnackBar("Speed can't be empty");
                }
            } else {
                CocktailService.motorTest(AddMotorActivity.this, motor);
            }
        });
        imgInfo.setOnClickListener(view -> InfoActivity.start(view.getContext()));
        showButtons(false);
    }

    private void showButtons(boolean show) {
        btnTest.setEnabled(show);
        btnSave.setEnabled(show);
    }

    private void initData() {
        spinnerGpio.setAdapter(new SelectGpioAdapter(this, gpioList));
        if (!isNewMotor) {
            etSpeed.setText(String.valueOf(motor.getMotorSpeed()));
            if (motor.getGpio() != null) {
                spinnerGpio.setSelection(getPosition(gpioList, motor.getGpio().getGpioPin()));
            }
            tvName.setText(motor.getMotorName());
        } else {
            tvName.setText("");
        }
    }

    private int getPosition(List<Gpio> gpioList, int gpioPin) {
        for (int i = 0; i < gpioList.size(); i++) {
            if (gpioList.get(i).getGpioPin() == gpioPin) {
                return i;
            }
        }
        return 0;
    }
}
