package com.saienko.androidthings.barman.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;
import com.saienko.androidthings.barman.Utils;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.db.motor.Motor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CocktailService extends IntentService {

    private static final String TAG = "CocktailService";

    private static final String ACTION_COCKTAIL_CREATE = "ACTION_COCKTAIL_CREATE";
    private static final String ACTION_COCKTAIL_CANCEL = "ACTION_COCKTAIL_CANCEL";
    private static final String ACTION_MOTOR_TEST      = "ACTION_MOTOR_TEST";
    private static final String ACTION_MOTOR_CLEAR     = "ACTION_MOTOR_CLEAR";

    private static final String EXTRA_COCKTAIL   = "EXTRA_COCKTAIL";
    private static final String EXTRA_MOTOR      = "EXTRA_MOTOR";
    private static final String EXTRA_MOTOR_LIST = "EXTRA_MOTOR_LIST";

    public static final String BROADCAST_COCKTAIL_PROGRESS = "BROADCAST_COCKTAIL_PROGRESS";

    public static final String BROADCAST_COCKTAIL_STATUS         = "BROADCAST_COCKTAIL_STATUS";
    public static final String BROADCAST_EXTRA_COCKTAIL_STATUS   = "BROADCAST_EXTRA_COCKTAIL_STATUS";
    public static final String BROADCAST_EXTRA_COCKTAIL_ITEM_MAP = "BROADCAST_EXTRA_COCKTAIL_ITEM_MAP";

    private static boolean isTaskRun           = false;
    private static boolean taskShouldBeStopped = false;

    private HashMap<Long, Integer> progressMap;
    private ArrayList<Thread>      threadList;

    public CocktailService() {
        super("CocktailService");
    }

    public static void startCocktail(Context context, Cocktail cocktail) {
        Intent intent = new Intent(context, CocktailService.class);
        intent.setAction(ACTION_COCKTAIL_CREATE);
        intent.putExtra(EXTRA_COCKTAIL, cocktail);
        context.startService(intent);
    }

    public static void stopCocktail(Context context) {
        Intent intent = new Intent(context, CocktailService.class);
        intent.setAction(ACTION_COCKTAIL_CANCEL);
        context.startService(intent);
    }

    public static void motorTest(Context context, Motor motor) {
        Intent intent = new Intent(context, CocktailService.class);
        intent.setAction(ACTION_MOTOR_TEST);
        intent.putExtra(EXTRA_MOTOR, motor);
        context.startService(intent);
    }


    public static void clearMotors(Context context, List<Motor> motorList) {
        Intent intent = new Intent(context, CocktailService.class);
        intent.setAction(ACTION_MOTOR_CLEAR);
        intent.putParcelableArrayListExtra(EXTRA_MOTOR_LIST, (ArrayList<Motor>) motorList);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_COCKTAIL_CREATE.equals(action)) {
                final Cocktail cocktail = intent.getParcelableExtra(EXTRA_COCKTAIL);
                handleActionCocktail(cocktail);
            } else if (ACTION_COCKTAIL_CANCEL.equals(action)) {
                Log.d(TAG, "onHandleIntent: cancel");
                sendCocktailStatus(CocktailStatus.CANCEL);
                stopTask();
                Thread.currentThread().interrupt();
                stopSelf();
            } else if (ACTION_MOTOR_TEST.equals(action)) {
                motorTest(intent.getParcelableExtra(EXTRA_MOTOR));
                Log.i(TAG, "onHandleIntent: motorTest");
            } else if (ACTION_MOTOR_CLEAR.equals(action)) {
                clear(intent.getParcelableArrayListExtra(EXTRA_MOTOR_LIST));
            } else {
                throw new UnsupportedOperationException("Unknown action");
            }
        }
    }

    private void stopTask() {
        if (isTaskRun) {
            taskShouldBeStopped = true;
        }

        if (threadList != null) {
            for (Thread thread : threadList) {
                thread.interrupt();
            }
        }
    }

    private void handleActionCocktail(Cocktail cocktail) {
        taskShouldBeStopped = false;
        isTaskRun = true;
        progressMap = new HashMap<>();
        threadList = new ArrayList<>();
        for (CocktailElement cocktailElement : cocktail.getCocktailElements()) {
            progressMap.put(cocktailElement.getPosition().getMotor().getGpioId(), 0);
            pour(cocktailElement.getPosition().getMotor(), cocktailElement.getVolume());
        }
    }

    private void sendCocktailItemProgress(long gpioId, int progress) {
        progressMap.put(gpioId, progress);

        Intent intentUpdate = new Intent();
        intentUpdate.setAction(BROADCAST_COCKTAIL_PROGRESS);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        intentUpdate.putExtra(BROADCAST_EXTRA_COCKTAIL_ITEM_MAP, progressMap);
        sendBroadcast(intentUpdate);
    }

    private void sendCocktailStatus(CocktailStatus status) {
        Intent intentUpdate = new Intent();
        intentUpdate.setAction(BROADCAST_COCKTAIL_STATUS);
        intentUpdate.addCategory(Intent.CATEGORY_DEFAULT);
        intentUpdate.putExtra(BROADCAST_EXTRA_COCKTAIL_STATUS, status);
        sendBroadcast(intentUpdate);
    }

    private void motorTest(Motor motor) {
        pour(motor, 100);
    }

    private static void setLedValue(Gpio ledGpio, boolean value) {
        try {
            ledGpio.setValue(value);
        } catch (IOException e) {
            Log.e(TAG, "Error updating GPIO value", e);
        }
    }

    private void clear(List<Motor> motorList) {
        for (Motor motor : motorList) {
            if (Utils.isThingsDevice(this)) {
                pour(motor, 500);
            }
        }
    }

    private void startRealPourThread(int millis, Motor motor) {
        Thread thread = new Thread(() -> {

            Log.d(TAG, "startRealPourThread: before sleep GPIO" + motor.getGpio());

            long startTime = System.nanoTime();
            try {
                Thread.sleep(millis);
            } catch (InterruptedException e) {
                Log.e(TAG, "startRealPourThread: ", e);
                Thread.currentThread().interrupt();
            }
            long endTime  = System.nanoTime();
            long duration = (endTime - startTime);
            Log.d(TAG, "startRealPourThread: time " + duration + " GPIO " + motor.getGpio());
        });
        threadList.add(thread);
        thread.start();
    }

    private void startProgressThread(int step, Motor motor) {
        Thread thread = new Thread(() -> {
            Log.d(TAG, "startProgressThread: before sleep GPIO " + motor.getGpio());

            long startTime = System.nanoTime();
            for (int i = 0; i < 101; i++) {
                try {
                    Thread.sleep(step);
                    if (taskShouldBeStopped) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (InterruptedException e) {
                    Log.e(TAG, "pourDemo: ", e);
                    Thread.currentThread().interrupt();
                }
                sendCocktailItemProgress(motor.getGpioId(), i);
            }
            long endTime  = System.nanoTime();
            long duration = (endTime - startTime);
            Log.d(TAG, "startProgressThread: time " + duration + " GPIO " + motor.getGpio());
        });
        thread.start();
    }

    private void pour(Motor motor, int ml) {
        Log.d(TAG, "pour() called with: motor = [" + motor + "], ml = [" + ml + "]");
        if (taskShouldBeStopped) {
            return;
        }
        Log.d(TAG, "pour() called with: motor = [" + motor + "], ml = [" + ml + "]");
        PeripheralManagerService pioService = null;
        if (Utils.isThingsDevice(this)) {
            pioService = new PeripheralManagerService();
        }
        Gpio mLedGpio = null;
        int  millis   = ml * motor.getMotorSpeed();
        int  step     = millis / 100;

        Log.d(TAG, "pour: millis = [" + millis + "], step= [" + step + "]");

        try {

            if (Utils.isThingsDevice(this)) {
                mLedGpio = pioService.openGpio(motor.getGpio().getGpioName());
                mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
                setLedValue(mLedGpio, true);
            }
            Log.d(TAG, "pour: start sleep");
            startProgressThread(step, motor);
            startRealPourThread(millis, motor);

            if (Utils.isThingsDevice(this)) {
                setLedValue(mLedGpio, false);
            }

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
