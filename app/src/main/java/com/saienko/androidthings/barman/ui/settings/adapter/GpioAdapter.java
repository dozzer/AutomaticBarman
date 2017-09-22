package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.gpio.Gpio;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:35
 */

public class GpioAdapter extends RecyclerView.Adapter<GpioAdapter.GpioViewHolder> {

    private List<Gpio>     gpioList;
    private OnItemListener onItemListener;

    public GpioAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public GpioViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        return new GpioViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_gpio, parent, false));
    }

    @Override
    public void onBindViewHolder(GpioViewHolder holder, int position) {
        Gpio gpio = gpioList.get(holder.getAdapterPosition());
        holder.gpioName.setText(gpio.getGpioName());
        holder.gpioPin.setText(String.valueOf(gpio.getGpioPin()));
        holder.gpio = gpio;
    }

    @Override
    public int getItemCount() {
        if (gpioList == null) {
            return 0;
        }
        return gpioList.size();
    }

    public void addItems(List<Gpio> gpioList) {
        this.gpioList = gpioList;
        notifyDataSetChanged();
    }

    class GpioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView gpioName;
        TextView gpioPin;
        Gpio     gpio;

        GpioViewHolder(View v) {
            super(v);
            gpioName = v.findViewById(R.id.motorName);
            gpioPin = v.findViewById(R.id.component);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onItemClick(gpio);
        }
    }

    public interface OnItemListener {
        void onItemClick(Gpio gpio);
    }
}
