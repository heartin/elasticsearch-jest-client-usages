package cloud.heartin.projects.esjestclientdemo.utils;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.search.builder.SearchSourceBuilder;

import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Util class for Jest Demo.
 */
public final class JestDemoUtils {

    private JestDemoUtils() {

    }

    /**
     * Generic executeSearch that accept SearchSourceBuilder.
     * @param client - JestClient
     * @param index - Index
     * @param searchSource - source
     * @return SearchResult with result.
     * @throws IOException not handled
     */
    public static SearchResult executeSearch(
            final JestClient client,
            final String index,
            final SearchSourceBuilder searchSource)
            throws IOException {
        Search search = new Search.Builder(searchSource.toString())
                            // multiple index can be added.
                            .addIndex(index)
                            .build();

        SearchResult result = client.execute(search);

        if (!result.isSucceeded()) {
            throw new RuntimeException("Search Request Failed:"
                    + result.getErrorMessage());
        }

        return result;
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
        final Search.Builder builder =
                new Search.Builder(searchSource.toString());
        indexes.forEach(
            index -> {
                builder.addIndex(index);
            }
        );
        SearchResult result = client.execute(builder.build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Search Request Failed:"
                    + result.getErrorMessage());
        }

        return result;
    }
}
