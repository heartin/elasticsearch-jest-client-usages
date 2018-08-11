package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.Map;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;

import static org.junit.Assert.*;

public class BucketAggregationServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private BucketAggregationService service;

    @Test
    public final void avgAggregationTest() throws IOException {
        Map<String, Long> map =
                service.termsAggregationBucketCounts(getIndexes(), "age");
        assertEquals(4, (long) map.get("45"));
    }


    @Test
    public final void avgAggregationTestWithFilter() throws IOException {
        Map<String, Long> map =
                service.termsAggregationBucketCounts(getIndexes(), "age",
                        QueryBuilders.termQuery("company", "avengers"));
        assertEquals(3, (long) map.get("45"));
    }

}
