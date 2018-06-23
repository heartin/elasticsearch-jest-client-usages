package cloud.heartin.projects.jestclientusages;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import cloud.heartin.projects.jestclientusages.service.*;

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
