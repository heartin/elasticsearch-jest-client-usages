package cloud.heartin.projects.jestclientusages;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestParent {

    @Test
    public void test() {
      // Do nothing - Hack to avoid 'JUnit No runnable methods' error.
    }
}
