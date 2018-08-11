package cloud.heartin.projects.jestclientusages.service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.indices.CreateIndex;
import io.searchbox.indices.DeleteIndex;
import io.searchbox.indices.IndicesExists;
import io.searchbox.indices.Refresh;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

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
        JestResult result  = this.client.execute(new CreateIndex.Builder(index).build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Create Index Request Failed:" + result.getErrorMessage());
        }
    }

    /**
     * Create an index with settings.
     * @param index index.
     * @param jsonSettings json settings.
     * @throws IOException not handled, not a great thing.
     */
    public final void createIndexFromSettings(final String index, final Object jsonSettings) throws IOException {

        final CreateIndex.Builder createIndexBuilder = new CreateIndex.Builder(index);
        if (jsonSettings != null) {
            createIndexBuilder.settings(jsonSettings);
        }

        final JestResult result = this.client.execute(createIndexBuilder.build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Create Index Request Failed:" + result.getErrorMessage());
        }
    }

    /**
     * Create an index with settings from file.
     * @param index index.
     * @param path path.
     * @throws IOException not handled, not a great thing.
     */
    public final void createIndexFromPath(final String index, final String path) throws IOException {
        final String jsonSettings = getContentsFromFile(path);
        createIndexFromSettings(index, jsonSettings);
    }

    private String getContentsFromFile(final String path) throws IOException {
        File file = ResourceUtils.getFile(path);
        return new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    }

    /**
     * Delete an index.
     * @param index index.
     * @throws IOException not handled, not a great thing.
     */
    public final void deleteIndex(final String index) throws IOException {
        JestResult result  =  client.execute(new DeleteIndex.Builder(index).build());

        if (!result.isSucceeded()) {
            throw new RuntimeException("Delete Index Request Failed:" + result.getErrorMessage());
        }
    }

    /**
     * Check if index is present.
     * @param index index.
     * @return true if index present.
     * @throws IOException not handled, not a great thing.
     */
    public final boolean checkIndex(final String index) throws IOException {
        JestResult result  =  client.execute(new IndicesExists.Builder(index).build());

        if (!result.isSucceeded()) {
            if (result.getResponseCode() == NOT_FOUND) {
                return false;
            }
            throw new RuntimeException("IndexExists Request Failed:" + result.getErrorMessage());
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
            result = client.execute(new CreateIndex.Builder(index).build());
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
            result  =  client.execute(new DeleteIndex.Builder(index).build());
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
        JestResult result;
        try {
            result  =  client.execute(new Refresh.Builder().addIndices(indexes).build());
        } catch (IOException io) {
            return false;
        }

        if (result.isSucceeded()) {
            return true;
        }

        return false;
    }
}
