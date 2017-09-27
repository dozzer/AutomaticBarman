package com.saienko.androidthings.barman.ui.settings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.motor.Motor;
import com.saienko.androidthings.barman.ui.settings.adapter.MotorAdapter;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:46
 */
public class MotorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public  TextView                    textName;
    public  TextView                    textPin;
    public  TextView                    motorSpeed;
    public  ImageButton                 btnDelete;
    public  Motor                       motor;
    private MotorAdapter.OnItemListener onItemListener;

    public MotorViewHolder(MotorAdapter.OnItemListener onItemListener, View v) {
        super(v);
        this.onItemListener = onItemListener;
        textName = v.findViewById(R.id.motorName);
        btnDelete = v.findViewById(R.id.btnDelete);
        textPin = v.findViewById(R.id.motorPin);
        motorSpeed = v.findViewById(R.id.motorSpeed);
        btnDelete.setOnClickListener(view -> onItemListener.onDelete(motor));
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(motor);
    }
}
