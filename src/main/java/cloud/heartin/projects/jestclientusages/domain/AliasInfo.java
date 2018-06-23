package cloud.heartin.projects.jestclientusages.domain;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Return type for GetAliases method of
 * {@link cloud.heartin.projects.jestclientusages.service.AliasService}.
 */
@Builder
@Data
public class AliasInfo {

    private String indexName;
    private String alias;
    private String indexRouting;
    private List<String> searchRouting;
    private Map<String, Object> filter;

}
