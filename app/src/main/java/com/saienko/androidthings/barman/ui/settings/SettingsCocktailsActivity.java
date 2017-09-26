package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.AppDatabase;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.ui.base.BaseListActivity;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;
import com.saienko.androidthings.barman.ui.settings.adapter.SettingsCocktailAdapter;
import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SettingsCocktailsActivity extends BaseListActivity {

    private static final int REG_CODE_ADD_COCKTAIL  = 23;
    private static final int REG_CODE_EDIT_COCKTAIL = 24;

    private SettingsCocktailAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);
        initUI();
        getCocktailList();
    }

    @Override
    public void initUI() {
        super.initUI();
        FloatingActionButton fabAddDrink = findViewById(R.id.fabAdd);
        fabAddDrink.setOnClickListener(view -> showDialogAddGroup());

        initRecyclerView(findViewById(R.id.list));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REG_CODE_ADD_COCKTAIL) {

                Cocktail cocktail = data.getParcelableExtra(AddCocktailActivity.EXTRA_COCKTAIL_NEW);
                saveCocktail(cocktail);

            } else if (requestCode == REG_CODE_EDIT_COCKTAIL) {
                Cocktail cocktail = data.getParcelableExtra(AddCocktailActivity.EXTRA_COCKTAIL_UPDATE);
                updateCocktail(cocktail);
            }
        }
    }

    protected void removeAll() {
        Completable
                .fromCallable(() -> {
                    AppDatabase.getDb().cocktailDao().clear();
                    getCocktailList();
                    return null;
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    private void showDialogAddGroup() {

        Single.fromCallable(DatabaseUtil::getCocktails)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(cocktailGroups -> AddCocktailActivity
                      .start(SettingsCocktailsActivity.this, REG_CODE_ADD_COCKTAIL));
    }

    private void getCocktailList() {
        Single.fromCallable(DatabaseUtil::getCocktails)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(cocktailList -> getAdapter().addItems(cocktailList), this::showErrorSnackBar);
    }

    @Override
    protected SettingsCocktailAdapter getAdapter() {
        if (adapter == null) {
            adapter = new SettingsCocktailAdapter(new SettingsCocktailAdapter.OnItemListener() {
                @Override
                public void onItemClick(Cocktail cocktail) {
                    AddCocktailActivity.start(SettingsCocktailsActivity.this, cocktail, REG_CODE_EDIT_COCKTAIL);
                }

                @Override
                public void onDelete(Cocktail cocktail) {
                    showDeleteDialog(cocktail);
                }
            });
        }
        return adapter;
    }

    private void showDeleteDialog(Cocktail cocktail) {
        CustomDialog.showDialog(this, "Are you sure ?", "Do you want to delete this item ?",
                                (dialogInterface, i) -> deleteItem(cocktail));
    }

    private void deleteItem(Cocktail cocktail) {
        Completable.fromCallable(() -> {
            DatabaseUtil.delete(cocktail);
            getCocktailList();
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    private void updateCocktail(Cocktail cocktail) {
        Completable.fromCallable(() -> {
            DatabaseUtil.update(cocktail);
            getCocktailList();
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }

    private void saveCocktail(Cocktail cocktail) {

        Completable.fromCallable(() -> {
            DatabaseUtil.save(cocktail);
            getCocktailList();
            return null;
        }).subscribeOn(Schedulers.io())
                   .observeOn(AndroidSchedulers.mainThread())
                   .subscribe();
    }
}