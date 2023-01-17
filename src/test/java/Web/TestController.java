package Web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    public RequestScope requestScope;
    @Autowired
    public SessionScope sessionScope;
    @Autowired
    public ApplicationScope applicationScope;


    @GetMapping("/request")
    public String getRequestScope() {
        return requestScope.toString();
    }

    @GetMapping("/session")
    public String getSessionScope() {
        return sessionScope.toString();
    }

    @GetMapping("/application")
    public String getApplication() {
        return applicationScope.toString();
    }
}
