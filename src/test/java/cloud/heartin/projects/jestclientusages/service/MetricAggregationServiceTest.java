package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cloud.heartin.projects.jestclientusages.AbstractMultiIndexTest;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MetricAggregationServiceTest extends AbstractMultiIndexTest {

    @Autowired
    private MetricAggregationService service;

    @Test
    public void countAggregationTest() throws IOException {
        double result = service.countAggregation(getIndexes(), "_id");
        assertEquals(4, result, 0);
    }

}
