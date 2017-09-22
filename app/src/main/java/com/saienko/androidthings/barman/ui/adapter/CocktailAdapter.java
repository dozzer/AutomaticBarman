package com.saienko.androidthings.barman.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

public class CocktailAdapter extends RecyclerView.Adapter<CocktailAdapter.ViewHolder> {

    private List<Cocktail> cocktailList;
    private OnItemListener onItemListener;

    public CocktailAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public CocktailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_main_cocktail, parent, false));
    }

    @Override
    public void onBindViewHolder(CocktailAdapter.ViewHolder holder, int position) {
        Cocktail item = cocktailList.get(position);
        holder.btn.setText(item.getCocktailName());
        holder.cocktail = item;
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

    public List<Cocktail> getItems() {
        return cocktailList;
    }

    public interface OnItemListener {
        void onStart(Cocktail cocktail);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button   btn;
        Cocktail cocktail;

        ViewHolder(View v) {
            super(v);
            btn = v.findViewById(R.id.btnCocktail);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onItemListener.onStart(cocktail);
        }
    }
}
