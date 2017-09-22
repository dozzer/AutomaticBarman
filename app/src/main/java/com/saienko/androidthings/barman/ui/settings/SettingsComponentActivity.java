package com.saienko.androidthings.barman.ui.settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;
import com.saienko.androidthings.barman.ui.settings.adapter.ComponentAdapter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingsComponentActivity extends BaseListActivity {

    private ComponentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_component);
        initUI();
        getComponentList();
    }

    @Override
    public void initUI() {
        super.initUI();
        FloatingActionButton fabAddDrink = findViewById(R.id.fabAdd);
        fabAddDrink.setOnClickListener(view -> showDialogAddComponent());

        initRecyclerView(findViewById(R.id.list));
    }

    private void showDialogAddComponent() {
        CustomDialog.addNewItem(this, "New component", "Enter new Component", "",
                                this::addComponent);
    }

    @Override
    protected void removeAll() {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().componentDao().clear();
                    getComponentList();
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void getComponentList() {
        Single
                .fromCallable(DatabaseUtil::getComponents)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(componentList -> getAdapter().addItems(componentList), this::showErrorSnackBar);
    }

    @Override
    protected ComponentAdapter getAdapter() {
        if (adapter == null) {
            adapter = new ComponentAdapter(new ComponentAdapter.OnItemListener() {
                @Override
                public void onEdit(Component component) {
                    showDialogUpdateComponent(component);
                }

                @Override
                public void onDelete(Component component) {
                    showDeleteDialog(component);
                }
            });
        }
        return adapter;
    }

    private void showDialogUpdateComponent(Component component) {
        CustomDialog.addNewItem(this, "Update component", "Update Component", component.getName(),
                                text -> {
                                    component.setName(text);
                                    updateComponent(component);
                                });
    }


    private void showDeleteDialog(Component component) {
        CustomDialog.showDialog(this, "Are you sure ?", "Do you want to delete this item ?",
                                (dialogInterface, i) -> deleteItem(component));
    }

    private void deleteItem(Component component) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().componentDao().delete(component);
                    getComponentList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void addComponent(String name) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().componentDao().insert(new Component(name));
                    getComponentList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }

    private void updateComponent(Component component) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().componentDao().update(component);
                    getComponentList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
}
