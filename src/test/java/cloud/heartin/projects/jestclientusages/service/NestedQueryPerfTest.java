package cloud.heartin.projects.jestclientusages.service;

import cloud.heartin.projects.jestclientusages.TestData;
import cloud.heartin.projects.jestclientusages.TestParent;
import io.searchbox.core.BulkResult;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

/**
 * This is not a test, but a utility method to setup and cleanup data manually.
 */
@Slf4j
public class NestedQueryPerfTest extends TestParent {

    private static final String INDEX = "bulk_test_index";
    private static final String MAPPINGS = "nested-mappings.json";
    private static final int BATCH_SIZE = 100;
    private static final int MAX_DOCS = 500; // MAX_DOCS % BATCH_SIZE should be 0.
    private static final int START_DOC = 1; // (START_DOC + BATCH_SIZE - 1) % BATCH_SIZE should be 0.
    private static final int SIZE_INNER_DOCS = 10; // Can be increased for quick performance testing.

    @Autowired
    private NestedQueryService nestedService;

    @Autowired
    private BulkService bulkService;

    @Autowired
    private IndexService indexService;

    /**
     * Setup data in index. Need to manually cleanup.
     * @throws IOException in case of any exception.
     */
    @Test
    public void createIndexes() throws IOException {

        indexService.createIndexFromPath(INDEX, "classpath:" + MAPPINGS);

        final int setCount = MAX_DOCS / BATCH_SIZE;

        log.info("Number of sets is: " + setCount);

        final int startBatch = (START_DOC + BATCH_SIZE - 1) / BATCH_SIZE;

        for (int i = startBatch; i <= setCount; i++) {
            int from = (i - 1) * BATCH_SIZE + 1;
            int to = i * BATCH_SIZE;

            BulkResult result = null;
            try {
                result = bulkService.bulkUpload(
                        TestData.generateTestDocumentsWithNestedObject(INDEX, from, to, SIZE_INNER_DOCS, "org1"), true);
            } catch (Exception e) {
                log.error(e.getMessage());
                //log.error(result.getFailedItems().get(0).errorReason);

            }

            // To reflect changes immediately. The refresh property of BulkUpload is not guaranteed to help.
            indexService.refresh(Arrays.asList(INDEX));

            log.info("Updated from " + from + " to " + to);

        }

        double result = nestedService.countAggregationNested(Arrays.asList(INDEX), "nested1", "_id");
    }


    /**
     * Cleanup data in index.
     * @throws IOException in case of any exception.
     */
    @Test
    public void cleanup() {
        indexService.deleteIndexSilently(INDEX);
    }


}
