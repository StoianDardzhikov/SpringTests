package TestFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class E {
    public F f;

    @Autowired
    public E(F f) {
        this.f = f;
    }
}