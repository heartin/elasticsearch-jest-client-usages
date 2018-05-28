package cloud.heartin.projects.esjestclientdemo;

import cloud.heartin.projects.esjestclientdemo.service.*;

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
