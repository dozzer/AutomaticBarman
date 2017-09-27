package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.gpio.Gpio;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.GpioViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:35
 */

public class GpioAdapter extends RecyclerView.Adapter<GpioViewHolder> {

    private List<Gpio>     gpioList;
    private OnItemListener onItemListener;

    public GpioAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public GpioViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        return new GpioViewHolder(onItemListener,
                                  LayoutInflater.from(parent.getContext())
                                                .inflate(R.layout.recycler_item_gpio, parent, false));
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

    public interface OnItemListener {
        void onItemClick(Gpio gpio);
    }
}
