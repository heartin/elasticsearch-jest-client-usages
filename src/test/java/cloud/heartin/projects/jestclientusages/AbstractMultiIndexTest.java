package cloud.heartin.projects.jestclientusages;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.service.BulkService;
import cloud.heartin.projects.jestclientusages.service.IndexService;

import lombok.Getter;

public class AbstractMultiIndexTest {

    private static final String INDEX1 = "index1";
    private static final String INDEX2 = "index2";

    @Getter
    private List<String> indexes;

    @Autowired
    private BulkService bulkService;

    @Autowired
    private IndexService indexService;

    @Before
    public void createIndexes() throws IOException {
        indexes = new LinkedList<>();
        indexes.add(INDEX1);
        indexes.add(INDEX2);
        bulkService.bulkUpload(TestData.generateUploadDocumentList(INDEX1));
        bulkService.bulkUpload(TestData.generateUploadDocumentList(INDEX2));

        // Bulk insert may take some time to complete. This is a bad hack.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @After
    public void cleanup() throws IOException {
        indexService.deleteIndexSilently(INDEX1);
        indexService.deleteIndexSilently(INDEX2);
    }
}
