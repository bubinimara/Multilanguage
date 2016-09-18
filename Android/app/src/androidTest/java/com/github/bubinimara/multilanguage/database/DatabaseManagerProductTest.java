package com.github.bubinimara.multilanguage.database;

import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;


import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.ProductModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;
import com.github.bubinimara.multilanguage.model.cms.ProductCmsModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Created by davide on 3/08/16.
 */
@RunWith(AndroidJUnit4.class)
public class DatabaseManagerProductTest {

    private DatabaseManager databaseManager;
    private final LanguageModel mockLanguage1 = new LanguageModel(1,"mockName","mockIso",0);
    private final LanguageModel mockLanguage2 = new LanguageModel(2,"mockName","mockIso",1);

    private CategoryCmsModel mockCategory1 = new CategoryCmsModel(1,1,new CategoryCmsModel.Language[]{
            new CategoryCmsModel.Language(mockLanguage1.getId(),"Category 1"),
            new CategoryCmsModel.Language(mockLanguage2.getId(),"Categoria 1")
    });

    private CategoryCmsModel mockCategory2 = new CategoryCmsModel(2,2,new CategoryCmsModel.Language[]{
            new CategoryCmsModel.Language(mockLanguage1.getId(),"Category 2"),
            new CategoryCmsModel.Language(mockLanguage2.getId(),"Categoria 2")
    });


    private ProductCmsModel mockProduct1 = new ProductCmsModel(1,mockCategory1.getId(),1,"image 1", new ProductCmsModel.Language[]{
            new ProductCmsModel.Language(mockLanguage1.getId(),"name product 1","descritpion product 1"),
            new ProductCmsModel.Language(mockLanguage2.getId(),"nome prodotto 1","descrizione prodotto 1")
    });
    private ProductCmsModel mockProduct2 = new ProductCmsModel(2,mockCategory1.getId(),2,"image 2", new ProductCmsModel.Language[]{
            new ProductCmsModel.Language(mockLanguage1.getId(),"name product 2","descritpion product 2"),
            new ProductCmsModel.Language(mockLanguage2.getId(),"nome prodotto 2","descrizione prodotto 2")
    });
    private ProductCmsModel mockProduct3 = new ProductCmsModel(3,mockCategory2.getId(),3,"image 3", new ProductCmsModel.Language[]{
            new ProductCmsModel.Language(mockLanguage1.getId(),"name product 3","descritpion product 3"),
            new ProductCmsModel.Language(mockLanguage2.getId(),"nome prodotto 3","descrizione prodotto 3")
    });

    @Before
    public void setUp() throws Exception {
        databaseManager = new DatabaseManager(InstrumentationRegistry.getTargetContext());
        databaseManager.addLanguage(mockLanguage1); // add default language
        databaseManager.addLanguage(mockLanguage2); // add default language

        databaseManager.addCategoryModel(mockCategory1);
        databaseManager.addCategoryModel(mockCategory2);


    }

    @After
    public void tearDown() throws Exception {
        databaseManager.clearAll();

    }

