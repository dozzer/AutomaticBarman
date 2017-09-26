package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.Utils;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import com.saienko.androidthings.barman.ui.settings.adapter.SelectComponentAdapter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.io.IOException;
import java.util.List;

public class AddPositionActivity extends BaseActivity {

    private static final String TAG = "AddPositionActivity";

    public static final  String EXTRA_POSITION = "EXTRA_POSITION";
    private static final String EXTRA_EDIT     = "EXTRA_EDIT";
    private Position                 position;
    private List<Component>          componentList;
    private Component                selectedComponent;
    private boolean                  isNew;
    private Spinner                  spinnerComponent;
    private PeripheralManagerService pioService;
    private Gpio                     mLedGpio;
    private SelectComponentAdapter   adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_position);
        handleIntent();
        initUI();
        if (Utils.isThingsDevice(this)) {
            pioService = new PeripheralManagerService();
        }
    }

    public static void add(BaseActivity activity, Position position, int regCode) {
        Intent intent = new Intent(activity, AddPositionActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        activity.startActivityForResult(intent, regCode);
    }

    public static void edit(BaseActivity activity, Position position, int regCode) {
        Intent intent = new Intent(activity, AddPositionActivity.class);
        intent.putExtra(EXTRA_POSITION, position);
        intent.putExtra(EXTRA_EDIT, true);
        activity.startActivityForResult(intent, regCode);
    }

    private void handleIntent() {
        position = getIntent().getParcelableExtra(EXTRA_POSITION);
        isNew = !getIntent().hasExtra(EXTRA_EDIT);
    }

    @Override
    public void initUI() {
        super.initUI();
        TextView tvMotor = findViewById(R.id.tvMotor);
        spinnerComponent = findViewById(R.id.spinnerComponent);
        Button btnSave = findViewById(R.id.btnSave);
        Button btnPour = findViewById(R.id.btnPour);

        btnPour.setOnTouchListener((view, motionEvent) -> {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    btnPour.setBackgroundColor(getColor(R.color.colorAccent));
                    startPour(position);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    btnPour.setBackgroundColor(getColor(R.color.lighter_gray));
                    endPour();
                    break;
                default:
                    break;
            }
            return false;
        });

        tvMotor.setText(position.getMotor().getMotorName());
        spinnerComponent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedComponent = componentList.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // IGNORE
            }
        });

        btnSave.setOnClickListener(view -> {
            position.setComponent(selectedComponent);
            savePosition(position);
        });

        if (isNew) {
            Single.fromCallable(DatabaseUtil::getFreeComponents).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(components -> {
                      componentList = components;
                      spinnerComponent.setAdapter(getAdapter());
                  });
        } else {
            Single.fromCallable(DatabaseUtil::getComponents).subscribeOn(Schedulers.io())
                  .observeOn(AndroidSchedulers.mainThread())
                  .subscribe(components -> {
                      componentList = components;
                      spinnerComponent.setAdapter(getAdapter());
                      if (!isNew) {
                          spinnerComponent.setSelection(getPosition(componentList, position.getComponent().getId()));
                      }
                  });
        }
    }

    private SpinnerAdapter getAdapter() {
        if (adapter == null) {
            adapter = new SelectComponentAdapter(AddPositionActivity.this, componentList);
        }
        return adapter;
    }

    private void endPour() {
        Log.d(TAG, "endPour: ");
        if (pioService != null && mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing LED GPIO", e);
            } finally {
                mLedGpio = null;
            }
            mLedGpio = null;
        }
    }

    private void startPour(Position position) {
        Log.d(TAG, "startPour: ");
        if (pioService != null && mLedGpio == null) {
            try {
                mLedGpio = pioService.openGpio(position.getMotor().getGpio().getGpioName());
                mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            } catch (IOException e) {
                Log.e(TAG, "startPour: ", e);
            }
        }

    }

    private void savePosition(Position position) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_POSITION, position);
        setResult(RESULT_OK, intent);
        finish();
    }

    private int getPosition(List<Component> components, long componentId) {
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i).getId() == componentId) {
                return i;
            }
        }
        return 0;
    }

}
