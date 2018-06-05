package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.AbstractMultiIndexTest;

import static org.junit.Assert.*;

public class SearchServiceTest extends AbstractMultiIndexTest {

    @Autowired
    private SearchService service;

    @Test
    public void matchQueryTest() throws IOException {
        JsonArray result = service.matchQuery(getIndexes(), "age", "25", 10);
        assertEquals(2, result.size());
    }

}
