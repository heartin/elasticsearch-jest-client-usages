package cloud.heartin.projects.jestclientusages.service;

import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;
import io.searchbox.client.JestClient;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.SearchResult;

import java.io.IOException;
import java.util.List;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;

import lombok.extern.slf4j.Slf4j;

/**
 * Nested Query Service.
 */
@Slf4j
@Service
public class NestedQueryService {

    private final Gson gson;
    private final JestClient client;

    private static final String AGG_NAME = "ValueCount";

    @Autowired
    NestedQueryService(final JestClient client) {
        this.client = client;
        this.gson = JestHttpClient.class.cast(client).getGson();
    }

    /**
     * Nested Query.
     * @param indexes - Index
     * @param path - path.
     * @param subQuery sub query.
     * @param scoreMode score mode.
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray nestedQuery(
            final List<String> indexes,
            final String path,
            final QueryBuilder subQuery,
            final ScoreMode scoreMode,
            final int size) throws IOException {
        final QueryBuilder query = QueryBuilders
                .nestedQuery(path,
                        subQuery,
                        scoreMode);

        return JestDemoUtils
                .executeSearch(client, indexes,
                        JestDemoUtils.createSearchSourceBuilder(query, size))
                .getJsonObject()
                .getAsJsonObject("hits")
                .getAsJsonArray("hits");

    }

    /**
     * Count Aggregation for Nested Fields.
     * @param indexes - Indexes.
     * @param path - path.
     * @param field - Field to find Average.
     * @return Average.
     * @throws IOException not handled, not a great thing.
     */
    public final Long countAggregationNested(final List<String> indexes,
            final String path, final String field) throws IOException {

        final AggregationBuilder subAggregation = AggregationBuilders.count(AGG_NAME).field(field);

        final NestedAggregationBuilder aggregation = AggregationBuilders.nested("NESTED", path)
                .subAggregation(subAggregation);

        final SearchResult result = JestDemoUtils.executeSearch(
                client, indexes, JestDemoUtils.createSearchSourceBuilder(aggregation, 0));

        return result.getJsonObject()
                .getAsJsonObject("aggregations")
                .getAsJsonObject("NESTED")
                .getAsJsonObject(AGG_NAME)
                .get("value")
                .getAsLong();
    }
}
