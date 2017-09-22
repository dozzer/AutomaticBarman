package com.saienko.androidthings.barman.ui.settings;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;

public class SettingsBottlesActivity extends BaseListActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_bottles);
        initUI();
    }

    @Override
    public void initUI() {
        super.initUI();
//        initRecyclerView();
    }

    @Override
    protected void removeAll() {
        // TODO: 9/11/17  
    }

    @Override
    protected RecyclerView.Adapter getAdapter() {
        return null;
    }
}
