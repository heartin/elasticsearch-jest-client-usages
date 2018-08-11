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

    public static List<UploadDocument> generateTestDocumentsWithNestedObject(final String index, final int maxDocs, final int maxInner) {

        final int min = 21, max = 50;
        int range = max - min + 1;

        List<UploadDocument> documents = new LinkedList<>();

        for (int i = 1; i <= maxDocs; i++) {
            int rand = (int) (Math.random() * range) + min;

            Map<String, Object> sourceParent = new HashMap<>();
            sourceParent.put("top_name", "Emp" + i);
            sourceParent.put("top_value", rand);

            List<Map<String, Object>> nestedObjectList = new ArrayList<>();

            for (int j = 1; j <= maxInner; j++) {
                Map<String, Object> sourceChild = new HashMap<>();
                rand = (int) (Math.random() * range) + min;
                sourceChild.put("inner_name", "inn" + j);
                sourceChild.put("inner_value", rand);
                nestedObjectList.add(sourceChild);
            }

            sourceParent.put("inner", nestedObjectList);

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
