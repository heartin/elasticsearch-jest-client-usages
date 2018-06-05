package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.AbstractMultiIndexTest;

import static org.junit.Assert.*;

/*
Data is setup as mentioned in https://cloudmaterials.com/en/recipe/recipes-elasticsearch-bulk-api-batch-updates-elastic-cloud
 */
public class BucketAggregationServiceTest extends AbstractMultiIndexTest {

    @Autowired
    private BucketAggregationService service;

    @Test
    public final void avgAggregationTest() throws IOException {
        Map<String, Long> map =
                service.termsAggregationBucketCounts(getIndexes(), "age");
        assertEquals(2, (long) map.get("25"));
    }

}
