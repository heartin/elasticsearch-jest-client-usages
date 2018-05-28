package cloud.heartin.projects.esjestclientdemo.service;

import java.io.IOException;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cloud.heartin.projects.esjestclientdemo.AbstractMultiIndexTest;

import static org.junit.Assert.*;

/*
Data is setup as mentioned in https://cloudmaterials.com/en/recipe/recipes-elasticsearch-bulk-api-batch-updates-elastic-cloud
 */
@RunWith(SpringRunner.class)
@SpringBootTest
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
