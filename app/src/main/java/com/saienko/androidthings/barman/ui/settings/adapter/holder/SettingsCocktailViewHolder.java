package com.saienko.androidthings.barman.ui.settings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.ui.settings.adapter.SettingsCocktailAdapter;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:47
 */
public class SettingsCocktailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public  TextView                               textName;
    public  ImageButton                            btnDelete;
    public  Cocktail                               cocktail;
    private SettingsCocktailAdapter.OnItemListener onItemListener;

    public SettingsCocktailViewHolder(SettingsCocktailAdapter.OnItemListener onItemListener, View v) {
        super(v);
        this.onItemListener = onItemListener;
        textName = v.findViewById(R.id.itemName);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(view -> onItemListener.onDelete(cocktail));
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(cocktail);
    }
}
