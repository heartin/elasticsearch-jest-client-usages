package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.AbstractParentTest;
import cloud.heartin.projects.jestclientusages.domain.UploadDocument;
import cloud.heartin.projects.jestclientusages.TestData;

import static org.junit.Assert.*;

public class BulkServiceTest extends AbstractParentTest {

    private static final String INDEX1 = "index1";
    private static final String INDEX2 = "index2";

    private static final List<String> INDEXES = new LinkedList<>();

    @Autowired
    private BulkService service;

    @Autowired
    private SearchService searchService;

    @Autowired
    private IndexService indexService;

    @Before
    public void createIndexes() {
        INDEXES.add(INDEX1);
        INDEXES.add(INDEX2);
    }

    @After
    public void cleanup() {
        indexService.deleteIndexSilently(INDEX1);
        indexService.deleteIndexSilently(INDEX2);
    }


    @Test
    public void bulkUploadTest() throws IOException {

        List<UploadDocument> documents =
                TestData.generateUploadDocumentList(INDEX1);
        documents.addAll(TestData.generateUploadDocumentList(INDEX2));

        service.bulkUpload(documents, true);

        // To reflect changes immediately.
        // The refresh property of BulkUpload is not guaranteed to help.
        indexService.refresh(INDEXES);

        JsonArray result = searchService.matchAllQuery(INDEXES, 10);
        assertEquals(4, result.size());
    }

}
