package cloud.heartin.projects.esjestclientdemo.service;

import cloud.heartin.projects.esjestclientdemo.utils.JestDemoUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * Multi Index Metric Aggregation Service.
 * Refer to:
 * elastic.co/guide/en/elasticsearch/client/java-api/current/
 *      _metrics_aggregations.html
 */
@Service
public class MetricAggregationService {

    private static final String AGG_NAME = "Average";

    private final JestClient client;

    @Autowired
    MetricAggregationService(final JestClient client) {
        this.client = client;
    }

    /**
     * Match Query.
     * @param indexes - Indexes.
     * @param field - Field to find Average.
     * @return Average.
     * @throws IOException not handled, not a great thing.
     */
    public final Long countAggregation(
            final List<String> indexes, final String field)
            throws IOException {
        final AggregationBuilder aggregation = AggregationBuilders
                                                   .count(AGG_NAME)
                                                   .field(field);
        final SearchResult result = JestDemoUtils.executeSearch(
                client, indexes, createSearchSourceBuilder(aggregation));

        return result.getAggregations()
            .getValueCountAggregation(AGG_NAME)
            .getValueCount();
    }

    private SearchSourceBuilder createSearchSourceBuilder(
            final AggregationBuilder aggregation) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregation);
        return searchSourceBuilder;
    }

}
