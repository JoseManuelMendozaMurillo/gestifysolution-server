package com.ventuit.adminstrativeapp.shared.validations.exist;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import com.ventuit.adminstrativeapp.config.ContextProvider;

import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

public class ExistValidator implements ConstraintValidator<Exist, Object> {

    private static final Logger logger = LoggerFactory.getLogger(ExistValidator.class);

    private Class<?> repositoryClass;
    private Class<?> paramType;
    private String methodName;
    private Object repositoryBean;
    private Method validationMethod;

    @Override
    public void initialize(Exist constraintAnnotation) {
        repositoryClass = constraintAnnotation.repository();
        methodName = constraintAnnotation.method();
        paramType = constraintAnnotation.paramType();

        validateRepositoryClass();
        repositoryBean = getRepositoryBean();

        validationMethod = getValidationMethod();
        validateValidationMethod();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        try {
            return (boolean) validationMethod.invoke(repositoryBean, value);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            String errorMessage = "Error invoking method '" + methodName + "' on repository '"
                    + repositoryClass.getSimpleName() + "': " + (cause != null ? cause.getMessage() : e.getMessage());
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } catch (IllegalAccessException e) {
            String errorMessage = "Cannot access method '" + methodName + "' on repository '"
                    + repositoryClass.getSimpleName() + "': " + e.getMessage();
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        } catch (Exception e) {
            String errorMessage = "Unexpected error validating existence with repository '"
                    + repositoryClass.getSimpleName() + "' and method '" + methodName + "': "
                    + e.getMessage();
            logger.error(errorMessage);
            throw new RuntimeException(errorMessage);
        }
    }

    private void validateRepositoryClass() {
        if (repositoryClass == null) {
            throw new IllegalArgumentException("Repository class cannot be null");
        }

        // Check if class has @Repository, @Component, or @Service annotation
        boolean hasValidAnnotation = repositoryClass.isAnnotationPresent(Repository.class) ||
                repositoryClass.isAnnotationPresent(Component.class) ||
                repositoryClass.isAnnotationPresent(Service.class);

        if (!hasValidAnnotation) {
            logger.error("Repository class '{}' is not annotated with @Repository, @Component, or @Service.",
                    repositoryClass.getName());
            throw new IllegalArgumentException("Invalid repository class: " + repositoryClass.getName());
        }
    }

    private void validateValidationMethod() {
        if (!Boolean.class.equals(validationMethod.getReturnType())
                && !boolean.class.equals(validationMethod.getReturnType())) {
            String errorMessage = "Validation method '" + methodName + "' in repository '"
                    + repositoryClass.getSimpleName() + "' must return a boolean.";
            logger.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }

    private Object getRepositoryBean() {
        try {
            return ContextProvider.getBean(repositoryClass);
        } catch (Exception e) {
            String error = "Error getting repository bean for class '" + repositoryClass.getName() + "': "
                    + e.getMessage();
            logger.error(error);
            throw new IllegalStateException(error);
        }
    }

    private Method getValidationMethod() {
        try {
            return repositoryClass.getMethod(methodName, paramType);
        } catch (NoSuchMethodException e) {
            String errorMessage = "Method '" + methodName + "' with parameter type '"
                    + paramType.getSimpleName() + "' not found in repository '"
                    + repositoryClass.getSimpleName() + "'.";
            logger.error(errorMessage);
            throw new IllegalStateException(errorMessage);
        }
    }
}
