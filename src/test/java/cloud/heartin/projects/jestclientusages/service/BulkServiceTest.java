package cloud.heartin.projects.esjestclientdemo.service;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cloud.heartin.projects.esjestclientdemo.AbstractMultiIndexTest;
import cloud.heartin.projects.esjestclientdemo.domain.UploadDocument;
import cloud.heartin.projects.esjestclientdemo.TestData;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BulkServiceTest extends AbstractMultiIndexTest {

    @Autowired
    private BulkService service;

    @Autowired
    private SearchService searchService;

    @Autowired
    private IndexService indexService;


    @Test
    public void bulkUploadTest() throws IOException {

        List<UploadDocument> documents =
                TestData.generateUploadDocumentList(getIndexes().get(0));
        documents.addAll(TestData.generateUploadDocumentList(getIndexes().get(1)));

        service.bulkUpload(documents);

        // Bulk insert may take some time to complete. This is a bad hack.
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        JsonArray result = searchService.matchAllQuery(getIndexes(), 10);
        assertEquals(4, result.size());
    }

}
