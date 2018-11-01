package cloud.heartin.projects.jestclientusages;

import cloud.heartin.projects.jestclientusages.service.BulkService;
import cloud.heartin.projects.jestclientusages.service.IndexService;

import java.io.IOException;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NestedTestParent extends TestParent {

    private static final String INDEX = "bulk_test_index";
    private static final String MAPPINGS = "nested-mappings.json";
    private static final int SIZE_TOP_DOCS = 5; // Can be increased for quick performance testing.
    private static final int SIZE_INNER_DOCS = 10; // Can be increased for quick performance testing.

    @Autowired
    private BulkService bulkService;

    @Autowired
    private IndexService indexService;

    @Before
    public void createIndexes() throws IOException {

        indexService.createIndexFromPath(INDEX, "classpath:" + MAPPINGS);
        bulkService.bulkUpload(
                TestData.generateTestDocumentsWithNestedObject(INDEX, 1, SIZE_TOP_DOCS, SIZE_INNER_DOCS, "org1"), true);

        // To reflect changes immediately. The refresh property of BulkUpload is not guaranteed to help.
        indexService.refresh(Arrays.asList(INDEX));
    }

    @After
    public void cleanup() {
        indexService.deleteIndexSilently(INDEX);
    }

}
