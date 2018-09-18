import org.junit.Assert;
import org.junit.Test;

public class JUnitTest {

    @Test
    public void isTrue() {
        ToTest toTest = new ToTest();
        Assert.assertTrue(toTest.isTrue());
    }
}
