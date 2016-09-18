package com.github.bubinimara.multilanguage.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.github.bubinimara.multilanguage.model.CategoryModel;
import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by davide on 3/08/16.
 */

@RunWith(AndroidJUnit4.class)
public class DatabaseManagerCategoryTest {

    private DatabaseManager databaseManager;
    private final LanguageModel mockLanguage1 = new LanguageModel(1,"mockName","mockIso",1);
    private final LanguageModel mockLanguage2 = new LanguageModel(2,"mockName","mockIso",2);

    private CategoryCmsModel mockCategory1 = new CategoryCmsModel(1,1,new CategoryCmsModel.Language[]{
            new CategoryCmsModel.Language(mockLanguage1.getId(),"Category 1"),
            new CategoryCmsModel.Language(mockLanguage2.getId(),"Categoria 1")
    });

    private CategoryCmsModel mockCategory2 = new CategoryCmsModel(2,2,new CategoryCmsModel.Language[]{
            new CategoryCmsModel.Language(mockLanguage1.getId(),"Category 2"),
            new CategoryCmsModel.Language(mockLanguage2.getId(),"Categoria 2")
    });
    private CategoryCmsModel mockCategory3 = new CategoryCmsModel(3,2,new CategoryCmsModel.Language[]{
            new CategoryCmsModel.Language(mockLanguage1.getId(),"Category 2"),
            new CategoryCmsModel.Language(mockLanguage2.getId(),"Categoria 2")
    });




    @Before
    public void setUp() throws Exception {
        databaseManager = new DatabaseManager(InstrumentationRegistry.getTargetContext());
        databaseManager.addLanguage(mockLanguage1); // add default language
        databaseManager.addLanguage(mockLanguage2); // add default language

    }

    @After
    public void tearDown() throws Exception {
        databaseManager.clearAll();

    }

    private void initTest(){
        LanguageModel dbLanguage;

        assertNotNull(databaseManager);
        int count = databaseManager.getLanguageCount();
        assertThat(count,is(2)); // 2 languages correctly inserted

        // assert language is correctly inserted
        dbLanguage= databaseManager.getLanguage(mockLanguage1.getId());
        assertThat(mockLanguage1,is(dbLanguage));
        dbLanguage= databaseManager.getLanguage(mockLanguage2.getId());
        assertThat(mockLanguage2,is(dbLanguage));

        assertThat(databaseManager.getCategoryCount(),is(0));
    }

    /**
     * Testing simple add and delete
     * @throws Exception
     */
    @Test
    public void testCategory() throws Exception {
        ArrayList<Integer> ids;

        initTest();

        // add category 2
        databaseManager.addCategory(mockCategory2.getId(),mockCategory2.getId());
        assertThat(databaseManager.getCategoryCount(),is(1));

        ids = databaseManager.getCategoryId();
        assertNotNull(ids);
        assertThat(ids.size(),is(1));
        assertThat(ids,hasItem(mockCategory2.getId()));

        databaseManager.addCategory(mockCategory1.getId(),mockCategory1.getId());
        assertThat(databaseManager.getCategoryCount(),is(2));

        ids = databaseManager.getCategoryId();
        assertNotNull(ids);
        assertThat(ids.size(),is(2));
        // assert ordered
        assertThat(ids.get(0),is(mockCategory1.getId()));
        assertThat(ids.get(1),is(mockCategory2.getId()));

        databaseManager.deleteCategory(mockCategory2.getId());
        assertThat(databaseManager.getCategoryCount(),is(1));
        ids = databaseManager.getCategoryId();
        assertNotNull(ids);
        assertThat(ids.size(),is(1));
        assertTrue(ids.contains(mockCategory1.getId()));
        assertFalse(ids.contains(mockCategory2.getId()));
    }


    @Test
    public void testCategoryLang() throws Exception {
        initTest();

        // populate with two category
        databaseManager.addCategory(mockCategory2.getId(),mockCategory2.getPriority());
        databaseManager.addCategory(mockCategory1.getId(),mockCategory1.getPriority());

        assertThat(databaseManager.getCategoryLangCount(),is(0));

        // add 2 languages to category1
        databaseManager.addCategoryLang(mockCategory1.getId(),mockLanguage1.getId(),"mockName1"); //
        databaseManager.addCategoryLang(mockCategory1.getId(),mockLanguage2.getId(),"mockName2"); //

        assertThat(databaseManager.getCategoryLangCount(),is(2));

        // add 2 languages to category2
        databaseManager.addCategoryLang(mockCategory2.getId(),mockLanguage1.getId(),"localMockName1"); //
        databaseManager.addCategoryLang(mockCategory2.getId(),mockLanguage2.getId(),"localMockName2"); //

        assertThat(databaseManager.getCategoryLangCount(),is(4));

        ArrayList<CategoryModel> list = databaseManager.getAllCategoryModel(mockLanguage1.getId());
        assertNotNull(list);
        assertThat(list.size(),is(2));
        assertThat(list.get(0).getName(),is("mockName1"));
        assertThat(list.get(1).getName(),is("localMockName1"));

        // lang 2
        list = databaseManager.getAllCategoryModel(mockLanguage2.getId());
        assertNotNull(list);
        assertThat(list.size(),is(2));
        assertThat(list.get(0).getName(),is("mockName2"));
        assertThat(list.get(1).getName(),is("localMockName2"));

        // update lang
        databaseManager.addCategoryLang(mockCategory1.getId(),mockLanguage1.getId(),"mockNameUpdate1"); //

        list = databaseManager.getAllCategoryModel(mockLanguage1.getId());
        assertNotNull(list);
        assertThat(list.size(),is(2));
        assertThat(list.get(0).getName(),is("mockNameUpdate1"));
        assertThat(list.get(1).getName(),is("localMockName1"));

        //remove category will remove translation too
        databaseManager.deleteCategory(mockLanguage1.getId());
        assertThat(databaseManager.getCategoryCount(),is(1));
        assertThat(databaseManager.getCategoryLangCount(),is(2));

    }


