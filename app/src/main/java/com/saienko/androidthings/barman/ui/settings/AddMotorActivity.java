package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.service.CocktailService;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import com.saienko.androidthings.barman.ui.settings.adapter.SelectGpioAdapter;

import java.util.ArrayList;
import java.util.List;

public class AddMotorActivity extends BaseActivity {
    private static final String EXTRA_GPIO_LIST = "EXTRA_GPIO_LIST";

    public static final    String EXTRA_MOTOR_SPEED = "EXTRA_MOTOR_SPEED";
    public static final    String EXTRA_GPIO        = "EXTRA_GPIO";
    protected static final String EXTRA_MOTOR       = "EXTRA_MOTOR";

    private ArrayList<Gpio> gpioList;
    private Gpio            selectedGpio;
    private Motor           motor;
    private boolean         isNewMotor;

    //todo remove List<Gpio> gpioList
    public static void start(BaseActivity activity, List<Gpio> gpioList, int regCode) {
        Intent intent = new Intent(activity, AddMotorActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GPIO_LIST, (ArrayList<Gpio>) gpioList);
        activity.startActivityForResult(intent, regCode);
    }

    //todo remove List<Gpio> gpioList
    public static void start(BaseActivity activity, List<Gpio> gpioList, Motor motor,
                             int regCode) {
        Intent intent = new Intent(activity, AddMotorActivity.class);
        intent.putParcelableArrayListExtra(EXTRA_GPIO_LIST, (ArrayList<Gpio>) gpioList);
        intent.putExtra(EXTRA_MOTOR, motor);
        activity.startActivityForResult(intent, regCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_motor);
        handleIntent();
        initUI();
    }

    private void handleIntent() {
        gpioList = getIntent().getParcelableArrayListExtra(EXTRA_GPIO_LIST);
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

        Spinner     spinnerGpio = findViewById(R.id.spinnerGpio);
        TextView    tvName      = findViewById(R.id.tvName);
        EditText    etSpeed     = findViewById(R.id.etSpeed);
        Button      btnSave     = findViewById(R.id.btnSave);
        ImageButton minus       = findViewById(R.id.minus);
        ImageButton plus        = findViewById(R.id.plus);
        Button      btnTest     = findViewById(R.id.btnTest);

        spinnerGpio.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGpio = gpioList.get(i);
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
//                    motor.setMotorName(etName.getText().toString());
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
        spinnerGpio.setAdapter(new SelectGpioAdapter(this, gpioList));
        if (!isNewMotor) {
            etSpeed.setText(String.valueOf(motor.getMotorSpeed()));
            spinnerGpio.setSelection(getPosition(gpioList, motor.getGpio().getGpioPin()));
            tvName.setText(motor.getMotorName());
        }else{
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
