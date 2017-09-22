package com.saienko.androidthings.barman.ui.settings.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 17:25
 */

public class SelectComponentAdapter extends ArrayAdapter<Component> {

    public SelectComponentAdapter(@NonNull Context context,
                                  List<Component> components) {
        super(context, R.layout.spinner_item_group, components);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        Component  item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_item_gpio, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.motorName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(item.getName());
        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {

        Component      item     = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View           row      = inflater.inflate(R.layout.spinner_item_gpio, parent, false);
        TextView       txtTitle = row.findViewById(R.id.motorName);

        txtTitle.setText(item.getName());
        return row;
    }

    public List<Component> getItems() {
        List<Component> list = new ArrayList<>(getCount());
        for (int i = 0; i < getCount(); i++) {
            list.add(getItem(i));
        }
        return list;
    }

    private class ViewHolder {
        TextView name;
    }
}
