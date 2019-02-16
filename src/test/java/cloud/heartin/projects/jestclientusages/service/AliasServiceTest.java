package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import com.google.gson.JsonArray;
import org.elasticsearch.common.collect.MapBuilder;
import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestData;
import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;
import cloud.heartin.projects.jestclientusages.domain.AliasParams;

import static org.junit.Assert.*;

public class AliasServiceTest extends TestParentWithDataMultiIndex {

    private static final String ALIAS_NAME_1 = "alias1";

    public static final Map<String, Object> USER_FILTER_JSON = new MapBuilder<String, Object>()
            .put("term", MapBuilder.newMapBuilder()
                    .put("company", "Avengers") //Small avengers as no analysis
                    .immutableMap())
            .immutableMap();

    @Autowired
    private AliasService aliasService;

    @Autowired
    private SearchService searchService;

    @After
    public void cleanup() {
        aliasService.dropAlias(ALIAS_NAME_1, TestData.EMPLOYEE_INDEX);
        assertFalse(aliasService.checkAlias(ALIAS_NAME_1, TestData.EMPLOYEE_INDEX));
        super.cleanup();
    }

    @Test
    public void dropAlias() {
        aliasService.dropAlias("airwatch_devicesensors", "*");
        //assertFalse(aliasService.checkAlias(ALIAS_NAME_1, TestData.EMPLOYEE_INDEX));
    }

    @Test
    public void testAliasWithoutParams() throws IOException {

        final AliasParams params = AliasParams
                .builder()
                .indexes(Arrays.asList(TestData.EMPLOYEE_INDEX))
                .aliasName(ALIAS_NAME_1)
                .build();

        aliasService.createAlias(params);
        assertTrue(aliasService.checkAlias(ALIAS_NAME_1, TestData.EMPLOYEE_INDEX));

        JsonArray result = searchService.matchQuery(
                Arrays.asList(ALIAS_NAME_1), "age", "45", 10);
        assertEquals(3, result.size());


    }

    // Ref: https://www.elastic.co/guide/en/elasticsearch/guide/current/faking-it.html
    @Test
    public void testAliasWithFiltering() throws IOException {
        final AliasParams params = AliasParams
                .builder()
                .indexes(Arrays.asList(TestData.EMPLOYEE_INDEX))
                .aliasName(ALIAS_NAME_1)
                .filter(USER_FILTER_JSON)
                .build();

        aliasService.createAlias(params);
        assertTrue(aliasService.checkAlias(ALIAS_NAME_1, TestData.EMPLOYEE_INDEX));

        // Query with only alias, filter is automatically added.
        JsonArray result = searchService.matchQuery(
                Arrays.asList(ALIAS_NAME_1), "age", "45", 10);
        assertEquals(2, result.size());

    }

    //TODO: Demonstrate the use of custom routing value with aliases.
}
