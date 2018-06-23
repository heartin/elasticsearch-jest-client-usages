package cloud.heartin.projects.jestclientusages.domain;

import java.util.List;

import org.elasticsearch.index.query.QueryBuilder;

import lombok.Builder;
import lombok.Data;

/**
 * Parameters to pass to
 * {@link cloud.heartin.projects.jestclientusages.service.BoolQueryService}.
 */
@Builder
@Data
public class BoolQueryParams {
    private List<String> indexes;
    private List<QueryBuilder> filterQueries;
    private List<QueryBuilder> mustQueries;
    private List<QueryBuilder> shouldQueries;
    private List<QueryBuilder> mustNotQueries;
}
