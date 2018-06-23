package cloud.heartin.projects.jestclientusages.domain;

import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

/**
 * Parameters to pass to
 * {@link cloud.heartin.projects.jestclientusages.service.AliasService}.
 */
@Builder
@Data
public class AliasParams {
    private String aliasName;
    private List<String> indexes;
    private String routing;
    private Map<String, Object> filter;
}
