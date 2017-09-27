package com.saienko.androidthings.barman.ui.settings.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.SpinnerGroupViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 17:25
 */

public class SelectGroupAdapter extends ArrayAdapter<CocktailGroup> {

    public SelectGroupAdapter(@NonNull Context context,
                              List<CocktailGroup> gpioList) {
        super(context, R.layout.spinner_item_group, gpioList);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        SpinnerGroupViewHolder holder;
        CocktailGroup          item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_gpio, parent, false);
            holder = new SpinnerGroupViewHolder();
            holder.name = convertView.findViewById(R.id.motorName);
            convertView.setTag(holder);
        } else {
            holder = (SpinnerGroupViewHolder) convertView.getTag();
        }

        holder.name.setText(item.getDrinkGroupName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        CocktailGroup  item     = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View           row      = inflater.inflate(R.layout.spinner_item_gpio, parent, false);
        TextView       txtTitle = row.findViewById(R.id.motorName);

        txtTitle.setText(item.getDrinkGroupName());
        return row;
    }

}
