package com.ventuit.adminstrativeapp.config;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.lang.NonNull;

@Component
public class ContextProvider implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        ContextProvider.applicationContext = applicationContext;
    }

    public static Object getBean(Class<?> cls) {
        return ContextProvider.applicationContext.getBean(cls);
    }
}
