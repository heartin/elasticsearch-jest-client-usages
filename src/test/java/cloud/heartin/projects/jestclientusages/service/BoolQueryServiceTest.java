package cloud.heartin.projects.jestclientusages.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import org.elasticsearch.index.query.QueryBuilders;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.TestParentWithDataMultiIndex;
import cloud.heartin.projects.jestclientusages.domain.BoolQueryParams;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class BoolQueryServiceTest extends TestParentWithDataMultiIndex {

    @Autowired
    private BoolQueryService service;


    @Test
    public void testWithSingleFilterMustShouldMustNot() {

        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .filterQueries(Arrays.asList(
                        QueryBuilders.termQuery("company", "Avengers")))
                .mustQueries(Arrays.asList(
                        QueryBuilders.termQuery("age", "45")))
                .shouldQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "*Man*")))
                .mustNotQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "Ant*")))
                .build();

        JsonArray result = service.boolQuery(params, 10);
        // should query is only used for scoring, not filtering as we have filter and/or must clauses.
        assertThat(extractNames(result), containsInAnyOrder("Iron Man", "Hulk"));
        assertEquals(2, result.size());
    }

    @Test
    public void testWithSingleShouldMustNot() {
        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .shouldQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "*Man*")))
                .mustNotQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "Ant*")))
                .build();

        JsonArray result = service.boolQuery(params, 10);
        // should query is only used for filtering as we do not have filter and/or must clauses.
        // spider man comes twice as it is part of two indexes used.
        assertThat(extractNames(result), containsInAnyOrder("Iron Man", "Spider Man", "Spider Man", "Super Man"));
        assertEquals(4, result.size());
    }

    @Test
    public void testWithSingleFilterMustShouldMustNotInFilterContext() {

        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .filterQueries(Arrays.asList(
                        QueryBuilders.termQuery("company", "Avengers")))
                .mustQueries(Arrays.asList(
                        QueryBuilders.termQuery("age", "45")))
                .shouldQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "*Man*")))
                .mustNotQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "Ant*")))
                .build();

        JsonArray result = service.boolQueryFilterContext(params, 10);
        // should query is used for filtering even with filter and/or must clauses,
        // if run from a filter context.
        assertThat(extractNames(result), containsInAnyOrder("Iron Man"));
        assertEquals(1, result.size());
    }

    @Test
    public void testWithSingleShouldQuery() {
        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .shouldQueries(Arrays.asList(
                        QueryBuilders.termQuery("age", "45")))
                .build();

        JsonArray result = service.boolQuery(params, 10);

        // should query is used for filtering as we do not have filter and/or must clauses.
        assertThat(extractNames(result), containsInAnyOrder("Super Man", "Iron Man", "Ant Man", "Hulk"));
        assertEquals(4, result.size());
    }

    @Test
    public void testWithMultipleFilterQueries() {
        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .filterQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "S*"),
                        QueryBuilders.termQuery("age", "30")))
                .build();

        JsonArray result = service.boolQuery(params, 10);
        // multiple filter query works as a 'AND' operation.
        assertThat(extractNames(result), containsInAnyOrder("Spider Man", "Spider Man"));
        assertEquals(2, result.size());
    }

    @Test
    public void testWithMultipleMustQueries() {
        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .mustQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "S*"),
                        QueryBuilders.termQuery("age", "30")))
                .build();

        JsonArray result = service.boolQuery(params, 10);

        // multiple must query works as a 'AND' operation.
        assertThat(extractNames(result), containsInAnyOrder("Spider Man", "Spider Man"));
        assertEquals(2, result.size());
    }

    @Test
    public void testWithMultipleShouldQueries() {
        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .shouldQueries(Arrays.asList(
                        QueryBuilders.wildcardQuery("name", "S*"),
                        QueryBuilders.termQuery("age", "30")))
                .build();

        JsonArray result = service.boolQuery(params, 10);

        // multiple should query works as a 'OR' operation.
        assertThat(extractNames(result), containsInAnyOrder(
                "Super Man", "Spider Man", "Spider Man"));
        assertEquals(3, result.size());
    }

    @Test
    public void testWithFilterQueryAndMatchAll() {
        final BoolQueryParams params = BoolQueryParams.builder()
                .indexes(getIndexes())
                .filterQueries(Arrays.asList(
                        QueryBuilders.termQuery("age", "30")))
                .mustQueries(Arrays.asList(
                        QueryBuilders.matchAllQuery()
                ))
                .build();

        JsonArray result = service.boolQuery(params, 10);
        // multiple filter query works as a 'AND' operation.
        assertThat(extractNames(result), containsInAnyOrder("Spider Man", "Spider Man"));
        assertEquals(2, result.size());
    }

    private List<String> extractNames(final JsonArray result) {
        final List<String> names = new ArrayList<>();

        for (JsonElement j: result) {
            names.add(j.getAsJsonObject().getAsJsonObject("_source").get("name").getAsString());
        }

        return names;
    }
}
