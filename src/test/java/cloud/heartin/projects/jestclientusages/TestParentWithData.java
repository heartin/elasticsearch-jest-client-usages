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

public class TestParentWithData extends TestParent {

    @Getter
    private List<String> indexes;

    @Autowired
    private BulkService bulkService;

    @Autowired
    private IndexService indexService;

    @Before
    public void createIndexes() throws IOException {
        indexes = new LinkedList<>();
        indexes.add(TestData.EMPLOYEE_INDEX);

        bulkService.bulkUpload(TestData.generateEmployeeData(), true);

        // To reflect changes immediately.
        // The refresh property of BulkUpload is not guaranteed to help.
        indexService.refresh(indexes);
    }

    @After
    public void cleanup() {
        indexService.deleteIndexSilently(TestData.EMPLOYEE_INDEX);
    }

    @Test
    public void test() {
        // Do nothing - Hack to avoid 'JUnit No runnable methods' error.
    }
}
