package cloud.heartin.projects.jestclientusages.utils;

import java.io.IOException;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

/**
 * Util class for Jest Demo.
 */
public final class JestDemoUtils {

    private JestDemoUtils() {

    }

    /**
     * Generic executeSearch that accept SearchSourceBuilder.
     * @param client - JestClient
     * @param indexes - Indexes
     * @param searchSource - source
     * @return SearchResult with result.
     * @throws IOException not handled
     */
    public static SearchResult executeSearch(
            final JestClient client,
            final List<String> indexes,
            final SearchSourceBuilder searchSource)
            throws IOException {
        final Search.Builder builder = new Search.Builder(searchSource.toString());
        indexes.forEach(
            index -> {
                builder.addIndex(index);
            }
        );
        SearchResult result = client.execute(builder.build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Search Request Failed:" + result.getErrorMessage());
        }

        return result;
    }

    /**
     * Create SearchSourceBuilder from QueryBuilder.
     * @param query {@link QueryBuilder}
     * @param size to limit response items.
     * @return {@link SearchSourceBuilder}
     */
    public static SearchSourceBuilder createSearchSourceBuilder(final QueryBuilder query, final int size) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(query).size(size);

        return searchSourceBuilder;
    }

    /**
     * Get JsonString from QueryBuilder.
     * @param queryBuilder {@link QueryBuilder}.
     * @return JsonString
     * @throws IOException Need to handle.
     */
    public static String getJsonStringFromQueryBuilder(final QueryBuilder queryBuilder) throws IOException {

        return queryBuilder.toXContent(XContentFactory.jsonBuilder(), ToXContent.EMPTY_PARAMS).prettyPrint().string();
    }

    /**
     * Create SearchSourceBuilder.
     * @param aggregation aggregation builder.
     * @param size size.
     * @return SearchSourceBuilder.
     */
    public static SearchSourceBuilder createSearchSourceBuilder(
            final AggregationBuilder aggregation, final int size) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.aggregation(aggregation);
        searchSourceBuilder.size(size);
        return searchSourceBuilder;
    }

}
