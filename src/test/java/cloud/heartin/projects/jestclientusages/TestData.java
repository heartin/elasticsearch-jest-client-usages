package cloud.heartin.projects.jestclientusages;

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

}
