package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;

import static org.junit.Assert.*;

public class BucketAggregationServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private BucketAggregationService service;

    @Test
    public final void aggregationTest() throws IOException {
        Map<String, Long> map =
                service.termsAggregationBucketCounts(getIndexes(), "age");
        assertEquals(4, (long) map.get("45"));
    }

    @Test
    public final void nestedAggregationTest() throws IOException {
        Map<String, Long> map =
                service.nestedTermsAggregationBucketCounts(getIndexes(), "_emp_custom.name");
        assertEquals(4, (long) map.get("first_movie"));
        assertEquals(3, (long) map.get("second_movie"));
    }


    @Test
    public final void aggregationTestWithFilter() throws IOException {
        Map<String, Long> map =
                service.termsAggregationBucketCounts(getIndexes(), "age",
                        QueryBuilders.termQuery("company", "Avengers"));
        assertEquals(3, (long) map.get("45"));
    }

    @Test
    public final void nestedAggregationWithFilterTest() throws IOException {
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        boolQueryBuilder.filter(QueryBuilders.termQuery("_emp_custom.name", "first_movie"));
        boolQueryBuilder.filter(QueryBuilders.prefixQuery("_emp_custom.value_keyword", "S"));

        Map<String, Long> map =
                service.nestedTermsAggregationBucketCounts(getIndexes(), "_emp_custom.name",
                        boolQueryBuilder);
        assertEquals(2, (long) map.get("first_movie"));
    }

    @Test
    public final void nestedPrefixSearchTest() throws IOException {

        List<String> suggestions =
                service.nestedPrefixSearch(getIndexes(), "first_movie",
                        "S", 10);
    }

}
