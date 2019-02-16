package cloud.heartin.projects.jestclientusages.service;

import cloud.heartin.projects.jestclientusages.NestedTestParent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import static org.junit.Assert.assertEquals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NestedQueryServiceTest extends NestedTestParent {

    private static final String INDEX = "bulk_test_index";
    private static final int SIZE_TOP_DOCS = 5; // Can be increased for quick performance testing.
    private static final int SIZE_INNER_DOCS = 10; // Can be increased for quick performance testing.

    @Autowired
    private MetricAggregationService metricAggregationService;

    @Autowired
    private NestedQueryService nestedService;

    @Autowired
    private UpdateService updateService;

    @Test
    public void idQueryTestWithNestedFields() throws IOException {
        final long before = System.currentTimeMillis();
        List<Map<String, Object>> result = nestedService.idQueryForNestedField(
                Arrays.asList(INDEX),
                "nested1",
                "1",
                SIZE_TOP_DOCS);
        final long after = System.currentTimeMillis();
        log.info("! Time for nestedMatchQueryTest = " + (after - before));
        assertEquals(10, result.size());
        System.out.println(result);
    }

    @Test
    public void nestedMatchQueryTest() throws IOException {
        ScoreMode scoreMode = ScoreMode.None;
        final long before = System.currentTimeMillis();
        JsonArray result = nestedService.nestedQuery(
                Arrays.asList(INDEX),
                "nested1",
                QueryBuilders.matchQuery("nested1.name", "nested_org1_2"),
                scoreMode,
                SIZE_TOP_DOCS);
        final long after = System.currentTimeMillis();
        log.info("! Time for nestedMatchQueryTest = " + (after - before));
        assertEquals(SIZE_TOP_DOCS, result.size());
    }

    @Test
    public void nestedTermQueryTest() throws IOException {
        ScoreMode scoreMode = ScoreMode.None;
        final long before = System.currentTimeMillis();
        JsonArray result = nestedService.nestedQuery(
                Arrays.asList(INDEX),
                "nested1",
                QueryBuilders.termQuery("nested1.name", "nested_org1_2"),
                scoreMode,
                SIZE_TOP_DOCS);
        final long after = System.currentTimeMillis();
        log.info("! Time for nestedTermQueryTest = " + (after - before));
        assertEquals(SIZE_TOP_DOCS, result.size());
    }

    @Test
    public void nestedTermQueryTestWithTopFilter() throws IOException {
        ScoreMode scoreMode = ScoreMode.None;
        final long before = System.currentTimeMillis();
        JsonArray result = nestedService.nestedQueryWithTopFilter(
                Arrays.asList(INDEX),
                QueryBuilders.termQuery("top1_string", "reg1"),
                "nested1",
                QueryBuilders.termQuery("nested1.name", "nested_org1_2"),
                scoreMode,
                SIZE_TOP_DOCS);
        final long after = System.currentTimeMillis();
        log.info("! Time for nestedTermQueryTest = " + (after - before));
        assertEquals(1, result.size());
    }


    @Test
    public void countAggregationWithNestedQuery() throws IOException {
        final long before = System.currentTimeMillis();
        double result = metricAggregationService.countAggregation(Arrays.asList(INDEX), "_id",
                QueryBuilders.nestedQuery("nested1",
                        QueryBuilders.termQuery("nested1.name", "nested_org1_2"),
                        ScoreMode.None));
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationWithFilterTest = " + (after - before));
        assertEquals(SIZE_TOP_DOCS, result, 0);
    }

    @Test
    public void countAggregationWithNestedQueryWithTopFilter() throws IOException {
        final long before = System.currentTimeMillis();
        double result = metricAggregationService.countAggregation(Arrays.asList(INDEX), "_id",
                QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery("top1_string", "reg1"))
                .filter(QueryBuilders.nestedQuery("nested1",
                        QueryBuilders.boolQuery()
                        .filter(QueryBuilders.termQuery("nested1.name", "nested_org1_5"))
                        .must(QueryBuilders.termQuery("nested1.value_integer", "5")),
                        ScoreMode.None)));
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationWithFilterTest = " + (after - before));
        assertEquals(1, result, 0);
    }

    @Test
    public void countAggregationNestedTest() throws IOException {
        final long before = System.currentTimeMillis();
        double result = nestedService.countAggregationNested(Arrays.asList(INDEX), "nested1", "_id");
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationNestedTest = " + (after - before));
        assertEquals((SIZE_TOP_DOCS * SIZE_INNER_DOCS), result, 0);
    }

    /* @Test
    public void avgAggregationNestedTestWithFilter() throws IOException {
        final long before = System.currentTimeMillis();
        double result = nestedService.avgAggregationNestedWithFilter(
                Arrays.asList(INDEX),
                "nested1",
                "nested1.value_int",
                QueryBuilders.termQuery("top1_string", "reg1"),
                QueryBuilders.termQuery("nested1.name", "nested_org1_7"));
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationNestedTest = " + (after - before));
        assertEquals((SIZE_TOP_DOCS * SIZE_INNER_DOCS), result, 0);
    } */

    @Test
    public void avgAggregationNestedTest() throws IOException {
        final long before = System.currentTimeMillis();
        double result = nestedService.avgAggregationNested(Arrays.asList(INDEX), "nested1", "nested1.value_integer");
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationNestedTest = " + (after - before));
        assertEquals(5, result, 0);
    }

    @Test
    public void multiGetQueryTestWithNestedFields() throws IOException {

        final String path = "nested1";
        final long before = System.currentTimeMillis();
        Map<String, Map<String, Object>> result = nestedService.multiGetQuerywithNestedField(
                INDEX,
                path,
                Arrays.asList("1"),
                "true");

        final long after = System.currentTimeMillis();
        log.info("! Time for nestedMatchQueryTest = " + (after - before));

        assertEquals(4, result.get("1").size(), 0);

        System.out.println(result);
    }

    @Test
    public void multiGetQueryTestWithNestedFieldsAndSourceFiltering() throws IOException {

        final String path = "nested1";
        final long before = System.currentTimeMillis();
        Map<String, Map<String, Object>> result = nestedService.multiGetQuerywithNestedField(
                INDEX,
                path,
                Arrays.asList("1"),
                path);

        final long after = System.currentTimeMillis();
        log.info("! Time for nestedMatchQueryTest = " + (after - before));

        // Only the nested field denoted by path and _id returned
        assertEquals(2, result.get("1").size(), 0);
    }

    @Test
    public void multiGetQueryTestWithNestedFieldsAndMultiSourceFiltering() throws IOException {

        final String path = "nested1";
        final long before = System.currentTimeMillis();
        Map<String, Map<String, Object>> result = nestedService.multiGetQuerywithNestedField(
                INDEX,
                path,
                Arrays.asList("1"),
                path + "," + "top1_string");

        final long after = System.currentTimeMillis();
        log.info("! Time for nestedMatchQueryTest = " + (after - before));

        // The nested field denoted by path, field top1_string and _id returned.
        assertEquals(3, result.get("1").size(), 0);
    }

    @Test
    public void multiGetQueryTestWithNestedFieldsAndUpdates() throws IOException {

        final String path = "nested1";
        long before = System.currentTimeMillis();
        Map<String, Map<String, Object>> result = nestedService.multiGetQuerywithNestedField(
                INDEX,
                path,
                Arrays.asList("1"),
                "true");
        long after = System.currentTimeMillis();
        log.info("! Time for nestedMatchQueryTest = " + (after - before));

        final Map<String, Object> currentDocumentMap  = result.get("1");
        final Map<String, Object> modifiedDocumentMap = new HashMap<>();

        assertEquals(4, currentDocumentMap.size(), 0);

        currentDocumentMap.keySet().forEach(k -> {
            if (k.equalsIgnoreCase(path)) {
                JsonArray array = (JsonArray) currentDocumentMap.get(k);
                final List<Map<String, String>> nestedAttributes = new ArrayList<>();
                array.forEach(v -> {
                    final JsonObject jsonObject = v.getAsJsonObject();
                    final Map<String, String> m = new HashMap<>();
                    jsonObject.entrySet().forEach((e -> m.put(e.getKey(), e.getValue().getAsString())));
                    nestedAttributes.add(m);
                });
                modifiedDocumentMap.put(path, nestedAttributes);
            } else {
                modifiedDocumentMap.put(k, currentDocumentMap.get(k));
            }
        });

        final Map<String, String> attributeTypeMap = new HashMap<>();
        attributeTypeMap.put(path, "NESTED");

        updateService.updateIndex(INDEX, "1", modifiedDocumentMap, attributeTypeMap, "nested1");

        result = nestedService.multiGetQuerywithNestedField(
                INDEX,
                path,
                Arrays.asList("1"),
                "true");

        final Map<String, Object> documentMap2  = result.get("1");
        assertEquals(4, documentMap2.size(), 0);
    }
}
