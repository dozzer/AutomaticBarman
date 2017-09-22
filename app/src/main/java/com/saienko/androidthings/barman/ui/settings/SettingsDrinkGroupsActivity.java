package com.saienko.androidthings.barman.ui.settings;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;
import com.saienko.androidthings.barman.ui.settings.adapter.DrinkGroupAdapter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.concurrent.Callable;

public class SettingsDrinkGroupsActivity extends BaseListActivity {

    private DrinkGroupAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        initUI();
        getDrinkGroupList();
    }

    @Override
    public void initUI() {
        super.initUI();
        FloatingActionButton fabAddDrink = findViewById(R.id.fabAdd);
        fabAddDrink.setOnClickListener(view -> showDialogAddGroup());

        initRecyclerView(findViewById(R.id.list));
    }

    private void showDialogAddGroup() {
        CustomDialog.addNewItem(this, "New Group", "Enter new group name", "",
                                this::addGroup);
    }


    protected void removeAll() {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().cocktailGroup().clear();
                    getDrinkGroupList();
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void getDrinkGroupList() {
        Single
                .fromCallable(DatabaseUtil::getCocktailGroups)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(drinkGroups -> getAdapter().addItems(drinkGroups), this::showErrorSnackBar);
    }

    @Override
    protected DrinkGroupAdapter getAdapter() {
        if (adapter == null) {
            adapter = new DrinkGroupAdapter(new DrinkGroupAdapter.OnItemListener() {
                @Override
                public void onEdit(CocktailGroup group) {
                    showDialogUpdateGroup(group);
                }

                @Override
                public void onDelete(CocktailGroup group) {
                    showDeleteDialog(group);
                }
            });
        }
        return adapter;
    }

    private void showDialogUpdateGroup(CocktailGroup group) {
        CustomDialog.addNewItem(this, "Update Group", "Update group name", group.getDrinkGroupName(),
                                text -> {
                                    group.setDrinkGroupName(text);
                                    updateGroup(group);
                                });
    }

    private void updateGroup(CocktailGroup group) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().cocktailGroup().update(group);
                    getDrinkGroupList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }


    private void showDeleteDialog(CocktailGroup group) {
        CustomDialog.showDialog(this, "Are you sure ?", "Do you want to delete this item ?",
                                (dialogInterface, i) -> deleteItem(group));
    }

    private void deleteItem(CocktailGroup group) {
        Completable.fromCallable(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                AppDatabase.getDb().cocktailGroup().delete(group);
                getDrinkGroupList();
                return null;
            }
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    private void addGroup(String name) {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().cocktailGroup().insert(new CocktailGroup(name));
                    getDrinkGroupList();
                    return null;
                }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }
}
