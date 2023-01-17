package TestFiles;

public class ShutdownHookAnnotation {
    public static boolean destroyed = false;

    public void destroy() {
        ShutdownHookAnnotation.destroyed = true;
    }
}
