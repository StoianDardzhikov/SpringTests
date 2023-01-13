package TestFiles;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class I {
    @Autowired @Qualifier("g")
    public Inter inter;
}