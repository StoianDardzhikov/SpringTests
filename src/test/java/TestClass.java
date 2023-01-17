import CircularDependency.Circular;
import org.example.PropTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.*;
import TestFiles.*;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.*;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan("TestFiles")
@Configuration
@PropertySource("prop.properties")
@ImportResource("context.xml")
public class TestClass {

    @Bean
    public BeanPostProcessor beanPostProcessor() {
        return new MyBeanPostProcessor();
    }

    @Bean
    public A a1() { return new A(); }
    @Bean
    @Scope("prototype")
    public B b1() { return new B(); }
    @Bean
    public D d1() { return new D(); }
    @Bean
    public F f1() { return new F(); }
    @Bean
    public I i1() { return new I(); }
    @Bean(destroyMethod = "destroy")
    public ShutdownHookAnnotation shutdownHookAnnotation() {
        return new ShutdownHookAnnotation();
    }

    @Bean(name = "dbURL")
    public String string1() {
        return "This is dbURL";
    }

    @Bean
    public String string2() {return  ""; }

    ConfigurableApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = new AnnotationConfigApplicationContext(TestClass.class);
    }

    @Test
    @DisplayName("Autowire variable")
    public void test1() {
        B b = context.getBean(B.class);

        assertNotNull(b.a);
    }

    @Test
    @DisplayName("Throw no bean declared")
    public void test2() {
        assertThrows(NoSuchBeanDefinitionException.class, () -> {
           C c = context.getBean(C.class);
        });
    }

    @Test
    @DisplayName("Defaults interface")
    public void test3() {
        B b = context.getBean(B.class);
        assertEquals(b.a.getClass(), A.class);
    }

    @Test
    @DisplayName("Autowire named bean")
    public void test4() {
        D d = context.getBean(D.class);
        assertNotNull(d.dbURL);
        assertEquals(string1(), d.dbURL);
    }

    @Test
    @DisplayName("Autowire constructor")
    public void test5() {
        E e = context.getBean(E.class);
        assertNotNull(e.f);
    }

    @Test
    @DisplayName("Get correct implementation with @Qualifier")
    public void test6() {
        I i = context.getBean(I.class);
        assertNotNull(i.inter);
        assertEquals(i.inter.getClass(), G.class);
    }

    @Test
    @DisplayName("Throw exception when trying to get interface, without @Qualifier, that has multiple implementation")
    public void test7() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> {
            J j = context.getBean(J.class);
            System.out.println(j.inter);
        });
    }

    @Test
    @DisplayName("AnnotationConfigApplicationContext circular dependency working")
    public void test8() {
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext(CircularDependencyConfig.class);
        Circular circular = configApplicationContext.getBean(Circular.class);
        assertNotNull(circular);
        assertNotNull(circular.dependency);
        assertEquals(circular.dependency.circular.dependency.circular.dependency, circular.dependency);
    }

    @Test
    @DisplayName("Injecting through properties file")
    public void test9() {
        PropTest propTest = context.getBean(PropTest.class);

        assertEquals(propTest.thisIsMyProp, "propValue");
    }

    @Test
    @DisplayName("Setter injection")
    public void test10() {
        N n = context.getBean(N.class);
        assertNotNull(n.a);
    }

    @Test
    @DisplayName("Test lazy loading")
    public void test11() {
        K k = context.getBean(K.class);
        assertFalse(K.loadedL);
        k.l.toString();
        assertTrue(K.loadedL);
    }

    @Test
    @DisplayName("Test not auto wired variable")
    public void test12() {
        A a = context.getBean(A.class);
        assertNull(a.c);
    }

    @Test
    @DisplayName("Initialize interface with only 1 implementation")
    public void test13() {
        IA ia = context.getBean(IA.class);
        assertEquals(ia.getClass(), A.class);
    }

    @Test
    @DisplayName("Initialize interface with more than 1 implementations")
    public void test14() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> {
            Inter inter = context.getBean(Inter.class);
        });
    }

    @Test
    @DisplayName("Initializing bean test")
    public void test15() {
        M m = context.getBean(M.class);
        assertEquals(m.dbURL, string1() + " - url");
    }

    @Test
    @DisplayName("Constructor and set based injection together")
    public void test16() {
        O o = context.getBean(O.class);
        assertNotNull(o.a);
        assertNotNull(o.b);
    }

    @Test
    @DisplayName("Prototype scope creates a different instance every time")
    public void test17() {
        B b1 = context.getBean(B.class);
        B b2 = context.getBean(B.class);

        assertNotEquals(b1, b2);
    }

    @Test
    @DisplayName("Injecting value into static field")
    public void test18() {
        context.getBean(PropTest.class);

        assertEquals(PropTest.thisIsMyStaticProp, "propValue");
    }

    @Test
    @DisplayName("BeanPostProcessor invoking after initialization")
    public void test19() {
        context.getBean(PropTest.class);
        assertEquals(PostProcessorTest.message, "after");
    }

    @Test
    @DisplayName("BeanPostProcessor invoking before initialization")
    public void test20() {
        MyBeanPostProcessor.invokedBeforeInitialization = false;
        new AnnotationConfigApplicationContext(TestClass.class);
        assertTrue(MyBeanPostProcessor.invokedBeforeInitialization);
    }

    @Test
    @DisplayName("Shutdown hook pre destroy invoking on destroy")
    public void test21() {
        ShutdownHookPreDestroy.destroyed = false;
        context.close();
        assertTrue(ShutdownHookPreDestroy.destroyed);
    }

    @Test
    @DisplayName("Shutdown hook disposable invoking on destroy")
    public void test22() {
        ShutdownHookDisposable.destroyed = false;
        context.close();
        assertTrue(ShutdownHookDisposable.destroyed);
    }

    @Test
    @DisplayName("Shutdown hook annotation invoking on destroy")
    public void test23() {
        ShutdownHookAnnotation.destroyed = false;
        context.close();
        assertTrue(ShutdownHookAnnotation.destroyed);
    }

    @Test
    @DisplayName("Post construct annotation invoking")
    public void test24() {
        assertTrue(PostConstructAnnotation.postConstructInvoked);
    }
}