import CircularDependency.Circular;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.NoUniqueBeanDefinitionException;
import org.springframework.beans.factory.UnsatisfiedDependencyException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import TestFiles.*;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import static org.junit.jupiter.api.Assertions.*;

@ComponentScan("TestFiles")
@SpringBootConfiguration
public class TestClass {

    public static boolean loaded = false;

    @Bean
    public A a1() { return new A(); }
    @Bean
    public B b1() { return new B(); }
    @Bean
    public D d1() { return new D(); }
    @Bean
    public F f1() { return new F(); }
    @Bean
    public I i1() { return new I(); }

    @Bean(name = "dbURL")
    public String string1() {
        return "This is dbURL";
    }

    ConfigurableApplicationContext context;

    @BeforeEach
    public void setUp() {
        context = SpringApplication.run(TestClass.class);
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
        AnnotationConfigApplicationContext configApplicationContext = new AnnotationConfigApplicationContext("CircularDependency");
        Circular circular = configApplicationContext.getBean(Circular.class);
        assertNotNull(circular);
        assertNotNull(circular.dependency);
        assertEquals(circular.dependency.circular.dependency.circular.dependency, circular.dependency);
    }

    @Test
    @DisplayName("ConfigurableApplicationContext throws circular dependency exception")
    public void test9() {
        assertThrows(UnsatisfiedDependencyException.class, () -> {
            ConfigurableApplicationContext cirDepContext = SpringApplication.run(CircularDependencyConfig.class);
            Circular circular = cirDepContext.getBean(Circular.class);
        });
    }

    @Test
    @DisplayName("Test lazy loading")
    public void test10() {
        K k = context.getBean(K.class);
        assertFalse(K.loadedL);
        k.l.toString(); // Trigger toString method of L to force it to initialize
        assertTrue(K.loadedL);
    }

    @Test
    @DisplayName("Test not auto wired variable")
    public void test11() {
        A a = context.getBean(A.class);
        assertNull(a.c);
    }

    @Test
    @DisplayName("Initialize interface with only 1 implementation")
    public void test12() {
        IA ia = context.getBean(IA.class);
        assertEquals(ia.getClass(), A.class);
    }

    @Test
    @DisplayName("Initialize interface with more than 1 implementations")
    public void test13() {
        assertThrows(NoUniqueBeanDefinitionException.class, () -> {
            Inter inter = context.getBean(Inter.class);
        });
    }

    @Test
    @DisplayName("Initializing bean test")
    public void test14() {
        M m = context.getBean(M.class);
        assertEquals(m.dbURL, string1() + " - url");
    }
}