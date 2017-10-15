package com.saienko.androidthings.barman.db.component;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import com.saienko.androidthings.barman.db.AppDatabase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created
 * User: Vasiliy Saienko
 * Date: 10/16/17
 * Time: 01:22
 */
public class ComponentDaoTest {
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
        ComponentDao componentDao = mDatabase.componentDao();
        createThreeItems(componentDao);
        checkCount(3);
    }

    @Test
    public void insertAll() throws Exception {
        ComponentDao componentDao = mDatabase.componentDao();
        createThreeItems(componentDao);
        checkCount(3);
    }

    @Test
    public void insert() throws Exception {
        ComponentDao componentDao = mDatabase.componentDao();
        componentDao.insert(new Component("component1"));

        checkCount(1);
        componentDao.insert(new Component("component2"));
        checkCount(2);

        // not unique
        componentDao.insert(new Component("component2"));
        checkCount(2);
    }

    @Test
    public void delete() throws Exception {
        ComponentDao componentDao = mDatabase.componentDao();
        long         id           = componentDao.insert(new Component("component1"));
        componentDao.delete(id);
        checkCount(0);
    }

    @Test
    public void clear() throws Exception {
        ComponentDao componentDao = mDatabase.componentDao();
        createThreeItems(componentDao);
        componentDao.clear();
        checkCount(0);
    }

    @Test
    public void update() throws Exception {
    }

    @Test
    public void getById() throws Exception {
        ComponentDao componentDao = mDatabase.componentDao();
        componentDao.insert(new Component("component1"));

        Component component = componentDao.getById(1).blockingGet();
        assertNotNull(component);
        assertEquals("component1", component.getName());
    }

    @Test
    public void getAllFree() throws Exception {
        // TODO: 10/16/17
    }

    private void createThreeItems(ComponentDao componentDao) {
        componentDao.insert(new Component("component1"));
        componentDao.insert(new Component("component2"));
        componentDao.insert(new Component("component3"));
    }

    private void checkCount(int count) {
        ComponentDao    componentDao = mDatabase.componentDao();
        List<Component> tags         = componentDao.getAll().blockingGet();
        assertEquals(tags.size(), count);
    }
}