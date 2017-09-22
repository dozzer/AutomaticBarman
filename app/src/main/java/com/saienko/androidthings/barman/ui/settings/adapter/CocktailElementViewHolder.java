package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 17:15
 */

class CocktailElementViewHolder extends RecyclerView.ViewHolder {
    TextView    textName;
    TextView    textVolume;
    ImageButton btnDelete;

    CocktailElementViewHolder(View v) {
        super(v);
        textName = v.findViewById(R.id.itemName);
        textVolume = v.findViewById(R.id.itemVolume);
        btnDelete = v.findViewById(R.id.btnDelete);
    }
}