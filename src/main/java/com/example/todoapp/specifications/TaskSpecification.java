package com.example.todoapp.specifications;

import com.example.todoapp.entities.Task;
import org.springframework.data.jpa.domain.Specification;

public class TaskSpecification {
    public static Specification<Task> belongsToUser(String email) {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("user").get("email"), email);
    }

    public static Specification<Task> hasName(String name) {
        return (root, query, criteriaBuilder) ->
                name == null || name.isEmpty() ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.like(
                                criteriaBuilder.lower(root.get("title")),
                                "%" + name.toLowerCase() + "%"
                        );
    }

    public static Specification<Task> hasCaseInsensitiveSort(String sortBy, String direction) {
        return (root, query, cb) -> {
            if ("title".equals(sortBy)) {
                query.orderBy(direction.equalsIgnoreCase("desc") ?
                        cb.desc(cb.lower(root.get("title"))) :
                        cb.asc(cb.lower(root.get("title"))));
            }
            return query.getRestriction();
        };
    }

    public static Specification<Task> hasPriority(String priority) {
        return (root, query, criteriaBuilder) ->
                priority == null || priority.isEmpty() ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("priority"), priority);
    }


    public static Specification<Task> hasState(String state) {
        return (root, query, criteriaBuilder) ->
                state == null || state.isEmpty() ?
                        criteriaBuilder.conjunction() :
                        criteriaBuilder.equal(root.get("state"), state);
    }
}
