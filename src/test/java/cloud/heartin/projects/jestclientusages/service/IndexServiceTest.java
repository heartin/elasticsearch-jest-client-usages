package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IndexServiceTest {

    static final String INDEX = "employee";

    @Autowired
    private IndexService service;

    @Test
    public void createIndexTest() throws IOException {
        service.createIndex(INDEX);
        assertTrue(service.checkIndex(INDEX));
    }

    @Test
    public void deleteIndexTest() throws IOException {
        // Create an Index for testing delete.
        service.createIndexSilently(INDEX);

        service.deleteIndex(INDEX);
        assertFalse(service.checkIndex(INDEX));
    }

    @Test
    public void checkIndexTest() throws IOException {
        service.createIndexSilently(INDEX);
        Boolean result = service.checkIndex(INDEX);
        assertTrue(result);

        service.deleteIndexSilently(INDEX);
        result = service.checkIndex(INDEX);
        assertFalse(result);

    }
}
