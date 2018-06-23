package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import com.google.gson.JsonArray;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import cloud.heartin.projects.jestclientusages.domain.BoolQueryParams;
import cloud.heartin.projects.jestclientusages.utils.JestDemoUtils;
import io.searchbox.client.JestClient;

import lombok.extern.slf4j.Slf4j;

/**
 * Bool Query Usages.
 */
@Slf4j
@Service
public class BoolQueryService {

    private final JestClient client;

    @Autowired
    BoolQueryService(final JestClient client) {
        this.client = client;
    }

    /**
     * Bool query in query context.
     * @param params {@link BoolQueryParams}
     * @param size to limit response items.
     * @return JsonArray response.
     */
    public final JsonArray boolQuery(
            final BoolQueryParams params, final int size) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        final Optional<List<QueryBuilder>> filterQueries =
                Optional.ofNullable(params.getFilterQueries());
        filterQueries.ifPresent(f -> f.forEach(
                queryBuilder -> boolQueryBuilder.filter(queryBuilder)));

        final Optional<List<QueryBuilder>> mustQueries =
                Optional.ofNullable(params.getMustQueries());
        mustQueries.ifPresent(m -> m.forEach(
                queryBuilder -> boolQueryBuilder.must(queryBuilder)));

        final Optional<List<QueryBuilder>> shouldQueries =
                Optional.ofNullable(params.getShouldQueries());
        shouldQueries.ifPresent(s -> s.forEach(
                queryBuilder -> boolQueryBuilder.should(queryBuilder)));

        final Optional<List<QueryBuilder>> mustNotQueries =
                Optional.ofNullable(params.getMustNotQueries());
        mustNotQueries.ifPresent(m -> m.forEach(
                queryBuilder -> boolQueryBuilder.mustNot(queryBuilder)));

        try {
            return JestDemoUtils
                    .executeSearch(client, params.getIndexes(),
                            JestDemoUtils.createSearchSourceBuilder(
                                    boolQueryBuilder, size))
                    .getJsonObject()
                    .getAsJsonObject("hits")
                    .getAsJsonArray("hits");
        } catch (IOException e) {
            log.error("Error executing bool query: {}", e.getMessage());
        }

        return null;
    }

    /**
     * Bool query in filter context.
     * @param params {@link BoolQueryParams}
     * @param size to limit response items.
     * @return JsonArray response.
     */
    public final JsonArray boolQueryFilterContext(
            final BoolQueryParams params, final int size) {

        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();

        BoolQueryBuilder subBoolQueryBuilder = QueryBuilders.boolQuery();

        final Optional<List<QueryBuilder>> filterQueries =
                Optional.ofNullable(params.getFilterQueries());
        filterQueries.ifPresent(f -> f.forEach(
                queryBuilder -> subBoolQueryBuilder.filter(queryBuilder)));

        final Optional<List<QueryBuilder>> mustQueries =
                Optional.ofNullable(params.getMustQueries());
        mustQueries.ifPresent(m -> m.forEach(
                queryBuilder -> subBoolQueryBuilder.must(queryBuilder)));

        final Optional<List<QueryBuilder>> shouldQueries =
                Optional.ofNullable(params.getShouldQueries());
        shouldQueries.ifPresent(s -> s.forEach(
                queryBuilder -> subBoolQueryBuilder.should(queryBuilder)));

        final Optional<List<QueryBuilder>> mustNotQueries =
                Optional.ofNullable(params.getMustNotQueries());
        mustNotQueries.ifPresent(m -> m.forEach(
                queryBuilder -> subBoolQueryBuilder.mustNot(queryBuilder)));

        boolQueryBuilder.filter(subBoolQueryBuilder);

        // Verifying is we are running bool query within filter context.
        try {
            Assert.isTrue(
                    JestDemoUtils
                            .getJsonStringFromQueryBuilder(boolQueryBuilder)
                            .startsWith("{\"bool\":{\"filter\":[{\"bool\":"),
                    "Not running within Filter Context.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            return JestDemoUtils
                    .executeSearch(client, params.getIndexes(),
                            JestDemoUtils.createSearchSourceBuilder(
                                    boolQueryBuilder, size))
                    .getJsonObject()
                    .getAsJsonObject("hits")
                    .getAsJsonArray("hits");
        } catch (IOException e) {
            log.error("Error executing bool query: {}", e.getMessage());
        }

        return null;
    }
}
