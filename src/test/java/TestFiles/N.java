package TestFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class N {
    public A a;

    @Autowired
    public void setA(A a) {
        this.a = a;
    }
}