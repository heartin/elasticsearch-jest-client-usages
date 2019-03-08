package cloud.heartin.projects.jestclientusages.service;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Update;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Index services.
 */
@Service
public class UpdateService {

    private final JestClient client;

    private static final int NOT_FOUND = 404;

    @Autowired
    UpdateService(final JestClient client) {
        this.client = client;
    }

    /**
     * Update an index.
     * @param index index.
     * @param id id.
     * @param documentMap document map.
     * @param attributeTypeMap attribute type map.
     * @param path nested attribute name to be used as path.
     * @throws IOException not handled, not a great thing.
     */
    public final void updateIndex(final String index, final String id, final Map<String, Object> documentMap,
            final Map<String, String> attributeTypeMap, final String path) throws IOException {

        final String json = prepareJsonForUpdate(documentMap, attributeTypeMap, path);
        final Update request = new Update.Builder(json).index(index).type("_doc").id(id).build();
        final DocumentResult result = this.client.execute(request);

        if (!result.isSucceeded()) {
            throw new RuntimeException("Update Index Request Failed:" + result.getErrorMessage());
        }
    }


    private static String prepareJsonForUpdate(final Map<String, Object> documentMap,
            final Map<String, String> attributeTypeMap, final String path) {

        final String template = "{\"doc\": {%s}}";

        final StringBuilder sb = new StringBuilder();
        documentMap.forEach((attr, val) -> {
            if (StringUtils.equals(attr, "_id") || val == null) {
                return; // skip the _id
            }

            if (StringUtils.equals(attr, path)) {
                sb.append(" \"")
                        .append(path)
                        .append("\": [");
                prepareJsonForNestedAttribute((List<Map<String, String>>) val, sb);
                sb.append("],");
                return;
            }

            if (StringUtils.equals(attr, "_id") || val == null) {
                return; // skip the _id
            }

            final String type = attributeTypeMap.get(attr);

            final String quotedStringForVal;

            if ((type == "STRING" || type == "BYTE" || type == "BINARY")) {
                quotedStringForVal = "\"";
            } else {
                quotedStringForVal = "";
            }

            sb.append("\"")
                    .append(attr)
                    .append("\": ")
                    .append(quotedStringForVal)
                    .append(val)
                    .append(quotedStringForVal)
                    .append(",");
        });

        if (sb.length() == 0) {
            return null;
        }
        sb.deleteCharAt(sb.length() - 1); // remove final comma
        return String.format(template, sb.toString());
    }

    private static String prepareJsonForNestedAttribute(final List<Map<String, String>> nestedAttributes,
            final StringBuilder sb) {

        nestedAttributes.forEach(m -> {
            sb.append("{");
            m.keySet().forEach(k -> {
                sb.append(" \"")
                        .append(k)
                        .append("\": ")
                        .append("\"")
                        .append(m.get(k))
                        .append(("\""))
                        .append(",");
            });
            sb.deleteCharAt(sb.length() - 1); // remove final comma
            sb.append("},");
        });

        if (sb.length() == 0) {
            return null;
        }
        sb.deleteCharAt(sb.length() - 1); // remove final comma
        return sb.toString();
    }
}
