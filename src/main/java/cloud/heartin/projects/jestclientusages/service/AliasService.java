package cloud.heartin.projects.jestclientusages.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import io.searchbox.client.JestClient;
import io.searchbox.client.JestResult;
import io.searchbox.client.http.JestHttpClient;
import io.searchbox.indices.aliases.AddAliasMapping;
import io.searchbox.indices.aliases.AliasExists;
import io.searchbox.indices.aliases.GetAliases;
import io.searchbox.indices.aliases.ModifyAliases;
import io.searchbox.indices.aliases.RemoveAliasMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cloud.heartin.projects.jestclientusages.domain.AliasInfo;
import cloud.heartin.projects.jestclientusages.domain.AliasParams;

import lombok.extern.slf4j.Slf4j;

/**
 * Usages of Elasticsearch Aliases.
 */
@Slf4j
@Service
public class AliasService {

    private final JestClient client;
    private final Gson gson;

    @Autowired
    AliasService(final JestClient client) {
        this.client = client;
        this.gson = JestHttpClient.class.cast(client).getGson();
    }

    /**
     * Create Alias.
     * @param aliasParams {@link AliasParams}.
     * @return true if create alias successful, false otherwise.
     */
    public final boolean createAlias(final AliasParams aliasParams) {
        final AddAliasMapping.Builder aliasBuilder =
                new AddAliasMapping.Builder(
                        aliasParams.getIndexes(), aliasParams.getAliasName());

        final Optional<String> optionalRouting =
                Optional.ofNullable(aliasParams.getRouting());
        optionalRouting.ifPresent(routing -> aliasBuilder.addRouting(routing));

        final Optional<Map<String, Object>> optionalFilter =
                Optional.ofNullable(aliasParams.getFilter());
        optionalFilter.ifPresent(filter -> aliasBuilder.setFilter(filter));

        final ModifyAliases modifyAliases =
                new ModifyAliases.Builder(aliasBuilder.build()).build();

        try {
            final JestResult result = this.client.execute(modifyAliases);
            if (!result.isSucceeded()) {
                log.error("Failed to create alias [{}]: {}",
                        aliasParams.getAliasName(), result.getErrorMessage());
            }
            return result.isSucceeded();
        } catch (final IOException e) {
            log.error("Failed to create alias [{}]: {}",
                    aliasParams.getAliasName(), e);
            return false;
        }

    }

    /**
     * Drop Alias.
     * @param aliasName the alias name.
     * @param indexName the index name.
     * @return true if drop alias successful, false otherwise.
     */
    public final boolean dropAlias(
            final String aliasName, final String indexName) {
        final ModifyAliases modifyAliases = new ModifyAliases.Builder(
                new RemoveAliasMapping.Builder(indexName, aliasName)
                .build()).build();
        try {
            final JestResult result = this.client.execute(modifyAliases);
            if (!result.isSucceeded()) {
                log.error("Failed to drop alias [{}] on indexName [{}]: {}",
                        aliasName, indexName, result.getErrorMessage());
            }
            return result.isSucceeded();
        } catch (final IOException ex) {
            log.error("Failed to drop the alias [{}] on indexName [{}]: {}",
                    aliasName, indexName, ex.getMessage());
            return false;
        }
    }

    /**
     * Check if an alias exist.
     * @param aliasName the alias name.
     * @param indexName then index name.
     * @return true if alias exist, false if alias does not exist or error.
     */
    public final boolean checkAlias(
            final String aliasName, final String indexName) {

        final AliasExists aliasExists = new AliasExists.Builder()
                .alias(aliasName)
                .addIndex(indexName)
                .build();

        JestResult result;

        try {
            result = this.client.execute(aliasExists);
        } catch (IOException e) {
            log.error("Failed to load aliases on indexName [{}]: {}",
                    indexName, e.getMessage());
            throw new IllegalStateException(e);
        }

        if (!result.isSucceeded()) {
            log.error("Failed to load aliases on indexName [{}]: {}",
                    indexName, result.getErrorMessage());
            return false;
        }

        return true;
    }

    /**
     * Get Alias Info.
     * @param indexName the index names
     * @return List of {@list AliasInfo}
     */
    public final List<AliasInfo> getAliases(final String indexName) {

        /* GetAliases creates an URI aliases instead of alias as of jest 5.3.3.
        So this method won't work until we upgrade to a version with fix. */
        final GetAliases getAliases = new GetAliases.Builder()
                .addIndex(indexName)
                .build();

        JestResult result;

        try {
            result = this.client.execute(getAliases);
        } catch (final IOException ex) {
            log.error("Failed to load aliases on indexName [{}]: {}",
                    indexName, ex.getMessage());
            throw new IllegalStateException(ex);
        }

        if (!result.isSucceeded()) {
            log.error("Failed to load aliases on indexName [{}]: {}",
                    indexName, result.getErrorMessage());
            return Collections.emptyList();
        }

        final List<AliasInfo> aliasesInfo = new ArrayList<>();
        final JsonObject aliasesObj = result.getJsonObject()
                .getAsJsonObject(indexName).getAsJsonObject("aliases");



        for (final Map.Entry<String, JsonElement> aliasEntry
                : aliasesObj.entrySet()) {

            final JsonObject aliasObject =
                    aliasEntry.getValue().getAsJsonObject();

            String indexRouting = null;
            if (aliasObject.has("index_routing")) {
                indexRouting = aliasObject.get("index_routing").getAsString();
            }


            List<String> searchRouting = null;
            if (aliasObject.has("search_routing")) {
                searchRouting = Arrays.asList(aliasObject.get("search_routing")
                        .getAsString().split(","));
            }

            Map<String, Object> filter = null;
            if (aliasObject.has("filter")) {
                filter = this.gson.fromJson(aliasObject.get("filter"),
                        new TypeToken<Map<String, Object>>() { }.getType());
            }

            aliasesInfo.add(AliasInfo.builder()
                    .indexName(indexName)
                    .alias(aliasEntry.getKey())
                    .indexRouting(indexRouting)
                    .searchRouting(searchRouting)
                    .filter(filter)
                    .build());
        }

        return aliasesInfo;
    }

}
