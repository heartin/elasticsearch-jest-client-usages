package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.List;

import com.google.gson.JsonArray;
import io.searchbox.client.JestClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;

/**
 * Multi Index Search Service.
 */
@Service
public class SearchService {

    private final JestClient client;

    @Autowired
    SearchService(final JestClient client) {
        this.client = client;
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
     * Match Query.
     * @param indexes - Indexes
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray matchAllQuery(
            final List<String> indexes, final int size) throws IOException {
        final QueryBuilder query = QueryBuilders.matchAllQuery();
        return JestDemoUtils
                   .executeSearch(client, indexes,
                           JestDemoUtils.createSearchSourceBuilder(query, size))
                   .getJsonObject()
                   .getAsJsonObject("hits")
                   .getAsJsonArray("hits");
    }

}
