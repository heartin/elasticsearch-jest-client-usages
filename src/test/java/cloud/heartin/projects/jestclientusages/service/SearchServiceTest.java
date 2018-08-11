package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonArray;

import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;
import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;
import io.searchbox.client.JestClient;
import io.searchbox.core.MultiSearchResult;
import io.searchbox.core.Search;

import static org.junit.Assert.*;

public class SearchServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private SearchService service;

    @Autowired
    private JestClient client;

    @Test
    public void matchQueryTest() throws IOException {
        JsonArray result = service.matchQuery(getIndexes(), "age", "45", 10);
        assertEquals(4, result.size());
    }

    @Test
    public void matchQueryAllIndexTest() throws IOException {
        JsonArray result = service.matchQueryAllIndex("age", "45", 10);
        assertEquals(4, result.size());
    }

    @Test
    public void multiSearchTest() throws IOException {

        List<Search> searches = new LinkedList<>();

        QueryBuilder query = QueryBuilders.matchPhraseQuery("name", "Iron Man");
        SearchSourceBuilder searchSourceBuilder = JestDemoUtils.createSearchSourceBuilder(query, 10);
        Search search = new Search.Builder(searchSourceBuilder.toString().replaceAll("\\n|\\r", ""))
                .addIndex(getIndexes().get(0))
                .addType("_doc")
                .build();
        searches.add(search);

        query = QueryBuilders.matchPhraseQuery("name", "Super Man");
        searchSourceBuilder = JestDemoUtils.createSearchSourceBuilder(query, 10);
        search = new Search.Builder(searchSourceBuilder.toString().replaceAll("\\n|\\r", ""))
                .addIndex(getIndexes().get(0))
                .addType("_doc")
                .build();
        searches.add(search);

        List<MultiSearchResult.MultiSearchResponse> response = service.multiSearch(searches);
        assertEquals(2, response.size());

        response.forEach(r -> assertEquals((long) r.searchResult.getTotal(), 1));

    }

}
