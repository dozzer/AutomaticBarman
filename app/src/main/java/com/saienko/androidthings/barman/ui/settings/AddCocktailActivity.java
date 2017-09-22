package com.saienko.androidthings.barman.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.*;
import com.saienko.androidthings.barman.R;
import com.saienko.androidthings.barman.db.DatabaseUtil;
import com.saienko.androidthings.barman.db.cocktail.Cocktail;
import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.db.cocktail.Component;
import com.saienko.androidthings.barman.db.drinkGroup.CocktailGroup;
import com.saienko.androidthings.barman.ui.base.BaseActivity;
import com.saienko.androidthings.barman.ui.dialog.CustomDialog;
import com.saienko.androidthings.barman.ui.settings.adapter.CocktailElementAdapter;
import com.saienko.androidthings.barman.ui.settings.adapter.SelectGroupAdapter;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class AddCocktailActivity extends BaseActivity {

    private static final String EXTRA_COCKTAIL        = "EXTRA_COCKTAIL";
    public static final  String EXTRA_COCKTAIL_NEW    = "EXTRA_COCKTAIL_NEW";
    public static final  String EXTRA_COCKTAIL_UPDATE = "EXTRA_COCKTAIL_UPDATE";

    private boolean                isNewCocktail;
    private Cocktail               cocktail;
    private CocktailElementAdapter adapter;
    private List<CocktailGroup>    cocktails;
    private CocktailGroup          selectedGroup;
    private ImageButton            ibAdd;

    public static void start(BaseActivity activity, int regCode) {
        Intent intent = new Intent(activity, AddCocktailActivity.class);
        activity.startActivityForResult(intent, regCode);
    }

    public static void start(BaseActivity activity, Cocktail cocktail,
                             int regCode) {
        Intent intent = new Intent(activity, AddCocktailActivity.class);
        intent.putExtra(EXTRA_COCKTAIL, cocktail);
        activity.startActivityForResult(intent, regCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cocktail);
        handleIntent();
        initUI();
    }

    private void handleIntent() {
        if (getIntent().hasExtra(EXTRA_COCKTAIL)) {
            isNewCocktail = false;
            cocktail = getIntent().getParcelableExtra(EXTRA_COCKTAIL);
        } else {
            isNewCocktail = true;
        }
    }

    @Override
    public void initUI() {
        super.initUI();
        Spinner      spinnerGroup = findViewById(R.id.spinnerGroup);
        EditText     etName       = findViewById(R.id.etName);
        Button       btnSave      = findViewById(R.id.btnSave);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        ibAdd = findViewById(R.id.ibAdd);

        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(getAdapter());
        spinnerGroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectedGroup = cocktails.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // IGNORE
            }
        });


        btnSave.setOnClickListener(view -> {
            if (isNewCocktail) {
                if (!TextUtils.isEmpty(etName.getText().toString())) {
                    Intent intent = new Intent();
                    cocktail = new Cocktail(etName.getText().toString(),
                                            selectedGroup == null ? -1 : selectedGroup.getId());
                    if (getAdapter().getItems() != null && !getAdapter().getItems().isEmpty()) {
                        cocktail.setCocktailElements(getAdapter().getItems());
                        intent.putExtra(EXTRA_COCKTAIL_NEW, cocktail);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {
                        showSnackBar("Component list can't be empty");
                    }
                } else {
                    showSnackBar("Speed can't be empty");
                }
            } else {
                if (getAdapter().getItems() == null || getAdapter().getItems().isEmpty()) {
                    showSnackBar("Component list can't be empty");
                } else if (!TextUtils.isEmpty(etName.getText().toString())) {
                    Intent intent = new Intent();
                    cocktail.setCocktailName(etName.getText().toString());
                    cocktail.setCocktailGroup(selectedGroup);
                    cocktail.setCocktailElements(getAdapter().getItems());
                    intent.putExtra(EXTRA_COCKTAIL_UPDATE, cocktail);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    showSnackBar("Speed can't be empty");
                }
            }
        });

        Single.fromCallable(DatabaseUtil::getCocktailGroups).subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread())
              .subscribe(cocktailGroups -> {
                  AddCocktailActivity.this.cocktails = cocktailGroups;
                  spinnerGroup.setAdapter(new SelectGroupAdapter(AddCocktailActivity.this, cocktailGroups));
                  if (!isNewCocktail) {
                      spinnerGroup.setSelection(getPosition(cocktailGroups, cocktail.getCocktailGroupId()));
                  }
              });

        ibAdd.setOnClickListener(view -> Single
                .fromCallable(() -> DatabaseUtil.getFreeComponents(getExistComponentIds()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::openAddComponentDialog,
                           this::showErrorSnackBar));
        if (!isNewCocktail) {
            etName.setText(cocktail.getCocktailName());
            getAdapter().addAll(cocktail.getCocktailElements());
        }

        setAddButtonVisibility();
    }

    private void setAddButtonVisibility() {
        Single
                .fromCallable(() -> DatabaseUtil.getFreeComponents(getExistComponentIds()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(components -> ibAdd.setVisibility(components.isEmpty() ? View.GONE : View.VISIBLE),
                           this::showErrorSnackBar);
    }

    private long[] getExistComponentIds() {
        long[] componentIds = new long[getAdapter().getItemCount()];
        for (int i = 0; i < getAdapter().getItemCount(); i++) {
            componentIds[i] = getAdapter().getItems().get(i).getComponent().getId();
        }
        return componentIds;
    }

    private void openAddComponentDialog(
            List<Component> components) {
        CustomDialog.addNewCocktailElement(this, components, (component, volume) -> {
            setAddButtonVisibility();
            addComponent(component, volume);
        });
    }

    private void addComponent(Component component, int volume) {
        CocktailElement cocktailElement = new CocktailElement(component.getId(), volume, -1);
        cocktailElement.setComponent(component);
        getAdapter().add(cocktailElement);
    }

    CocktailElementAdapter getAdapter() {
        if (adapter == null) {
            adapter = new CocktailElementAdapter(cocktailElement -> new CocktailElementAdapter.OnItemListener() {
                @Override
                public void onDelete(CocktailElement cocktailElement) {
                    getAdapter().delete(cocktailElement);
                    setAddButtonVisibility();
                }
            });
        }
        return adapter;
    }

    private int getPosition(List<CocktailGroup> groupList, long groupId) {
        for (int i = 0; i < groupList.size(); i++) {
            if (groupList.get(i).getId() == groupId) {
                return i;
            }
        }
        return 0;
    }
}
