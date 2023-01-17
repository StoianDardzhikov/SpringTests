package TestFiles;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class MyBeanPostProcessor implements BeanPostProcessor {
    public static boolean invokedBeforeInitialization = false;

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        invokedBeforeInitialization = true;
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().equals(PostProcessorTest.class)) {
            PostProcessorTest.message = "after";
        }
        return bean;
    }
}