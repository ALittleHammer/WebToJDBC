package lambda;

import org.junit.Test;

public class LambdaTest {
    @Test
    public void test1(){
        Runnable r1 = () -> System.out.println("ww");
        r1.run();

    }
}
