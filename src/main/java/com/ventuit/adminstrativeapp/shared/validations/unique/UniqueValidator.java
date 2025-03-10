package com.ventuit.adminstrativeapp.shared.validations.unique;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerMapping;

import com.ventuit.adminstrativeapp.config.ContextProvider;
import com.ventuit.adminstrativeapp.utils.ReflectionUtils;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Query;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final EntityManager entityManager = (EntityManager) ContextProvider.getBean(EntityManager.class);
    private Class<?> model;
    private String fieldName;
    private String idField;

    @Override
    public void initialize(Unique constraintAnnotation) throws IllegalArgumentException, IllegalStateException {

        // Initialize every value

        ConstraintValidator.super.initialize(constraintAnnotation);
        this.model = constraintAnnotation.model();
        this.fieldName = constraintAnnotation.fieldName();
        this.idField = constraintAnnotation.idField();

        // Check if the entity manager is injected
        if (this.entityManager == null) {
            throw new IllegalStateException("Entity manager is not initialized");
        }

        // Check if entity and fieldName are not null
        if (this.model == null || this.fieldName == null) {
            throw new IllegalStateException("Entity class or field name is not set");
        }

        // Check if the entity class is annotated with @Entity
        if (!this.model.isAnnotationPresent(Entity.class))
            throw new IllegalArgumentException(constraintAnnotation.model().getSimpleName() + " is not an entity");

        // Check if the id field is defined in the entity
        if (!ReflectionUtils.hasField(model, idField)) {
            throw new IllegalArgumentException("The field id: '" + this.fieldName + "' has not been found in the '"
                    + this.model.getSimpleName() + "' entity");
        }

        // Check if the field to compare is defined in the entity
        if (!ReflectionUtils.hasField(model, fieldName)) {
            throw new IllegalArgumentException("The field '" + this.fieldName + "' has not been found in the '"
                    + this.model.getSimpleName() + "' entity");
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        try {

            Boolean isUpdating = false;
            String id = null;

            // Build the JPQL query dynamically.
            String jpql = "SELECT COUNT(e) FROM " + model.getSimpleName() + " e WHERE e."
                    + fieldName + " = :value";

            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();

            if (attributes != null) {
                HttpServletRequest request = attributes.getRequest();
                if (request != null) {
                    // Extract path variables from the request
                    Object attr = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
                    Map<String, String> pathVariables = new HashMap<>();
                    if (attr instanceof Map<?, ?>) {
                        for (Map.Entry<?, ?> entry : ((Map<?, ?>) attr).entrySet()) {
                            pathVariables.put(String.valueOf(entry.getKey()), String.valueOf(entry.getValue()));
                        }
                    }

                    id = pathVariables.get("id");

                    isUpdating = id != null && !id.trim().isEmpty();

                    if (isUpdating) {
                        jpql += " AND e." + idField + " <> :id";
                    }
                }
            }

            Query query = entityManager.createQuery(jpql);
            query.setFlushMode(FlushModeType.COMMIT);
            query.setParameter("value", value);

            if (isUpdating) {
                query.setParameter("id", id);
            }

            long count = (long) query.getSingleResult();
            return count == 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
