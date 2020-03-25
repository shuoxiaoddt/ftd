import hello.GreetingController;
import javassist.*;

import java.io.IOException;

/**
 * @author xiaos
 * @date 2020/1/10 13:55
 */
public class App {

    public static void main(String[] args) {
        GreetingController controller = new GreetingController();
        controller.getDomain();

    }


}
