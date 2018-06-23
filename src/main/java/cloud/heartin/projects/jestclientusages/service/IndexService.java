package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Refresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * Index services.
 */
@Service
public class IndexService {

    private final JestClient client;

    private static final int NOT_FOUND = 404;

    @Autowired
    IndexService(final JestClient client) {
        this.client = client;
    }

    /**
     * Create an index.
     * @param index index.
     * @throws IOException not handled, not a great thing.
     */
    public final void createIndex(final String index) throws IOException {
        JestResult result  = client.execute(
                new CreateIndex.Builder(index).build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Create Index Request Failed:"
                    + result.getErrorMessage());
        }
    }

    /**
     * Delete an index.
     * @param index index.
     * @throws IOException not handled, not a great thing.
     */
    public final void deleteIndex(final String index) throws IOException {
        JestResult result  =  client.execute(
                new DeleteIndex.Builder(index).build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Delete Index Request Failed:"
                    + result.getErrorMessage());
        }
    }

    /**
     * Check if index is present.
     * @param index index.
     * @return true if index present.
     * @throws IOException not handled, not a great thing.
     */
    public final boolean checkIndex(final String index) throws IOException {
        JestResult result  =  client.execute(
                new IndicesExists.Builder(index).build());

        if (!result.isSucceeded()) {
            if (result.getResponseCode() == NOT_FOUND) {
                return false;
            }
            throw new RuntimeException("IndexExists Request Failed:"
                    + result.getErrorMessage());
        }

        return true;
    }

    /**
     * Delete index silently. Currently used for testing purpose.
     * @param index index.
     * @return true if success.
     */
     public final boolean createIndexSilently(final String index) {
        JestResult result;

        try {
            result = client.execute(
                    new CreateIndex.Builder(index).build());
        } catch (IOException io) {
            return false;
        }


        if (result.isSucceeded()) {
            return true;
        }
        return false;
    }

    /**
     * Delete index silently. Currently used for testing purpose.
     * @param index index.
     * @return true if success.
     */
    public final boolean deleteIndexSilently(final String index) {
        JestResult result = null;
        try {
            result  =  client.execute(
                    new DeleteIndex.Builder(index).build());
        } catch (IOException io) {
            return false;
        }

        if (result.isSucceeded()) {
            return true;
        }

        return false;
    }

    /**
     * Refresh index.
     * Currently used for testing purpose to reflect changes immediately.
     * Bulk API also has a refresh field, but doesn't seem to work good.
     * @param indexes indexes.
     * @return true if success.
     */
    public final boolean refresh(final List<String> indexes) {
        JestResult result = null;
        try {
            result  =  client.execute(
                    new Refresh.Builder().addIndices(indexes).build());
        } catch (IOException io) {
            return false;
        }

        if (result.isSucceeded()) {
            return true;
        }

        return false;
    }
}