    private void initTest(){
        LanguageModel dbLanguage;

        assertNotNull(databaseManager);
        int count = databaseManager.getLanguageCount();
        assertThat(2,is(count)); // 2 languages correctly inserted

        // assert language is correctly inserted
        dbLanguage= databaseManager.getLanguage(mockLanguage1.getId());
        assertThat(dbLanguage,is(mockLanguage1));
        dbLanguage= databaseManager.getLanguage(mockLanguage2.getId());
        assertThat(dbLanguage,is(mockLanguage2));

        // assert category is correctly initialize
        assertThat(2,is(databaseManager.getCategoryCount()));
        ArrayList<Integer> ids = databaseManager.getCategoryId();
        assertNotNull(ids);
        assertThat(2,is(ids.size()));
        assertThat(ids.get(0),is(mockCategory1.getId()));
        assertThat(ids.get(1),is(mockCategory2.getId()));

    }
    @Test
    public void testProductModel() throws Exception {
        ArrayList<ProductModel> result;
        initTest();

        // category test
        assertThat(0,is(databaseManager.getProductCount()));

        // populate db
        ArrayList<ProductCmsModel> products = new ArrayList<>(3);
        products.add(mockProduct1);
        products.add(mockProduct2);
        products.add(mockProduct3);
        databaseManager.addAllProductModel(products);

        assertThat(databaseManager.getProductCount(),is(3));

        // first language
        result = databaseManager.getAllProductModel(mockLanguage1.getId());
        assertNotNull(result);
        assertThat(result.size(),is(3));

        assertThat(result.get(0).getId(),is(mockProduct1.getId()));
        assertThat(result.get(1).getId(),is(mockProduct2.getId()));
        assertThat(result.get(2).getId(),is(mockProduct3.getId()));

        assertThat(result.get(0).getName(),is(mockProduct1.getLanguages()[0].getName()));
        assertThat(result.get(1).getName(),is(mockProduct2.getLanguages()[0].getName()));
        assertThat(result.get(2).getName(),is(mockProduct3.getLanguages()[0].getName()));

        // language 2
        result = databaseManager.getAllProductModel(mockLanguage2.getId());
        assertNotNull(result);
        assertThat(result.size(),is(3));

        assertThat(result.get(0).getId(),is(mockProduct1.getId()));
        assertThat(result.get(1).getId(),is(mockProduct2.getId()));
        assertThat(result.get(2).getId(),is(mockProduct3.getId()));

        assertThat(result.get(0).getName(),is(mockProduct1.getLanguages()[1].getName()));
        assertThat(result.get(1).getName(),is(mockProduct2.getLanguages()[1].getName()));
        assertThat(result.get(2).getName(),is(mockProduct3.getLanguages()[1].getName()));

        result = databaseManager.getAllProductModel(mockCategory2.getId(),mockLanguage1.getId());
        assertNotNull(result);
        assertThat(result.size(),is(1));
        assertThat(result.get(0).getId(),is(mockProduct3.getId()));
        assertThat(result.get(0).getName(),is(mockProduct3.getLanguages()[0].getName()));

        result = databaseManager.getAllProductModel(mockCategory1.getId(),mockLanguage1.getId());
        assertNotNull(result);
        assertThat(result.size(),is(2));
        assertThat(result.get(0).getId(),is(mockProduct1.getId()));
        assertThat(result.get(0).getName(),is(mockProduct1.getLanguages()[0].getName()));
        assertThat(result.get(1).getId(),is(mockProduct2.getId()));
        assertThat(result.get(1).getName(),is(mockProduct2.getLanguages()[0].getName()));


        // remove 2
        databaseManager.deleteProduct(mockCategory2.getId());
        assertThat(databaseManager.getProductCount(),is(2));

        result = databaseManager.getAllProductModel(mockLanguage2.getId());
        assertNotNull(result);
        assertThat(result.size(),is(2));

        assertThat(result.get(0).getId(),is(mockProduct1.getId()));
        assertThat(result.get(1).getId(),is(mockProduct3.getId()));

        assertThat(result.get(0).getName(),is(mockProduct1.getLanguages()[1].getName()));
        assertThat(result.get(1).getName(),is(mockProduct3.getLanguages()[1].getName()));

        assertThat(result.get(0).getDescription(),is(mockProduct1.getLanguages()[1].getDescription()));
        assertThat(result.get(1).getDescription(),is(mockProduct3.getLanguages()[1].getDescription()));


        // get single product
        ProductModel product = databaseManager.getProductModel(mockProduct1.getId(), mockLanguage1.getId());
        assertNotNull(product);
        assertThat(product.getId(),is(mockProduct1.getId()));
        assertThat(product.getName(),is(mockProduct1.getLanguages()[0].getName()));
        assertThat(product.getDescription(),is(mockProduct1.getLanguages()[0].getDescription()));



    }

