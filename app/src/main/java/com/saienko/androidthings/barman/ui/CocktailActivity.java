package com.saienko.androidthings.barman.ui;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.LongSparseArray;
import android.widget.Button;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.listener.OnResultListener;
import com.saienko.androidthings.barman.listener.OnUpdateListener;
import com.saienko.androidthings.barman.receiver.CocktailBroadcastReceiver;
import com.saienko.androidthings.barman.receiver.CocktailUpdateBroadcastReceiver;
import com.saienko.androidthings.barman.service.CocktailService;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import devlight.io.library.ArcProgressStackView;

import java.util.ArrayList;

public class CocktailActivity extends BaseActivity {

    private static final String TAG = "CocktailActivity";

    private static final String EXTRA_COCKTAIL = "EXTRA_COCKTAIL";
    private static final long   TOTAL_POSITION = -1;

    CocktailBroadcastReceiver       cocktailBroadcastReceiver;
    CocktailUpdateBroadcastReceiver cocktailUpdateBroadcastReceiver;

    Cocktail cocktail;

    LongSparseArray<ArcProgressStackView.Model> holderMap;
    LongSparseArray<Boolean>                    cocktailEnd;
    LongSparseArray<Integer>                    progressArray;
    private Button               btnStart;
    private Button               btnStop;
    private ArcProgressStackView mArcProgressStackView;
    private int                  modelCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cocktail);
        holderMap = new LongSparseArray<>();
        progressArray = new LongSparseArray<>();
        cocktailEnd = new LongSparseArray<>();
        handleIntent();
        registerBroadcastReceiver();
        initUI();
    }

    private void handleIntent() {
        cocktail = getIntent().getParcelableExtra(EXTRA_COCKTAIL);
    }

    public static void start(Context context, Cocktail cocktail) {
        Intent intent = new Intent(context, CocktailActivity.class);
        intent.putExtra(EXTRA_COCKTAIL, cocktail);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        modelCount = cocktail.getCocktailElements().size() + 1;
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        mArcProgressStackView = findViewById(R.id.apsv);

        final int[] startColors = getResources().getIntArray(R.array.green);
//        final int[] endColors   = getResources().getIntArray(R.array.orange);
        final int[] bgColors = getResources().getIntArray(R.array.grey);

        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        for (int i = 0; i < cocktail.getCocktailElements().size(); i++) {
            CocktailElement element =
                    cocktail.getCocktailElements().get(i);
            if (element.getPosition() == null || element.getPosition().getMotor() == null) {
                break;
            }

            cocktailEnd.put(element.getPosition().getMotor().getGpioId(), false);
            ArcProgressStackView.Model model =
                    new ArcProgressStackView.Model(element.getComponent().getName(), i, bgColors[i],
                                                   startColors[i]);
            models.add(model);
            holderMap.put(element.getPosition().getMotor().getGpioId(), model);
            progressArray.put(element.getPosition().getMotor().getGpioId(), 0);
        }
        ArcProgressStackView.Model totalModel =
                new ArcProgressStackView.Model("Total", cocktail.getCocktailElements().size(),
                                               bgColors[cocktail.getCocktailElements().size()],
                                               startColors[cocktail.getCocktailElements().size()]
                );
        holderMap.put(TOTAL_POSITION, totalModel);
        progressArray.put(TOTAL_POSITION, 0);
        models.add(totalModel);

        mArcProgressStackView.setModels(models);

        btnStart.setOnClickListener(view -> {
            CocktailService.startCocktail(CocktailActivity.this, cocktail);
            setProgressValue(0);
        });
        btnStop.setOnClickListener(view -> CocktailService.stopCocktail(CocktailActivity.this));

        mArcProgressStackView.setShadowColor(Color.argb(200, 0, 0, 0));
//        mArcProgressStackView.setAnimationDuration(1000);
//        mArcProgressStackView.setSweepAngle(270);

    }

    private void registerBroadcastReceiver() {
        cocktailUpdateBroadcastReceiver = new CocktailUpdateBroadcastReceiver(new OnUpdateListener() {
            @Override
            public void onItemUpdate(long gpioId, int progress) {
                Log.d(TAG, "onItemUpdate: gpioId" + gpioId + " progress=" + progress);
                setValue(gpioId, progress);
            }

            @Override
            public void onItemFinish(long gpioId) {
                Log.d(TAG, "onItemFinish: " + gpioId);
                calculateFinish(gpioId);
            }

            @Override
            public void onItemStart(long gpioId) {
                Log.d(TAG, "onItemStart: " + gpioId);
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
//                setProgressValue(0);
            }
        });
        cocktailBroadcastReceiver = new CocktailBroadcastReceiver(new OnResultListener() {

            @Override
            public void onError() {
                Log.d(TAG, "onError: ");
                btnStart.setEnabled(false);
                btnStop.setEnabled(false);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
            }
        });
        IntentFilter intentFilter = new IntentFilter(CocktailService.BROADCAST_COCKTAIL_STATUS);
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(cocktailBroadcastReceiver, intentFilter);

        IntentFilter intentUpdateFilter = new IntentFilter(CocktailService.BROADCAST_COCKTAIL_PROGRESS);
        intentUpdateFilter.addCategory(Intent.CATEGORY_DEFAULT);
        registerReceiver(cocktailUpdateBroadcastReceiver, intentUpdateFilter);
    }

    private void setProgressValue(int progress) {
        for (ArcProgressStackView.Model model : mArcProgressStackView.getModels()) {
            model.setProgress(progress);
        }
        mArcProgressStackView.postInvalidate();

        for (int i = 1; i < modelCount; i++) {
            progressArray.put(progressArray.keyAt(i), progress);
        }
    }

    private void setValue(long gpioId, int progress) {
        ArcProgressStackView.Model model = holderMap.get(gpioId);
        if ((int) model.getProgress() != progress) {
            model.setProgress(progress);
        }
        progressArray.put(gpioId, progress);

        ArcProgressStackView.Model totalModel    = holderMap.get(TOTAL_POSITION);
        float                      totalProgress = getTotalProgress();
        if (totalModel.getProgress() != totalProgress) {
            totalModel.setProgress(totalProgress);
        }
        mArcProgressStackView.postInvalidate();
    }

    private float getTotalProgress() {
        int total = 0;
        for (int i = 1; i < modelCount; i++) {
            if (progressArray.keyAt(i) != TOTAL_POSITION) {
                if (progressArray.valueAt(i) != null) {
                    total += progressArray.valueAt(i);
                }

            }
        }
        return total / (modelCount - 1);
    }

    private void calculateFinish(long gpioId) {
        cocktailEnd.put(gpioId, true);
        for (int i = 0; i < cocktailEnd.size(); i++) {
            long    key   = cocktailEnd.keyAt(i);
            boolean value = cocktailEnd.get(key);
            if (!value) {
                return;
            }
        }
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(cocktailBroadcastReceiver);
        unregisterReceiver(cocktailUpdateBroadcastReceiver);
    }
}
