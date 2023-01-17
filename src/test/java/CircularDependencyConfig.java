import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan("CircularDependency")
@Configuration
public class CircularDependencyConfig {
}