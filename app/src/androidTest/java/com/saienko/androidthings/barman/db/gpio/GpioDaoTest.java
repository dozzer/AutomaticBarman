package com.saienko.androidthings.barman.db.gpio;

import android.arch.persistence.room.Room;
import android.support.annotation.NonNull;
import android.support.test.InstrumentationRegistry;
import com.saienko.androidthings.barman.db.AppDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/16/17
 * Time: 01:34
 */
public class GpioDaoTest {
    private AppDatabase mDatabase;


    @Before
    public void initDb() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                AppDatabase.class)
                        .build();
    }

    @After
    public void tearDown() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getAll() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        createThreeItems(gpioDao);
        checkCount(3);
    }

    @Test
    public void insertAll() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        createThreeItems(gpioDao);
        checkCount(3);
    }

    @Test
    public void insert() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        gpioDao.insert(getGetGpio());

        checkCount(1);
        gpioDao.insert(new Gpio(35, "GPIO_32"));
        checkCount(2);

        // not unique
        gpioDao.insert(new Gpio(35, "GPIO_32"));
        checkCount(2);
    }

    @Test
    public void delete() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        long    id      = gpioDao.insert(getGetGpio());
        gpioDao.delete(id);
        checkCount(0);
    }

    @Test
    public void clear() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        createThreeItems(gpioDao);
        gpioDao.clear();
        checkCount(0);
    }

    @Test
    public void update() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        long    id      = gpioDao.insert(getGetGpio());
        Gpio    gpio    = gpioDao.getById(id).blockingGet();
        gpio.setGpioName("bla-bla");
        gpioDao.update(gpio);

        Gpio gpio1 = gpioDao.getById(id).blockingGet();
        assertEquals("bla-bla", gpio1.getGpioName());
    }

    @Test
    public void update1() throws Exception {
        GpioDao gpioDao = mDatabase.gpioDao();
        long    id      = gpioDao.insert(getGetGpio());
        Gpio    gpio    = gpioDao.getById(id).blockingGet();
        gpio.setGpioPin(1);
        gpioDao.update(gpio);

        Gpio gpio1 = gpioDao.getById(id).blockingGet();
        assertEquals(1, gpio1.getGpioPin());
    }

    @Test
    public void getAllFree() throws Exception {
        // TODO: 10/16/17
    }

    private void createThreeItems(GpioDao gpioDao) {
        gpioDao.insert(getGetGpio());
        gpioDao.insert(new Gpio(35, "GPIO_32"));
        gpioDao.insert(new Gpio(36, "GPIO_36"));
    }

    private void checkCount(int count) {
        GpioDao    gpioDao = mDatabase.gpioDao();
        List<Gpio> gpios   = gpioDao.getAll().blockingGet();
        assertEquals(gpios.size(), count);
    }

    @NonNull
    private Gpio getGetGpio() {
        return new Gpio(15, "GPIO_10");
    }

}