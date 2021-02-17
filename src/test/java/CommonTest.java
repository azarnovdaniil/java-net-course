import org.junit.Before;
import project.*;

public class CommonTest {

    Client client

    @Before // чтобы не дублировать этот медод будет запускаться перед каждым методом теста
    public void prepare() {
        calculator = new Calculator();
        System.out.println("Before");
    }

}
