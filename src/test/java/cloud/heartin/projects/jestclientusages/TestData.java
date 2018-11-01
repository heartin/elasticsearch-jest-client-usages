package cloud.heartin.projects.jestclientusages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import cloud.heartin.projects.jestclientusages.domain.UploadDocument;

public final class TestData {

    private TestData() {

    }

    public static final String EMPLOYEE_INDEX = "employee_index";
    public static final String STUDENT_INDEX = "student_index";

    public static List<UploadDocument> generateEmployeeData() {

        final String employeeIndex = "employee_index";

        List<UploadDocument> documents = new LinkedList<>();

        Map<String, Object> source = new HashMap<>();
        source.put("name", "Iron Man");
        source.put("age", 45);
        source.put("company", "Avengers");
        documents.add(UploadDocument.builder().id("1").index(employeeIndex).source(source).build());

        source = new HashMap<>();
        source.put("name", "Super Man");
        source.put("age", 45);
        source.put("company", "Justice League");
        documents.add(UploadDocument.builder().id("2").index(employeeIndex).source(source).build());

        source = new HashMap<>();
        source.put("name", "Hulk");
        source.put("age", 45);
        source.put("company", "Avengers");
        documents.add(UploadDocument.builder().id("3").index(employeeIndex).source(source).build());

        source = new HashMap<>();
        source.put("name", "Spider Man");
        source.put("age", 30);
        source.put("company", "Avengers");
        documents.add(UploadDocument.builder().id("4").index(employeeIndex).source(source).build());

        return documents;
    }

    public static List<UploadDocument> generateStudentData() {

        final String employeeIndex = "student_index";

        List<UploadDocument> documents = new LinkedList<>();

        Map<String, Object> source = new HashMap<>();
        source.put("name", "Ant Man");
        source.put("age", 45);
        source.put("company", "Avengers");
        documents.add(UploadDocument.builder().id("1").index(employeeIndex).source(source).build());


        source = new HashMap<>();
        source.put("name", "Spider Man");
        source.put("age", 30);
        source.put("company", "Avengers");
        documents.add(UploadDocument.builder().id("2").index(employeeIndex).source(source).build());

        return documents;
    }

    public static List<UploadDocument> generateTestDocumentsWithNestedObject(final String index,
            final int outerFrom,
            final int outerTo,
            final int nestedTo,
            final String partition) {

        List<UploadDocument> documents = new LinkedList<>();

        for (int i = outerFrom; i <= outerTo; i++) {

            Map<String, Object> sourceParent = new HashMap<>();
            sourceParent.put("top1_string", "reg" + i);
            sourceParent.put("top2_int", i);

            List<Map<String, Object>> nestedObjectList = new ArrayList<>();

            for (int j = 1; j <= nestedTo; j++) {
                Map<String, Object> sourceChild = new HashMap<>();
                sourceChild.put("name", "nested_" + partition + "_" + j);

                if (j % 2 == 0) {
                    sourceChild.put("type", "string");
                    sourceChild.put("value_string", "val" + j);
                } else {
                    sourceChild.put("type", "integer");
                    sourceChild.put("value_integer", j);
                }

                nestedObjectList.add(sourceChild);
            }

            sourceParent.put("nested1", nestedObjectList);

            documents.add(UploadDocument.builder().id(i + "").index(index).source(sourceParent).build());

        }

        return documents;
    }

    public static List<UploadDocument> generateTestDocuments(final String index, final int maxDocs) {

        final int minAge = 21, maxAge = 50;
        int range = maxAge - minAge + 1;

        List<UploadDocument> documents = new LinkedList<>();

        for (int i = 1; i <= maxDocs; i++) {
            int rand = (int) (Math.random() * range) + minAge;

            Map<String, Object> sourceParent = new HashMap<>();
            sourceParent.put("top_name", "Emp" + i);
            sourceParent.put("top_value", rand);

            documents.add(UploadDocument.builder().id(i + "").index(index).source(sourceParent).build());
        }

        return documents;
    }

}
