package com.saienko.androidthings.barman.ui.settings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.ui.settings.adapter.GpioAdapter;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:45
 */
public class GpioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public  TextView                   gpioName;
    public  TextView                   gpioPin;
    public  Gpio                       gpio;
    private GpioAdapter.OnItemListener onItemListener;

    public GpioViewHolder(GpioAdapter.OnItemListener onItemListener, View v) {
        super(v);
        this.onItemListener = onItemListener;
        gpioName = v.findViewById(R.id.motorName);
        gpioPin = v.findViewById(R.id.component);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(gpio);
    }
}
