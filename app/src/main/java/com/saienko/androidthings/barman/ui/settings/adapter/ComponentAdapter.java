package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.ComponentViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

public class ComponentAdapter extends RecyclerView.Adapter<ComponentViewHolder> {

    private List<Component> componentList;
    private OnItemListener  onItemListener;

    public ComponentAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public ComponentViewHolder onCreateViewHolder(ViewGroup parent,
                                                  int viewType) {
        return new ComponentViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ComponentViewHolder holder, int position) {
        holder.textName.setText(componentList.get(position).getName());
        holder.btnEdit
                .setOnClickListener(view -> onItemListener.onEdit(componentList.get(holder.getAdapterPosition())));
        holder.btnDelete
                .setOnClickListener(view -> onItemListener.onDelete(componentList.get(holder.getAdapterPosition())));
    }

    @Override
    public int getItemCount() {
        if (componentList == null) {
            return 0;
        }
        return componentList.size();
    }

    public void addItems(List<Component> components) {
        this.componentList = components;
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onEdit(Component component);

        void onDelete(Component component);
    }

}
