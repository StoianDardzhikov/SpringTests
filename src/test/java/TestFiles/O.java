package TestFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class O {
    public A a;
    public B b;
    @Autowired
    public O(A a) {
        this.a = a;
    }

    @Autowired
    public void setB(B b) {
        this.b = b;
    }
}
