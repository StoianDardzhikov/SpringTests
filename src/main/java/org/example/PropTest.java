package org.example;

import org.springframework.stereotype.Component;

@Component
public class PropTest {
    public String thisIsMyProp;

    public static String thisIsMyStaticProp;

    public void setThisIsMyProp(String thisIsMyProp) {
        this.thisIsMyProp = thisIsMyProp;
    }

    public void setThisIsMyStaticProp(String thisIsMyStaticProp) {
        PropTest.thisIsMyStaticProp = thisIsMyStaticProp;
    }
}