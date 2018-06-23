package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;

import static org.junit.Assert.*;

public class SearchServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private SearchService service;

    @Test
    public void matchQueryTest() throws IOException {
        JsonArray result = service.matchQuery(getIndexes(), "age", "45", 10);
        assertEquals(4, result.size());
    }

}
