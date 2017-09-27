package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.position.Position;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;
import com.saienko.androidthings.barman.ui.settings.adapter.PositionAdapter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingsPositionsActivity extends BaseListActivity {

    private static final int REG_CODE_ADD_POSITION  = 32;
    private static final int REG_CODE_EDIT_POSITION = 33;
    private PositionAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_positions);
        initUI();
        getPositionList();
    }


    @Override
    public void initUI() {
        super.initUI();
        initRecyclerView(findViewById(R.id.list));
    }


    private void getPositionList() {
        Single.fromCallable(DatabaseUtil::getPositions).subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(positions -> getAdapter().addItems(positions), this::showErrorSnackBar);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REG_CODE_ADD_POSITION) {
                Position position = data.getParcelableExtra(AddPositionActivity.EXTRA_POSITION);
                savePosition(position);
            }
            if (requestCode == REG_CODE_EDIT_POSITION) {
                Position position = data.getParcelableExtra(AddPositionActivity.EXTRA_POSITION);
                updatePosition(position);
            }
        }
    }

    private void savePosition(Position position) {
        Completable.fromCallable(() -> {
            // TODO: 9/27/17 adb shell monkey UNIQUE constraint failed: Position.id (code 1555)
            AppDatabase.getDb().positionDao().insert(position);
            getPositionList();
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    private void updatePosition(Position position) {
        Completable.fromCallable(() -> {
            AppDatabase.getDb().positionDao().update(position);
            getPositionList();
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    @Override
    protected void removeAll() {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().positionDao().clear();
                    getPositionList();
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    @Override
    protected PositionAdapter getAdapter() {
        if (adapter == null) {
            adapter = new PositionAdapter(new PositionAdapter.OnItemListener() {
                @Override
                public void onDelete(Position position) {
                    showDeleteDialog(position);
                }

                @Override
                public void onItemClick(Position position) {
                    editPosition(position);
                }
            });
        }
        return adapter;
    }

    private void editPosition(Position position) {
        if (position.getComponent() != null) {
            AddPositionActivity.edit(SettingsPositionsActivity.this, position, REG_CODE_EDIT_POSITION);
        } else {
            AddPositionActivity.add(SettingsPositionsActivity.this, position, REG_CODE_ADD_POSITION);
        }
    }

    private void showDeleteDialog(Position position) {
        CustomDialog.showDialog(this,
                                "Are you sure ?",
                                "Do you want to delete this item ?",
                                (dialogInterface, i) -> deleteItem(position));
    }

    private void deleteItem(Position position) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().positionDao().delete(position);
                    getPositionList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }
}
