package com.saienko.androidthings.barman.ui.base;

import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/9/17
 * Time: 15:42
 */

public abstract class BaseListActivity extends BaseActivity {
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lists, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        int i = item.getItemId();
        if (i == R.id.action_remove_all) {
            showClearDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showClearDialog() {
        CustomDialog.showDialog(this, "Are you sure ?", "Do you want to clear all records ?",
                                (dialogInterface, i) -> removeAll());
    }

    protected abstract void removeAll();

    protected void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(getAdapter());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                                                                                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    protected abstract RecyclerView.Adapter getAdapter();
}
