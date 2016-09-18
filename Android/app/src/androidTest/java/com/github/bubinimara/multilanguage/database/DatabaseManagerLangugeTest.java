package com.github.bubinimara.multilanguage.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.github.bubinimara.multilanguage.model.LanguageModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.AllOf.allOf;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by davide on 3/08/16.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerLangugeTest {

    private DatabaseManager databaseManager;
    private LanguageModel mockLanguage1 = new LanguageModel(2,"mockName","mockIso",2);
    private LanguageModel mockLanguage2 = new LanguageModel(3,"mock3","iso3",4);


    @Before
    public void setUp() throws Exception {
        databaseManager = new DatabaseManager(InstrumentationRegistry.getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        databaseManager.clearAll();

    }

    /*************************************************************************/
    /**************************** LANGUAGE ***********************************/
    /*************************************************************************/

    @Test
    public void testLanguage() throws Exception {
        LanguageModel dbLanguage;

        assertNotNull(databaseManager);
        int count = databaseManager.getLanguageCount();
        assertThat(count,is(0));

        // assert add item
        databaseManager.addLanguage(mockLanguage1);
        assertThat(databaseManager.getLanguageCount(),is(1));
        dbLanguage= databaseManager.getLanguage(mockLanguage1.getId());
        assertThat(dbLanguage,is(mockLanguage1));

        // insert same mock ... nothing will change
        databaseManager.addLanguage(mockLanguage1);
        assertThat(databaseManager.getLanguageCount(),is(1));
        dbLanguage= databaseManager.getLanguage(mockLanguage1.getId());
        assertThat(dbLanguage,is(mockLanguage1));

        // add new item
        databaseManager.addLanguage(mockLanguage2);

        assertThat(databaseManager.getLanguageCount(),is(2));
        dbLanguage= databaseManager.getLanguage(mockLanguage2.getId());
        assertThat(dbLanguage,is(mockLanguage2));

        ArrayList<LanguageModel> listLanguage = databaseManager.getAllLanguages();
        assertNotNull(listLanguage);
        assertThat(listLanguage.size(),is(2));
        assertTrue(listLanguage.contains(mockLanguage1));
        assertTrue(listLanguage.contains(mockLanguage2));

        // assert all info are stored correctly
        dbLanguage = databaseManager.getLanguage(mockLanguage1.getId());
        assertThat(dbLanguage,is(mockLanguage1));
        assertThat(dbLanguage.getIsDefault(),is(mockLanguage1.getIsDefault()));
        assertThat(dbLanguage.getName(),is(mockLanguage1.getName()));
        assertThat(dbLanguage.getIso(),is(mockLanguage1.getIso()));


        dbLanguage = databaseManager.getLanguage(mockLanguage2.getId());
        assertThat(dbLanguage,is(mockLanguage2));
        assertThat(dbLanguage.getIsDefault(),is(mockLanguage2.getIsDefault()));
        assertThat(dbLanguage.getName(),is(mockLanguage2.getName()));
        assertThat(dbLanguage.getIso(),is(mockLanguage2.getIso()));

        // assert delete
        count = databaseManager.deleteLanguage(mockLanguage2.getId());
        assertThat(count,is(1));
        dbLanguage = databaseManager.getLanguage(mockLanguage2.getId());
        assertNull(dbLanguage);

        // assert update
        mockLanguage2.setId(mockLanguage1.getId());
        assertThat(mockLanguage2,is(mockLanguage1));
        databaseManager.addLanguage(mockLanguage2);
        assertThat(count,is(1));
        dbLanguage = databaseManager.getLanguage(mockLanguage2.getId());
        assertThat(dbLanguage,is(mockLanguage2));
        assertThat(dbLanguage.getIsDefault(),is(mockLanguage2.getIsDefault()));
        assertThat(dbLanguage.getName(),is(mockLanguage2.getName()));
        assertThat(dbLanguage.getIso(),is(mockLanguage2.getIso()));

    }

    @Test
    public void testLanguageBulk() throws Exception {
        int count;
        assertNotNull(databaseManager);
        count = databaseManager.getLanguageCount();
        assertThat(count,is(0));

        ArrayList<LanguageModel> languageModels = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            languageModels.add(new LanguageModel(i,"name"+i,String.valueOf(i),i));
        }

        databaseManager.addLanguage(languageModels);
        count = databaseManager.getLanguageCount();
        assertThat(count,is(100));

        ArrayList<LanguageModel> list = databaseManager.getAllLanguages();
        assertNotNull(list);
        assertTrue(list.containsAll(languageModels));

    }
}
