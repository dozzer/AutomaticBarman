package com.saienko.androidthings.barman.ui.settings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:45
 */
public class DrinkGroupViewHolder extends RecyclerView.ViewHolder {
    public TextView    textName;
    public ImageButton btnEdit;
    public ImageButton btnDelete;

    public DrinkGroupViewHolder(View v) {
        super(v);
        textName = v.findViewById(R.id.itemName);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnEdit = v.findViewById(R.id.btnEdit);
    }
}
