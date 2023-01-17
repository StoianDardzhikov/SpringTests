package TestFiles;

import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;

@Component
public class ShutdownHookPreDestroy {
    public static boolean destroyed = false;

    @PreDestroy
    public void destroy() {
        ShutdownHookPreDestroy.destroyed = true;
    }
}
