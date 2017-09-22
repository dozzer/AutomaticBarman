package com.saienko.androidthings.barman.ui.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.ui.base.BaseActivity;

public class ViewGpioActivity extends BaseActivity {

    private static final String EXTRA_GPIO = "EXTRA_GPIO";
    private Gpio gpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_gpio);
        handleIntent();
        initUI();
    }

    private void handleIntent() {
        gpio = getIntent().getParcelableExtra(EXTRA_GPIO);
    }

    public static void start(Context context, Gpio gpio) {
        Intent intent = new Intent(context, ViewGpioActivity.class);
        intent.putExtra(EXTRA_GPIO, gpio);
        context.startActivity(intent);
    }

    @Override
    public void initUI() {
        super.initUI();
        TextView tvName = findViewById(R.id.tvName);
        TextView tvPin  = findViewById(R.id.tvPin);

        tvName.setText(gpio.getGpioName());
        tvPin.setText(String.valueOf(gpio.getGpioPin()));
    }
}
