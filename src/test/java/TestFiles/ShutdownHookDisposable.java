package TestFiles;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHookDisposable implements DisposableBean {
    public static boolean destroyed = false;

    @Override
    public void destroy() throws Exception {
        ShutdownHookDisposable.destroyed = true;
    }
}
