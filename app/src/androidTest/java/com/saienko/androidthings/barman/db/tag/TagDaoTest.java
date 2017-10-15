package com.saienko.androidthings.barman.db.tag;

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
 * Date: 10/15/17
 * Time: 22:19
 */
public class TagDaoTest {

    private AppDatabase mDatabase;

//    @Before
//    public void setUp() throws Exception {
//    }

    @Before
    public void initDb() throws Exception {
        mDatabase = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                AppDatabase.class)
                        .build();
    }

    @After
    public void closeDb() throws Exception {
        mDatabase.close();
    }

    @Test
    public void getById() throws Exception {
        TagDao tagDao = mDatabase.tagDao();
        tagDao.insert(new TagElement("tag1"));

        TagElement tag = tagDao.getById(1).blockingGet();
        assertNotNull(tag);
        assertEquals("tag1", tag.getTagName());
    }

    @Test
    public void getAll() throws Exception {
        TagDao tagDao = mDatabase.tagDao();
        createThreeItems(tagDao);
        checkCount(3);
    }

    private void createThreeItems(TagDao tagDao) {
        tagDao.insert(new TagElement("tag1"));
        tagDao.insert(new TagElement("tag2"));
        tagDao.insert(new TagElement("tag3"));
    }

    @Test
    public void insert() throws Exception {
        TagDao tagDao = mDatabase.tagDao();
        tagDao.insert(new TagElement("tag1"));

        checkCount(1);
        tagDao.insert(new TagElement("tag2"));
        checkCount(2);

        // not unique
        tagDao.insert(new TagElement("tag2"));
        checkCount(2);
    }

    @Test
    public void delete() throws Exception {
        TagDao tagDao = mDatabase.tagDao();
        long   id     = tagDao.insert(new TagElement("tag1"));
        tagDao.delete(id);
        checkCount(0);
    }

    @Test
    public void deleteObject() throws Exception {
        TagDao     tagDao = mDatabase.tagDao();
        long       id     = tagDao.insert(new TagElement("tag1"));
        TagElement tag    = tagDao.getById(id).blockingGet();
        tagDao.delete(tag);
        checkCount(0);
    }

    @Test
    public void clear() throws Exception {
        TagDao tagDao = mDatabase.tagDao();
        createThreeItems(tagDao);
        tagDao.clear();
    }

    @Test
    public void update() throws Exception {
        TagDao     tagDao = mDatabase.tagDao();
        long       id     = tagDao.insert(new TagElement("tag1"));
        TagElement tag    = tagDao.getById(id).blockingGet();
        tag.setTagName("tag2");
        tagDao.update(tag);

        tag = tagDao.getById(id).blockingGet();
        assertEquals("tag2", tag.getTagName());
    }

    private void checkCount(int count) {
        TagDao           tagDao = mDatabase.tagDao();
        List<TagElement> tags   = tagDao.getAll().blockingGet();
        assertEquals(tags.size(), count);
    }
}