package TestFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Component("j")
public class J {
    // Made it Lazy, because otherwise it tries to create it on the start of the application and all the other tests fail. This way I only create it in the test needed.
    @Autowired @Lazy
    public Inter inter;
}