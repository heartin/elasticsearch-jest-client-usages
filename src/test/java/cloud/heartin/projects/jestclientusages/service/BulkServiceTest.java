package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;

import static org.junit.Assert.*;

public class BulkServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private SearchService searchService;


    @Test
    public void bulkUploadTest() throws IOException {

        // Bulk upload is done from TestParentWithDataMultiIndex

        JsonArray result = searchService.matchAllQuery(getIndexes(), 10);
        assertEquals(6, result.size());
    }

}
