package com.saienko.androidthings.barman.ui.adapter

import com.saienko.androidthings.barman.db.entity.Cocktail

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 11/10/17
 * Time: 00:15
 */
interface OnItemListener {
    fun onStart(cocktail: Cocktail)
}
