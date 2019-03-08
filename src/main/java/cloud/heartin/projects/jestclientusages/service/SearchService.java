package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import io.searchbox.client.JestClient;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.MultiSearch;
import io.searchbox.core.MultiSearchResult;
import io.searchbox.core.Search;

import lombok.extern.slf4j.Slf4j;

/**
 * Multi Index Search Service.
 */
@Slf4j
@Service
public class SearchService {

    private final Gson gson;
    private final JestClient client;

    @Autowired
    SearchService(final JestClient client) {
        this.client = client;
        this.gson = JestHttpClient.class.cast(client).getGson();
    }

    /**
     * Match Query.
     * @param indexes - Index
     * @param key - Key
     * @param value - Value
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray matchQuery(
            final List<String> indexes,
            final String key,
            final String value,
            final int size) throws IOException {
        final QueryBuilder query = QueryBuilders.matchQuery(key, value);
        return JestDemoUtils
                   .executeSearch(client, indexes,
                           JestDemoUtils.createSearchSourceBuilder(query, size))
                   .getJsonObject()
                   .getAsJsonObject("hits")
                   .getAsJsonArray("hits");

    }

    /**
     * Nested Match Query.
     * @param indexes - Index
     * @param path - path.
     * @param scoreMode score mode.
     * @param key - Key
     * @param value - Value
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray nestedQuery(
            final List<String> indexes,
            final String path,
            final ScoreMode scoreMode,
            final String key,
            final String value,
            final int size) throws IOException {
        final QueryBuilder query = QueryBuilders
                .nestedQuery(path,
                        QueryBuilders.matchQuery(key, value),
                        scoreMode);
                //.query();
        return JestDemoUtils
                .executeSearch(client, indexes,
                        JestDemoUtils.createSearchSourceBuilder(query, size))
                .getJsonObject()
                .getAsJsonObject("hits")
                .getAsJsonArray("hits");

    }

    /**
     * Match Query.
     * @param key - Key
     * @param value - Value
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray matchQueryAllIndex(
            final String key,
            final String value,
            final int size) throws IOException {
        final QueryBuilder query = QueryBuilders.matchQuery(key, value);
        return JestDemoUtils
                .executeSearch(client, Arrays.asList("_all"),
                        JestDemoUtils.createSearchSourceBuilder(query, size))
                .getJsonObject()
                .getAsJsonObject("hits")
                .getAsJsonArray("hits");

    }

    /**
     * Match Query.
     * @param indexes - Indexes
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray matchAllQuery(final List<String> indexes, final int size) throws IOException {
        final QueryBuilder query = QueryBuilders.matchAllQuery();
        return JestDemoUtils
                   .executeSearch(client, indexes,
                           JestDemoUtils.createSearchSourceBuilder(query, size))
                   .getJsonObject()
                   .getAsJsonObject("hits")
                   .getAsJsonArray("hits");
    }

    /**
     * Match Query.
     * @param searches - List of search objects
     * @return List of {@link MultiSearchResult.MultiSearchResponse}
     * @throws IOException not handled, not a great thing.
     */
    public final List<MultiSearchResult.MultiSearchResponse> multiSearch(
            final List<Search> searches) throws IOException {

        final MultiSearch multiSearch = new MultiSearch.Builder(searches).build();

        final MultiSearchResult result = this.client.execute(multiSearch);

        if (!result.isSucceeded()) {
            log.error("Failed to execute multi-search [{}]: {}",
                    multiSearch.getData(this.gson), result.getErrorMessage());
            throw new RuntimeException(result.getErrorMessage());
        }
        if (result.getResponses().stream().filter(r -> r.isError).count() > 0) {
            log.error("One or more of the searches failed for multi-search[{}]. Results: {}",
                    multiSearch.getData(this.gson), result);
        }

        final List<MultiSearchResult.MultiSearchResponse> multiSearchResponses = result.getResponses();
        Assert.isTrue((multiSearchResponses.size() == searches.size()),
                "Number of responses should be equal to number of requests");

        return multiSearchResponses;

    }

}
