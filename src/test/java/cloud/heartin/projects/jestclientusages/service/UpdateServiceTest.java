package cloud.heartin.projects.jestclientusages.service;

import cloud.heartin.projects.jestclientusages.NestedTestParent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UpdateServiceTest extends NestedTestParent {

    private static final String INDEX = "bulk_test_index";

    @Autowired
    private NestedQueryService nestedService;

    @Autowired
    private UpdateService updateService;


    @Test
    public void multiGetQueryTestWithNestedFieldsAndUpdates() throws IOException {

        final String path = "nested1";

        final Map<String, String> attributeTypeMap = new HashMap<>();
        attributeTypeMap.put(path, "NESTED");

        final Map<String, Object> updateDocumentMap = aNestedAttributesObject();
        attributeTypeMap.put("top1_string", "STRING");
        updateService.updateIndex(INDEX, "1", updateDocumentMap, attributeTypeMap, "nested1");

        final Map<String, Map<String, Object>> result = nestedService.multiGetQuerywithNestedField(
                INDEX,
                path,
                Arrays.asList("1"),
                "true");

        final Map<String, Object> documentMap2  = result.get("1");

        assertEquals(4, documentMap2.size(), 0);

    }


    private Map<String, Object> aNestedAttributesObject() {

        Map<String, Object> documentMap = new HashMap<>();

        // Add all custom attributes from the event
        final List<Map<String, Object>> nestedObjectList = new ArrayList<>();

        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("name", "name1");
        objectMap.put("type", "integer");
        objectMap.put("value_integer", "777");
        nestedObjectList.add(objectMap);

        objectMap = new HashMap<>();
        objectMap.put("name", "name2");
        objectMap.put("type", "keyword");
        objectMap.put("value_integer", "888");
        nestedObjectList.add(objectMap);

        documentMap.put("nested1", nestedObjectList);

        documentMap.put("top1_string", "top123");

        return documentMap;
    }
}
