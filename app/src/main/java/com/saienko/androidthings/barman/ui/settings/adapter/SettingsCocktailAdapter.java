package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.SettingsCocktailViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

public class SettingsCocktailAdapter extends RecyclerView.Adapter<SettingsCocktailViewHolder> {

    private List<Cocktail> cocktailList;
    private OnItemListener onItemListener;

    public SettingsCocktailAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public SettingsCocktailViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        return new SettingsCocktailViewHolder(onItemListener,
                                              LayoutInflater.from(parent.getContext())
                                                            .inflate(R.layout.recycler_item_cocktail, parent, false));
    }

    @Override
    public void onBindViewHolder(SettingsCocktailViewHolder holder, int position) {
        Cocktail cocktail = cocktailList.get(holder.getAdapterPosition());
        holder.textName.setText(cocktail.getCocktailName());
        holder.btnDelete.setOnClickListener(view -> onItemListener.onDelete(cocktail));
        holder.cocktail = cocktail;

    }

    @Override
    public int getItemCount() {
        if (cocktailList == null) {
            return 0;
        }
        return cocktailList.size();
    }

    public void addItems(List<Cocktail> cocktailList) {
        this.cocktailList = cocktailList;
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onItemClick(Cocktail cocktail);

        void onDelete(Cocktail cocktail);
    }

}
