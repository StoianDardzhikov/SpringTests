package TestFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Lazy
@Component
public class L {
    @Autowired
    public L() {
        K.loadedL = true;
    }
}