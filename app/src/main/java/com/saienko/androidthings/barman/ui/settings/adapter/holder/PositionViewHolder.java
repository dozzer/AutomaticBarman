package com.saienko.androidthings.barman.ui.settings.adapter.holder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.ui.settings.adapter.PositionAdapter;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/26/17
 * Time: 22:46
 */
public class PositionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public  TextView                       motorName;
    public  TextView                       componentName;
    public  Position                       position;
    private PositionAdapter.OnItemListener onItemListener;

    public PositionViewHolder(PositionAdapter.OnItemListener onItemListener, View v) {
        super(v);
        this.onItemListener = onItemListener;
        motorName = v.findViewById(R.id.motorName);
        componentName = v.findViewById(R.id.component);
        v.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemListener.onItemClick(position);
    }
}
