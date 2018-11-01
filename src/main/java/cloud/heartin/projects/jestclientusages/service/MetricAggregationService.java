package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.SearchResult;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.filter.FilterAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;

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
     * Count Aggregation.
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

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .aggregation(aggregation)
                .size(0);

        final SearchResult result = JestDemoUtils.executeSearch(
                client, indexes, searchSourceBuilder);

        return result.getAggregations()
            .getValueCountAggregation(AGG_NAME)
            .getValueCount();
    }

    /**
     * Count Aggregation within filter.
     * @param indexes - Indexes.
     * @param field - Field to find Average.
     * @param filter - Query filter.
     * @return Average.
     * @throws IOException not handled, not a great thing.
     */
    public final Long countAggregation(final List<String> indexes, final String field,
            final QueryBuilder filter) throws IOException {

        final AggregationBuilder subAggregation = AggregationBuilders.count(AGG_NAME).field(field);

        final FilterAggregationBuilder aggregation = AggregationBuilders.filter("Filter", filter)
                .subAggregation(subAggregation);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .aggregation(aggregation)
                .size(0);

        final SearchResult result = JestDemoUtils.executeSearch(
                client, indexes, searchSourceBuilder);

        return result.getAggregations()
                .getFilterAggregation("Filter")
                .getValueCountAggregation(AGG_NAME)
                .getValueCount();
    }

}
