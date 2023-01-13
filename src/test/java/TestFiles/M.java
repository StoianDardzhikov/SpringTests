package TestFiles;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class M implements InitializingBean {

    @Autowired
    public String dbURL;

    @Override
    public void afterPropertiesSet() throws Exception {
        dbURL = dbURL + " - url";
    }
}