    @Test
    public void testGetProductDefaultLanguage() throws Exception {
        // initial
        initTest();

        // category test
        assertThat(0,is(databaseManager.getProductCount()));

        // populate db
        ArrayList<ProductCmsModel> products = new ArrayList<>(3);
        products.add(mockProduct1);
        products.add(mockProduct2);
        products.add(mockProduct3);
        databaseManager.addAllProductModel(products);

        assertThat(databaseManager.getProductCount(),is(3));

        // remove language 1 from products
        databaseManager.deleteProductLang(mockLanguage1.getId());
        // add language 1 to product 2
        databaseManager.addProductLang(mockProduct1.getId(),mockProduct1.getLanguages()[1].getId(),mockProduct1.getLanguages()[1].getName(),mockProduct1.getLanguages()[1].getDescription());
        databaseManager.addProductLang(mockProduct2.getId(),mockProduct2.getLanguages()[0].getId(),mockProduct2.getLanguages()[0].getName(),mockProduct2.getLanguages()[0].getDescription());


        ArrayList<ProductModel> result = databaseManager.getAllProductModel(mockCategory1.getId(), mockLanguage1.getId(), mockLanguage2.getId());
        assertNotNull(result);
        assertThat(result.size(),is(2));

        assertThat(result.get(0).getId(),is(mockProduct1.getId()));
        assertThat(result.get(1).getId(),is(mockProduct2.getId()));

        assertThat(result.get(0).getName(),is(mockProduct1.getLanguages()[1].getName()));
        assertThat(result.get(1).getName(),is(mockProduct2.getLanguages()[0].getName()));

        assertThat(result.get(0).getDescription(),is(mockProduct1.getLanguages()[1].getDescription()));
        assertThat(result.get(1).getDescription(),is(mockProduct2.getLanguages()[0].getDescription()));


    }

    /**
     * test  add and remove products
     * @throws Exception
     */
    @Test
    public void testProducts() throws Exception {
        // only check product ids
        final int MOCK_PRODUCT_1 = 1;
        final int MOCK_PRODUCT_2 = 2;

        ArrayList<Integer> ids;
        initTest();

        assertThat(databaseManager.getProductCount(),is(0));

        // add mock1 and mock2 in category 1
        databaseManager.addProduct(mockCategory1.getId(),MOCK_PRODUCT_1,1,"IMAGE1");
        assertThat(databaseManager.getProductCount(),is(1));
        ids = databaseManager.getProductId();
        assertNotNull(ids);
        assertThat(ids.size(),is(1));
        assertThat(MOCK_PRODUCT_1,is(ids.get(0)));

        databaseManager.addProduct(mockCategory1.getId(),MOCK_PRODUCT_2,2,"IMAGE2");
        assertThat(2,is(databaseManager.getProductCount()));
        ids = databaseManager.getProductId();
        assertNotNull(ids);
        assertThat(2,is(ids.size()));
        assertThat(ids.get(0),is(MOCK_PRODUCT_1));
        assertThat(ids.get(1),is(MOCK_PRODUCT_2));


        // add translation to a products in language 1
        assertThat(databaseManager.getProductLangCount(),is(0));
        databaseManager.addProductLang(MOCK_PRODUCT_1,mockLanguage1.getId(),"product1_lang1","description");
        databaseManager.addProductLang(MOCK_PRODUCT_2,mockLanguage1.getId(),"product2_lang1","description");
        assertThat(databaseManager.getProductLangCount(),is(2));

        ArrayList<String> names = databaseManager.getProductLangTest(mockLanguage1.getId());
        assertNotNull(names);
        assertThat(names.size(),is(2));
        assertTrue(names.contains("product1_lang1"));
        assertTrue(names.contains("product1_lang1"));

        // add another translation
        databaseManager.addProductLang(MOCK_PRODUCT_1,mockLanguage2.getId(),"product1_lang2","description");
        assertThat(databaseManager.getProductLangCount(),is(3));
        // same test same result
        names = databaseManager.getProductLangTest(mockLanguage1.getId());
        assertNotNull(names);
        assertThat(names.size(),is(2));
        assertTrue(names.contains("product1_lang1"));
        assertTrue(names.contains("product1_lang1"));

        // different language
        names = databaseManager.getProductLangTest(mockLanguage2.getId());
        assertNotNull(names);
        assertThat(names.size(),is(1));
        assertTrue(names.contains("product1_lang2"));

        // remove all in cascade
        databaseManager.deleteLanguage(mockLanguage1.getId());
        names = databaseManager.getProductLangTest(mockLanguage1.getId());
        assertTrue(names.isEmpty());
        databaseManager.deleteProduct(MOCK_PRODUCT_1);
        names = databaseManager.getProductLangTest(mockLanguage2.getId());
        assertFalse(names.contains("product1_lang2"));
        names = databaseManager.getProductLangTest(mockLanguage1.getId());
        assertFalse(names.contains("product1_lang1"));
    }
}