    @Test
    public void testCategoryModelDefaultLang() throws Exception {
        // Init and insert
        initTest();

        // populate with two category
        databaseManager.addCategory(mockCategory2.getId(),mockCategory2.getPriority());
        databaseManager.addCategory(mockCategory1.getId(),mockCategory1.getPriority());

        assertThat(databaseManager.getCategoryLangCount(),is(0));

        // add 2 langs to category mock
        databaseManager.addCategoryLang(mockCategory1.getId(),mockLanguage1.getId(),"mockName1"); //
        databaseManager.addCategoryLang(mockCategory1.getId(),mockLanguage2.getId(),"mockName2"); //

        assertThat(databaseManager.getCategoryLangCount(),is(2));

        // add 1 langs to category localMock
        databaseManager.addCategoryLang(mockCategory2.getId(),mockLanguage2.getId(),"localMockName2"); //

        assertThat(databaseManager.getCategoryLangCount(),is(3));


        ArrayList<CategoryModel> list = databaseManager.getAllCategoryModel(mockLanguage1.getId(),mockLanguage2.getId());
        assertNotNull(list);
        assertThat(list.size(),is(2));
        assertThat(list.get(0).getName(),is("mockName1"));
        assertThat(list.get(1).getName(),is("localMockName2"));

        list = databaseManager.getAllCategoryModel(mockLanguage2.getId());
        assertNotNull(list);
        assertThat(list.size(),is(2));
        assertThat(list.get(0).getName(),is("mockName2"));
        assertThat(list.get(1).getName(),is("localMockName2"));

    }

    @Test
    public void testCmsModel() throws Exception {
        initTest();

        ArrayList<CategoryCmsModel> categories = new ArrayList<>(3);
        categories.add(mockCategory2);
        categories.add(mockCategory1);
        categories.add(mockCategory3);

        databaseManager.addAllCategoryModel(categories);

        // get language1
        ArrayList<CategoryModel> result = databaseManager.getAllCategoryModel(mockLanguage1.getId());
        assertNotNull(result);
        assertThat(result.size(),is(3));
        assertThat(result.get(0).getId(),is(mockCategory1.getId()));
        assertThat(result.get(0).getName(),is(mockCategory1.getLanguages()[0].getName()));
        assertThat(result.get(1).getId(),is(mockCategory2.getId()));
        assertThat(result.get(1).getName(),is(mockCategory2.getLanguages()[0].getName()));
        assertThat(result.get(2).getId(),is(mockCategory3.getId()));
        assertThat(result.get(2).getName(),is(mockCategory3.getLanguages()[0].getName()));

        // get language2
        result = databaseManager.getAllCategoryModel(mockLanguage2.getId());
        assertNotNull(result);
        assertThat(result.size(),is(3));

        assertThat(result.get(0).getId(),is(mockCategory1.getId()));
        assertThat(result.get(0).getName(),is(mockCategory1.getLanguages()[1].getName()));

        assertThat(result.get(1).getId(),is(mockCategory2.getId()));
        assertThat(result.get(1).getName(),is(mockCategory2.getLanguages()[1].getName()));

        assertThat(result.get(2).getId(),is(mockCategory3.getId()));
        assertThat(result.get(2).getName(),is(mockCategory3.getLanguages()[1].getName()));


        databaseManager.deleteCategory(mockCategory2.getId());

        result = databaseManager.getAllCategoryModel(mockLanguage1.getId());
        assertNotNull(result);
        assertThat(result.size(),is(2));

        assertThat(result.get(0).getId(),is(mockCategory1.getId()));
        assertThat(result.get(0).getName(),is(mockCategory1.getLanguages()[0].getName()));
        assertThat(result.get(1).getId(),is(mockCategory3.getId()));
        assertThat(result.get(1).getName(),is(mockCategory3.getLanguages()[0].getName()));

    }
}
