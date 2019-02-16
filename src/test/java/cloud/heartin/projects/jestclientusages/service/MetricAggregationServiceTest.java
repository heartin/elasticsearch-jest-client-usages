package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;

import static org.junit.Assert.*;

public class MetricAggregationServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private MetricAggregationService service;

    @Test
    public void countAggregationTest() throws IOException {
        double result = service.countAggregation(getIndexes(), "_id");
        assertEquals(6, result, 0);
    }

    @Test
    public void countAggregationTestWithFilter() throws IOException {
        double result = service.countAggregation(getIndexes(), "_id",
                QueryBuilders.termQuery("company", "Avengers"));
        assertEquals(5, result, 0);
    }

}
