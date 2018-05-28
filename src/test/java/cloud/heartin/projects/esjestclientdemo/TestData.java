package cloud.heartin.projects.esjestclientdemo;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cloud.heartin.projects.esjestclientdemo.domain.UploadDocument;

public final class TestData {

    private TestData() {

    }

    public static List<UploadDocument> generateUploadDocumentList(final String index) {

        List<UploadDocument> documents = new LinkedList<>();

        Map<String, Object> source = new HashMap<>();
        source.put("name", "Abc");
        source.put("age", 25);
        documents.add(UploadDocument.builder().id("1").index(index).source(source).build());


        source = new HashMap<>();
        source.put("name", "Def");
        source.put("age", 26);
        documents.add(UploadDocument.builder().id("2").index(index).source(source).build());

        return documents;
    }

}
