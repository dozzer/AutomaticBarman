package com.saienko.androidthings.barman.ui.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Component;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 01:34
 */

class ComponentDialogAdapter extends ArrayAdapter<Component> {
    ComponentDialogAdapter(@NonNull Context context, List<Component> list) {
        super(context, R.layout.dialog_item_group, list);
    }

    private class ViewHolder {
        TextView name;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        ViewHolder holder;
        Component  item = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_item_group, parent, false);
            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.name);
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
        View           row      = inflater.inflate(R.layout.dialog_item_group, parent, false);
        TextView       txtTitle = row.findViewById(R.id.name);

        txtTitle.setText(item.getName());
        return row;
    }
}
