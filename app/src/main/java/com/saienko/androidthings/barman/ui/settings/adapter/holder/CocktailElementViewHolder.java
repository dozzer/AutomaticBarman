package com.saienko.androidthings.barman.ui.settings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.ui.settings.adapter.CocktailElementAdapter;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 17:15
 */

public class CocktailElementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public  TextView                              textName;
    public  TextView                              textVolume;
    public  ImageButton                           btnDelete;
    public  CocktailElement                       cocktailElement;
    private CocktailElementAdapter.OnItemListener onItemListener;

    public CocktailElementViewHolder(CocktailElementAdapter.OnItemListener onItemListener, View v) {
        super(v);
        this.onItemListener = onItemListener;
        textName = v.findViewById(R.id.itemName);
        textVolume = v.findViewById(R.id.itemVolume);
        btnDelete = v.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(view -> onItemListener.onDelete(cocktailElement));
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onEdit(cocktailElement);
    }
}