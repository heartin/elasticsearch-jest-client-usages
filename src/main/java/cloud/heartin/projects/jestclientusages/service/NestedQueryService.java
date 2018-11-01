package cloud.heartin.projects.jestclientusages.service;

import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.core.MultiGet;
import io.searchbox.core.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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
     * @param id - id.
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final List<Map<String, Object>> idQueryForNestedField(
            final List<String> indexes,
            final String path,
            final String id,
            final int size) throws IOException {

        final QueryBuilder query = QueryBuilders
                .idsQuery()
                .addIds(id);

        JsonElement val = JestDemoUtils
                .executeSearch(client, indexes,
                        JestDemoUtils.createSearchSourceBuilder(query, size))
                .getJsonObject()
                .getAsJsonObject("hits")
                .getAsJsonArray("hits")
                .get(0)
                .getAsJsonObject()
                .get("_source")
                .getAsJsonObject()
                .get(path);

        return resolveJsonElement(val);
    }

    /**
     * Nested Query with MultiGet.
     * @param index - Index
     * @param path - path.
     * @param ids - ids.
     * @param source - source path to filter. true for everything.
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final Map<String, Map<String, Object>> multiGetQuerywithNestedField(
            final String index,
            final String path,
            final List<String> ids,
            final String source) throws IOException {

        final MultiGet request = new MultiGet.Builder.ById(index, "_doc")
                .addId(ids)
                .setParameter("_source", source)
                .refresh(false)
                .build();

        final JestResult result = this.client.execute(request);
        final JsonObject resultTopJsonObj = result.getJsonObject();
        final JsonArray docs = resultTopJsonObj.getAsJsonArray("docs");
        final int docCount = docs.size();

        final Map<String, Map<String, Object>> items = new HashMap<>();
        for (int i = 0; i < docCount; i++) {

            final JsonElement doc = docs.get(i);
            final JsonObject docObj = doc.getAsJsonObject();
            final String docId = docObj.getAsJsonPrimitive("_id").getAsString();

            // This object contains all the document fields as name/value pairs.
            final JsonObject sourceObj = docObj.getAsJsonObject("_source");

            System.out.println("SOURCE=" + sourceObj);

            final Map<String, Object> fields = new HashMap<>();
            sourceObj.entrySet().stream()
                    .filter(e -> (e.getKey() != null && e.getValue() != null && !e.getValue().isJsonNull()))
                    .forEach(e -> {
                        final String key = e.getKey();

                        final Object val;
                        if (e.getKey() == path) {
                            val = resolveJsonElement(e.getValue());
                        } else {
                            val = e.getValue();
                        }

                        if (val != null) {
                            fields.put(key, val);
                        }
                    });

            // Ensure that the docId is included in the map of fields as well
            fields.put("_id", docId);
            items.computeIfAbsent(docId, m -> new HashMap<>()).putAll(fields);
        }

        return items;
    }

    private static List<Map<String, Object>> resolveJsonElement(final JsonElement val) {
        final List<Map<String, Object>> nestedObjectList = new ArrayList<>();

        val.getAsJsonArray().forEach(a -> {

            final Map<String, Object> objectMap = new HashMap<>();
            final JsonObject object = a.getAsJsonObject();
            objectMap.put("name", object.get("name").getAsString());

            final String type = object.get("type").getAsString();
            objectMap.put("type", type);

            final String valueField = "value_" + type;
            objectMap.put(valueField, object.get(valueField).getAsString());
            nestedObjectList.add(objectMap);
        });

        return nestedObjectList;
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
     * Nested Query.
     * @param indexes - Index
     * @param path - path.
     * @param topFilter top filter.
     * @param subQuery sub query.
     * @param scoreMode score mode.
     * @param size - size (default is 10)
     * @return JsonObject with result.
     * @throws IOException not handled, not a great thing.
     */
    public final JsonArray nestedQueryWithTopFilter(
            final List<String> indexes,
            final QueryBuilder topFilter,
            final String path,
            final QueryBuilder subQuery,
            final ScoreMode scoreMode,
            final int size) throws IOException {
        final QueryBuilder query = QueryBuilders
                .boolQuery()
                .filter(topFilter)
                .must(QueryBuilders.nestedQuery(path,
                        subQuery,
                        scoreMode));

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

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .aggregation(aggregation)
                .size(0);

        final SearchResult result = JestDemoUtils.executeSearch(
                client, indexes, searchSourceBuilder);

        return result.getJsonObject()
                .getAsJsonObject("aggregations")
                .getAsJsonObject("NESTED")
                .getAsJsonObject(AGG_NAME)
                .get("value")
                .getAsLong();
    }

    /**
     * Average Aggregation for Nested Fields.
     * @param indexes - Indexes.
     * @param path - path.
     * @param field - Field to find Average.
     * @return Average.
     * @throws IOException not handled, not a great thing.
     */
    public final Long avgAggregationNested(final List<String> indexes,
            final String path, final String field) throws IOException {

        final AggregationBuilder subAggregation = AggregationBuilders.avg(AGG_NAME).field(field);

        final NestedAggregationBuilder aggregation = AggregationBuilders.nested("NESTED", path)
                .subAggregation(subAggregation);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder
                .aggregation(aggregation)
                .size(0);

        final SearchResult result = JestDemoUtils.executeSearch(
                client, indexes, searchSourceBuilder);

        System.out.println(result.getJsonObject());
        return result.getJsonObject()
                .getAsJsonObject("aggregations")
                .getAsJsonObject("NESTED")
                .getAsJsonObject(AGG_NAME)
                .get("value")
                .getAsLong();
    }
}
