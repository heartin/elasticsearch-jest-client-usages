package cloud.heartin.projects.jestclientusages;

import cloud.heartin.projects.jestclientusages.service.*;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)

@Suite.SuiteClasses({
    BulkServiceTest.class,
    IndexServiceTest.class,
    BucketAggregationServiceTest.class,
    MetricAggregationServiceTest.class,
    SearchServiceTest.class,
})
public class TestSuite {

}
