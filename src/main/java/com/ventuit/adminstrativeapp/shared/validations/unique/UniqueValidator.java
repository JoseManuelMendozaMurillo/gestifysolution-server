package com.ventuit.adminstrativeapp.shared.validations.unique;

import com.ventuit.adminstrativeapp.config.ContextProvider;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FlushModeType;
import jakarta.persistence.Query;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UniqueValidator implements ConstraintValidator<Unique, String> {

    private final EntityManager entityManager = (EntityManager) ContextProvider.getBean(EntityManager.class);
    private Class<?> model;
    private String fieldName;

    @Override
    public void initialize(Unique constraintAnnotation) throws IllegalArgumentException, IllegalStateException {

        // Initialize every value
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.model = constraintAnnotation.model();
        this.fieldName = constraintAnnotation.fieldName();

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

        // Check if the field to compare is defined in the entity
        try {
            this.model.getDeclaredField(this.fieldName);
        } catch (NoSuchFieldException ex) {
            throw new IllegalArgumentException("The field '" + this.fieldName + "' has not been found in the '"
                    + this.model.getSimpleName() + "' entity");
        }
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        System.out.println(this.fieldName);
        System.out.println(this.model);
        System.out.println(this.entityManager);
        // Create JPQL query for validating if the field is unique
        String jpqlQueryString = "SELECT COUNT(e." + this.fieldName + ") FROM " + this.model.getSimpleName()
                + " e WHERE e." + this.fieldName + "= :value";
        Query jpqlQuery = this.entityManager.createQuery(jpqlQueryString);

        // Prevent the query from triggering a flush of the persistence context.
        jpqlQuery.setFlushMode(FlushModeType.COMMIT); // or FlushModeType.MANUAL if supported

        jpqlQuery.setParameter("value", value);

        // Get the count of entities with the same value
        long count = (long) jpqlQuery.getSingleResult();

        // If count is 0, the field is unique; otherwise, it is not unique
        return count == 0;
    }
}
