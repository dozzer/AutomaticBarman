package com.saienko.androidthings.barman.ui.settings.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.ui.settings.adapter.holder.MotorViewHolder;

import java.util.List;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 16:10
 */

public class MotorAdapter extends RecyclerView.Adapter<MotorViewHolder> {

    private List<Motor>    motorList;
    private OnItemListener onItemListener;


    public MotorAdapter(@NonNull OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    @Override
    public MotorViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        return new MotorViewHolder(onItemListener,
                                   LayoutInflater.from(parent.getContext())
                                                 .inflate(R.layout.recycler_item_motor, parent, false));
    }

    @Override
    public void onBindViewHolder(MotorViewHolder holder, int position) {
        Motor motor = motorList.get(holder.getAdapterPosition());
        holder.textName.setText(motor.getMotorName());

        if (motor.getGpio() != null) {
            holder.textPin.setText(String.valueOf(motor.getGpio().getGpioPin()));
        }
        holder.motorSpeed.setText(String.valueOf(motor.getMotorSpeed()));
        holder.btnDelete
                .setOnClickListener(view -> onItemListener.onDelete(motor));
        holder.motor = motor;
    }

    @Override
    public int getItemCount() {
        if (motorList == null) {
            return 0;
        }
        return motorList.size();
    }

    public void addItems(List<Motor> motorList) {
        this.motorList = motorList;
        notifyDataSetChanged();
    }

    public interface OnItemListener {

        void onDelete(Motor motor);

        void onItemClick(Motor motor);
    }

}
