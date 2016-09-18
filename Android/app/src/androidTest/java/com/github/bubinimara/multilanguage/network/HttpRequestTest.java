package com.github.bubinimara.multilanguage.network;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.github.bubinimara.multilanguage.model.LanguageModel;
import com.github.bubinimara.multilanguage.model.cms.CategoryCmsModel;
import com.github.bubinimara.multilanguage.model.cms.ProductCmsModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.HttpURLConnection;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.HttpUrl;
import okhttp3.mockwebserver.Dispatcher;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by davide on 9/09/16.
 */
@RunWith(AndroidJUnit4.class)
public class HttpRequestTest {
    private static final String TAG = HttpRequestTest.class.getSimpleName();

    private static final String RESP_JSON_LANGUAGE ="{" +
            "  \"languages\": [" +
            "    {" +
            "      \"id\": 123," +
            "      \"name\": \"mock language 1\"," +
            "      \"isoCode\": \"es_ES\"," +
            "      \"isDefault\": 1" +
            "    }," +
            "    {" +
            "      \"id\": 456," +
            "      \"name\": \"mock language 2\"," +
            "      \"isoCode\": \"en_ES\"," +
            "      \"isDefault\": 0" +
            "    }" +
            "  ]" +
            "}";
    
    private static final String RESP_JSON_CATEGORIES = "{" +
            "  \"categories\": [" +
            "    {" +
            "      \"id\": 1," +
            "      \"priority\": 1," +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"category 1\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"categoria 1\"" +
            "        }" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"id\": 2," +
            "      \"priority\": 2," +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"category 2\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"categoria 2\"        " +
            "        }" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"id\": 3," +
            "      \"priority\": 3," +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"category 3\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"categoria 3\"" +
            "        }" +
            "      ]" +
            "    }" +
            "  ]" +
            "}";
    
    private static final String RESP_JSON_PRODUCTS = "{" +
            "  \"products\": [" +
            "    {" +
            "      \"id\": 1," +
            "      \"category\": 1," +
            "      \"priority\": 1," +
            "      \"image\": \"http://domain.com/image.jpg\"," +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"product 1\"," +
            "          \"description\": \"description 1\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"prodotto 1\"," +
            "          \"description\": \"descrizione 1\"" +
            "        }" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"id\": 2," +
            "      \"category\": 1," +
            "      \"priority\": 2," +
            "      \"image\": \"http://domain.com/image.jpg\"," +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"product 2\"," +
            "          \"description\": \"description 2\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"prodotto 2\"," +
            "          \"description\": \"descrizione 2\"" +
            "        }" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"id\": 3," +
            "      \"category\": 2," +
            "      \"priority\": 1," +
            "      \"image\": \"http://domain.com/image.jpg\",      " +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"product 3\"," +
            "          \"description\": \"description 3\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"prodotto 3\"," +
            "          \"description\": \"descrizione 3\"" +
            "        }" +
            "      ]" +
            "    }," +
            "    {" +
            "      \"id\": 4," +
            "      \"category\": 2," +
            "      \"priority\": 2," +
            "      \"image\": \"http://domain.com/image.jpg\"," +
            "      \"languages\": [" +
            "        {" +
            "          \"id\": 123," +
            "          \"name\": \"product 4\"," +
            "          \"description\": \"description 4\"" +
            "        }," +
            "        {" +
            "          \"id\": 456," +
            "          \"name\": \"prodotto 4\"," +
            "          \"description\": \"descrizione 4\"" +
            "        }" +
            "      ]" +
            "    }" +
            "  ]" +
            "}";

    MockWebServer server = new MockWebServer();

    final Dispatcher dispatcher = new Dispatcher() {

        @Override
        public MockResponse dispatch(RecordedRequest request) throws InterruptedException {
            assertThat(request.getHeader("X-MyApp-id"),is(HttpRequests.ANDROID_ID));
            if (request.getPath().endsWith(HttpRequests.Queries.LANGUAGES)){
                return new MockResponse().setResponseCode(200).setBody(RESP_JSON_LANGUAGE);
            } else if (request.getPath().endsWith(HttpRequests.Queries.CATEGORIES)){
                return new MockResponse().setResponseCode(200).setBody(RESP_JSON_CATEGORIES);
            } else if (request.getPath().endsWith(HttpRequests.Queries.PRODUCTS)) {
                return new MockResponse().setResponseCode(200).setBody(RESP_JSON_PRODUCTS);
            }

            return new MockResponse().setResponseCode(404);
        }
    };

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Before
    public void setUp() throws Exception {
        server.start();
        server.setDispatcher(dispatcher);
        HttpRequests.init(InstrumentationRegistry.getContext(),server.url("/").toString());
    }

    @After
    public void tearDown() throws Exception {
        server.shutdown();
    }


    @Test
    public void testGetLanguages() throws Exception {

        ArrayList<LanguageModel> response = HttpRequests.getLanguages();
        assertNotNull(response);
        assertThat(response.size(),is(2));
        assertThat(response.get(0).getId(),is(123));
        assertThat(response.get(0).getName(),is("mock language 1"));
        assertThat(response.get(0).getIsDefault(),is(1));

    }

