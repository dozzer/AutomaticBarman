package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.DrinkGroupViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

public class DrinkGroupAdapter extends RecyclerView.Adapter<DrinkGroupViewHolder> {

    private List<CocktailGroup> drinkGroups;
    private OnItemListener      onItemListener;

    public DrinkGroupAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public DrinkGroupViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        return new DrinkGroupViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(DrinkGroupViewHolder holder, int position) {
        holder.textName.setText(drinkGroups.get(position).getDrinkGroupName());
        holder.btnEdit.setOnClickListener(view -> onItemListener.onEdit(drinkGroups.get(holder.getAdapterPosition())));
        holder.btnDelete
                .setOnClickListener(view -> onItemListener.onDelete(drinkGroups.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        if (drinkGroups == null) {
            return 0;
        }
        return drinkGroups.size();
    }

    public void addItems(List<CocktailGroup> groups) {
        this.drinkGroups = groups;
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onEdit(CocktailGroup drinkGroup);

        void onDelete(CocktailGroup drinkGroup);
    }

}
