package cloud.heartin.projects.jestclientusages;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import cloud.heartin.projects.jestclientusages.service.BulkService;
import cloud.heartin.projects.jestclientusages.service.IndexService;

import lombok.Getter;

public class TestParentWithDataMultiIndex extends TestParent {

    @Getter
    private List<String> indexes;

    @Autowired
    private BulkService bulkService;

    @Autowired
    private IndexService indexService;

    private static final String MAPPINGS_EMPLOYEE = "employee-nested-mappings.json";
    private static final String MAPPINGS_STUDENT = "student-nested-mappings.json";


    @Before
    public void createIndexes() throws IOException {

        indexService.createIndexFromPath(TestData.EMPLOYEE_INDEX, "classpath:" + MAPPINGS_EMPLOYEE);
        indexService.createIndexFromPath(TestData.STUDENT_INDEX, "classpath:" + MAPPINGS_STUDENT);

        indexes = new LinkedList<>();
        indexes.add(TestData.EMPLOYEE_INDEX);
        indexes.add(TestData.STUDENT_INDEX);
        bulkService.bulkUpload(TestData.generateEmployeeData(), true);
        bulkService.bulkUpload(TestData.generateStudentData(), true);

        // To reflect changes immediately.
        // The refresh property of BulkUpload is not guaranteed to help.
        indexService.refresh(indexes);
    }

    @After
    public void cleanup() {
        indexService.deleteIndexSilently(TestData.EMPLOYEE_INDEX);
        indexService.deleteIndexSilently(TestData.STUDENT_INDEX);
    }

    @Test
    public void test() {
        // Do nothing - Hack to avoid 'JUnit No runnable methods' error.
    }
}
