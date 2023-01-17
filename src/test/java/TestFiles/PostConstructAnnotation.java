package TestFiles;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PostConstructAnnotation {
    public static boolean postConstructInvoked = false;

    @PostConstruct
    public void postConstruct() {
        PostConstructAnnotation.postConstructInvoked = true;
    }
}
