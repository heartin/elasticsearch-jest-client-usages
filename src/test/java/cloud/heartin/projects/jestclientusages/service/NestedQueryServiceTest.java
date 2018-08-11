package cloud.heartin.projects.jestclientusages.service;

import cloud.heartin.projects.jestclientusages.TestData;
import cloud.heartin.projects.jestclientusages.TestParent;

import java.io.IOException;
import java.util.Arrays;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.JsonArray;

import static org.junit.Assert.assertEquals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NestedQueryServiceTest extends TestParent {

    private static final String INDEX = "bulk_test_index";
    private static final String MAPPINGS = "nested-mappings.json";
    private static final int SIZE_TOP_DOCS = 5; // Can be increased for quick performance testing.
    private static final int SIZE_INNER_DOCS = 10; // Can be increased for quick performance testing.

    @Autowired
    private MetricAggregationService metricAggregationService;

    @Autowired
    private NestedQueryService nestedService;

    @Autowired
    private BulkService bulkService;

    @Autowired
    private IndexService indexService;

    @Before
    public void createIndexes() throws IOException {

        indexService.createIndexFromPath(INDEX, "classpath:" + MAPPINGS);
        bulkService.bulkUpload(
                TestData.generateTestDocumentsWithNestedObject(INDEX, SIZE_TOP_DOCS, SIZE_INNER_DOCS), true);

        // To reflect changes immediately. The refresh property of BulkUpload is not guaranteed to help.
        indexService.refresh(Arrays.asList(INDEX));
    }

    @After
    public void cleanup() {
        indexService.deleteIndexSilently(INDEX);
    }

    @Test
    public void nestedMatchQueryTest() throws IOException {
        ScoreMode scoreMode = ScoreMode.None;
        final long before = System.currentTimeMillis();
        JsonArray result = nestedService.nestedQuery(
                Arrays.asList(INDEX),
                "inner",
                QueryBuilders.matchQuery("inner.inner_name", "inn1"),
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
                "inner",
                QueryBuilders.termQuery("inner.inner_name", "inn1"),
                scoreMode,
                SIZE_TOP_DOCS);
        final long after = System.currentTimeMillis();
        log.info("! Time for nestedTermQueryTest = " + (after - before));
        assertEquals(SIZE_TOP_DOCS, result.size());
    }

    @Test
    public void countAggregationWithNestedQuery() throws IOException {
        final long before = System.currentTimeMillis();
        double result = metricAggregationService.countAggregation(Arrays.asList(INDEX), "_id",
                QueryBuilders.nestedQuery("inner",
                        QueryBuilders.termQuery("inner.inner_name", "inn1"),
                        ScoreMode.None));
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationWithFilterTest = " + (after - before));
        assertEquals(SIZE_TOP_DOCS, result, 0);
    }

    @Test
    public void countAggregationNestedTest() throws IOException {
        final long before = System.currentTimeMillis();
        double result = nestedService.countAggregationNested(Arrays.asList(INDEX), "inner", "_id");
        final long after = System.currentTimeMillis();
        log.info("! Time taken for countAggregationNestedTest = " + (after - before));
        assertEquals((SIZE_TOP_DOCS * SIZE_INNER_DOCS), result, 0);
    }

}
