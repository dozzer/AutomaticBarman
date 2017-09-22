package com.saienko.androidthings.barman.ui.dialog;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 9/10/17
 * Time: 00:59
 */

public interface OnSaveMotorListener {

    void onMotorSaved(String name, int motorSpeed, int gpioId);
}
