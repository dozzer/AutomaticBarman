package com.saienko.androidthings.barman.ui.dialog;

import com.saienko.androidthings.barman.db.cocktail.CocktailElement;
import com.saienko.androidthings.barman.db.cocktail.Component;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/11/17
 * Time: 20:02
 */

public interface OnAddComponentListener {
    void onComponentAdded(Component component, int volume);

    void onComponentEdit(CocktailElement cocktailElement, int volume);
}
