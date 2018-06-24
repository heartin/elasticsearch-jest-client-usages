package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;

/**
 * Multi Index Bucket Aggregation Service.
 */
@Service
public class BucketAggregationService {

    private static final String TERMS_AGG_NAME = "state_terms";

    private final JestClient client;

    @Autowired
    BucketAggregationService(final JestClient client) {
        this.client = client;
    }

    /**
     * Match Query.
     * @param indexes - Index.
     * @param field - Field to find Average.
     * @return Average.
     * @throws IOException not handled, not a great thing.
     */
    public final Map<String, Long> termsAggregationBucketCounts(final List<String> indexes, final String field)
            throws IOException {
        final AggregationBuilder aggregation = AggregationBuilders.terms(TERMS_AGG_NAME).field(field);
        final SearchResult result =
            JestDemoUtils.executeSearch(
                client,
                indexes,
                createSearchSourceBuilder(aggregation));

        final Map<String, Long> mapKeyToCount = new HashMap<>();

        result.getAggregations()
            .getTermsAggregation(TERMS_AGG_NAME)
            .getBuckets()
            .forEach(
                e -> mapKeyToCount.put(e.getKey(), e.getCount())
            );

        return mapKeyToCount;
    }

    private SearchSourceBuilder createSearchSourceBuilder(final AggregationBuilder aggregation) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregation);
        return searchSourceBuilder;
    }

}