    @Test
    public void testGetCategories() throws Exception {
        ArrayList<CategoryCmsModel> response = HttpRequests.getCategories();
        assertNotNull(response);
        assertThat(response.size(),is(3));
        // element 1
        assertThat(response.get(0).getId(),is(1));
        assertThat(response.get(0).getPriority(),is(1));
        assertThat(response.get(0).getLanguages()[0].getId(),is(123));
        assertThat(response.get(0).getLanguages()[0].getName(),is("category 1"));
        assertThat(response.get(0).getLanguages()[1].getId(),is(456));
        assertThat(response.get(0).getLanguages()[1].getName(),is("categoria 1"));
        // element 2
        assertThat(response.get(1).getId(),is(2));
        assertThat(response.get(1).getPriority(),is(2));
        assertThat(response.get(1).getLanguages()[0].getId(),is(123));
        assertThat(response.get(1).getLanguages()[0].getName(),is("category 2"));
        assertThat(response.get(1).getLanguages()[1].getId(),is(456));
        assertThat(response.get(1).getLanguages()[1].getName(),is("categoria 2"));
        // element 3
        assertThat(response.get(2).getId(),is(3));
        assertThat(response.get(2).getPriority(),is(3));
        assertThat(response.get(2).getLanguages()[0].getId(),is(123));
        assertThat(response.get(2).getLanguages()[0].getName(),is("category 3"));
        assertThat(response.get(2).getLanguages()[1].getId(),is(456));
        assertThat(response.get(2).getLanguages()[1].getName(),is("categoria 3"));
    }

    @Test
    public void testGetProducts() throws Exception {
        ArrayList<ProductCmsModel> response = HttpRequests.getProducts();
        assertNotNull(response);
        assertThat(response.size(),is(4));

        // element 1
        assertThat(response.get(0).getId(),is(1));
        assertThat(response.get(0).getPriority(),is(1));
        assertThat(response.get(0).getCategory(),is(1));
        assertThat(response.get(0).getImage(),is("http://domain.com/image.jpg"));
        assertThat(response.get(0).getLanguages()[0].getId(),is(123));
        assertThat(response.get(0).getLanguages()[0].getName(),is("product 1"));
        assertThat(response.get(0).getLanguages()[0].getDescription(),is("description 1"));
        assertThat(response.get(0).getLanguages()[1].getId(),is(456));
        assertThat(response.get(0).getLanguages()[1].getName(),is("prodotto 1"));
        assertThat(response.get(0).getLanguages()[1].getDescription(),is("descrizione 1"));
        // element 2
        assertThat(response.get(1).getId(),is(2));
        assertThat(response.get(1).getPriority(),is(2));
        assertThat(response.get(1).getCategory(),is(1));
        assertThat(response.get(1).getImage(),is("http://domain.com/image.jpg"));
        assertThat(response.get(1).getLanguages()[0].getId(),is(123));
        assertThat(response.get(1).getLanguages()[0].getName(),is("product 2"));
        assertThat(response.get(1).getLanguages()[0].getDescription(),is("description 2"));
        assertThat(response.get(1).getLanguages()[1].getId(),is(456));
        assertThat(response.get(1).getLanguages()[1].getName(),is("prodotto 2"));
        assertThat(response.get(1).getLanguages()[1].getDescription(),is("descrizione 2"));
        // element 3
        assertThat(response.get(2).getId(),is(3));
        assertThat(response.get(2).getPriority(),is(1));
        assertThat(response.get(2).getCategory(),is(2));
        assertThat(response.get(2).getImage(),is("http://domain.com/image.jpg"));
        assertThat(response.get(2).getLanguages()[0].getId(),is(123));
        assertThat(response.get(2).getLanguages()[0].getName(),is("product 3"));
        assertThat(response.get(2).getLanguages()[0].getDescription(),is("description 3"));
        assertThat(response.get(2).getLanguages()[1].getId(),is(456));
        assertThat(response.get(2).getLanguages()[1].getName(),is("prodotto 3"));
        assertThat(response.get(2).getLanguages()[1].getDescription(),is("descrizione 3"));
        // element 4
        assertThat(response.get(3).getId(),is(4));
        assertThat(response.get(3).getPriority(),is(2));
        assertThat(response.get(3).getCategory(),is(2));
        assertThat(response.get(3).getImage(),is("http://domain.com/image.jpg"));
        assertThat(response.get(3).getLanguages()[0].getId(),is(123));
        assertThat(response.get(3).getLanguages()[0].getName(),is("product 4"));
        assertThat(response.get(3).getLanguages()[0].getDescription(),is("description 4"));
        assertThat(response.get(3).getLanguages()[1].getId(),is(456));
        assertThat(response.get(3).getLanguages()[1].getName(),is("prodotto 4"));
        assertThat(response.get(3).getLanguages()[1].getDescription(),is("descrizione 4"));


    }
}
