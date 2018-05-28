package cloud.heartin.projects.jestclientusages.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * Encapsulate everything needed for uploading a document.
 */
@Builder
@Data
public class UploadDocument {
    /** _id of the document. */
    private String id;
    /** index to upload document. */
    private String index;
    /** source map with key-value pairs of data fields. */
    private Map<String, Object> source;
}
