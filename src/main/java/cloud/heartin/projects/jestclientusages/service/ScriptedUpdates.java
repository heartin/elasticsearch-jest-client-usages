package cloud.heartin.projects.jestclientusages.service;

import io.searchbox.client.JestClient;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Update;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * Updates using scripts.
 */
public class ScriptedUpdates {

    private final JestClient client;

    /**
     * ScriptedUpdates constructor.
     * @param client Jest Client to use.
     */
    @Autowired
    public ScriptedUpdates(final JestClient client) {
        this.client = client;
    }

    /**
     * Scripted updates.
     * @param index - index.
     * @param name - name.
     * @param value - new value.
     * @param id - id.
     */
    public final void scriptedUpdate(final String index, final String name, final String value, final String id) {

        final String script = "{\n"
                + "    \"lang\": \"painless\",\n"
                + "    \"source\": \"\"\"\n"
                + "            for (int i = 0; i < ctx._source.custom.length; ++i) {\n"
                + "              if(ctx._source.custom[i].name == params.ca_name) {\n"
                + "                ctx._source.custom[i].value_string = '" + value + "';\n"
                + "              }\n"
                + "            }\n"
                + "\"\"\",\n"
                + "    \"params\": {\n"
                + "      \"ca_name\": \"" + name + "\"\n"
                + "    }\n"
                + "  }";

        try {
            DocumentResult result = client.execute(new Update.Builder(script).index(index).type("_doc").id(id).build());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
