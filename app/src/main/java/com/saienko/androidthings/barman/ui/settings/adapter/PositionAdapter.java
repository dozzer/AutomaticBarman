package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.PositionViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/12/17
 * Time: 12:10
 */

public class PositionAdapter extends RecyclerView.Adapter<PositionViewHolder> {

    private List<Position> positionList;
    private OnItemListener onItemListener;

    public PositionAdapter(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public PositionViewHolder onCreateViewHolder(ViewGroup parent,
                                                 int viewType) {
        return new PositionViewHolder(onItemListener,
                                      LayoutInflater.from(parent.getContext())
                                                    .inflate(R.layout.recycler_item_position, parent, false));
    }

    @Override
    public void onBindViewHolder(PositionViewHolder holder, int position) {
        Position item = positionList.get(position);
        if (item.getMotor() != null) {
            holder.motorName.setText(item.getMotor().getMotorName());
        } else {
            holder.motorName.setText("No motor");
        }
        if (item.getComponent() != null) {
            holder.componentName.setText(String.valueOf(item.getComponent().getName()));
        } else {
            holder.componentName.setText("No component");
        }
        holder.position = item;
    }

    @Override
    public int getItemCount() {
        if (positionList == null) {
            return 0;
        }
        return positionList.size();
    }

    public void addItems(List<Position> positionList) {
        this.positionList = positionList;
        notifyDataSetChanged();
    }

    public interface OnItemListener {
        void onDelete(Position position);

        void onItemClick(Position position);
    }
}

