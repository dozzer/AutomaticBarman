package com.saienko.androidthings.barman.ui.settings.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.gpio.Gpio;

import java.util.ArrayList;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 17:25
 */

public class SelectGpioAdapter extends ArrayAdapter<Gpio> {

    public SelectGpioAdapter(@NonNull Context context,
                             ArrayList<Gpio> gpioList) {
        super(context, R.layout.spinner_item_gpio, gpioList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Gpio       item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_gpio, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.motorName);
            holder.pin = convertView.findViewById(R.id.motorPin);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(item.getGpioName());
        holder.pin.setText(Integer.toString(item.getGpioPin()));
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        Gpio           item     = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View           row      = inflater.inflate(R.layout.spinner_item_gpio, parent, false);
        TextView       txtTitle = row.findViewById(R.id.motorName);
        TextView       pin      = row.findViewById(R.id.motorPin);

        txtTitle.setText(item.getGpioName());
        pin.setText(Integer.toString(item.getGpioPin()));
        return row;
    }

    private class ViewHolder {
        TextView name;
        TextView pin;
    }
}
