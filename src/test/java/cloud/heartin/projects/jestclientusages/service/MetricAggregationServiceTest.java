package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.AbstractMultiIndexTest;

import static org.junit.Assert.*;

public class MetricAggregationServiceTest extends AbstractMultiIndexTest {

    @Autowired
    private MetricAggregationService service;

    @Test
    public void countAggregationTest() throws IOException {
        double result = service.countAggregation(getIndexes(), "_id");
        assertEquals(4, result, 0);
    }

}
