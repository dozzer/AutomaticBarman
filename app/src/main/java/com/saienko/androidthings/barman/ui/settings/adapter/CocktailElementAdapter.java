package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

public class CocktailElementAdapter extends RecyclerView.Adapter<CocktailElementViewHolder> {

    private ArrayList<CocktailElement> cocktailElements;
    private OnItemListener             onItemListener;

    public CocktailElementAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public CocktailElementViewHolder onCreateViewHolder(ViewGroup parent,
                                                        int viewType) {
        return new CocktailElementViewHolder(
                LayoutInflater.from(parent.getContext())
                              .inflate(R.layout.recycler_item_cocktail_element, parent, false));
    }

    @Override
    public void onBindViewHolder(CocktailElementViewHolder holder, int position) {
        CocktailElement item = cocktailElements.get(holder.getAdapterPosition());
        holder.textName.setText(item.getComponent().getName());
        holder.textVolume.setText(String.valueOf(item.getVolume()));
        holder.btnDelete
                .setOnClickListener(view -> onItemListener.onDelete(item));
    }

    @Override
    public int getItemCount() {
        if (cocktailElements == null) {
            return 0;
        }
        return cocktailElements.size();
    }

//    public void addItems(ArrayList<CocktailElement> cocktailElements) {
//        this.cocktailElements = cocktailElements;
//        notifyDataSetChanged();
//    }

    public void add(CocktailElement component) {
        if (cocktailElements == null) {
            cocktailElements = new ArrayList<>();
        }
        cocktailElements.add(component);
        notifyDataSetChanged();
    }

    public ArrayList<CocktailElement> getItems() {
        return cocktailElements;
    }

    public void delete(CocktailElement cocktailElement) {
        cocktailElements.remove(cocktailElement);
        notifyDataSetChanged();
    }

    public void addAll(List<CocktailElement> cocktailElements) {
        for (CocktailElement cocktailElement : cocktailElements) {
            add(cocktailElement);
        }
    }

    public interface OnItemListener {
        void onDelete(CocktailElement cocktailElement);
    }
}
