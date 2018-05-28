package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import com.google.gson.JsonArray;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cloud.heartin.projects.jestclientusages.AbstractMultiIndexTest;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchServiceTest extends AbstractMultiIndexTest {

    @Autowired
    private SearchService service;

    @Test
    public void matchQueryTest() throws IOException {
        JsonArray result = service.matchQuery(getIndexes(), "age", "25", 10);
        assertEquals(2, result.size());
    }

}
