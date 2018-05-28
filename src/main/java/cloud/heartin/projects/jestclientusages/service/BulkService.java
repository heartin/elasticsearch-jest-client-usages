package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.heartin.projects.jestclientusages.domain.UploadDocument;
import io.searchbox.core.Bulk;
import io.searchbox.core.BulkResult;
import io.searchbox.core.Index;
import io.searchbox.client.JestClient;

/**
 * Bulk API.
 */
@Service
public class BulkService {

    private static final String TYPE = "_doc";

    private final JestClient client;

    @Autowired
    BulkService(final JestClient client) {
        this.client = client;
    }

    /**
     * Uploads documents.
     * @param documents documents to upload.
     * @throws IOException not handled, not
     */
    public final void bulkUpload(
            final List<UploadDocument> documents) throws IOException {

        final Bulk.Builder builder = new Bulk.Builder();

        documents.forEach(
            d -> {
                builder.addAction(new Index.Builder(d.getSource())
                        .index(d.getIndex()).id(d.getId()).type(TYPE).build());
            }
        );

        Bulk bulk = builder.build();
        BulkResult result = client.execute(bulk);

        if (!result.isSucceeded()) {
            throw new RuntimeException("Bulk request failed:"
                    + result.getErrorMessage());
        }
    }

}
