package ru.stoker.database.specification;

import org.springframework.data.jpa.domain.Specification;
import ru.stoker.database.entity.Category;

import javax.persistence.criteria.Path;

public final class CategorySpecification {

    public static Specification<Category> id(Long id) {
        return (root, query, builder) -> builder.equal(root.get("id"), id);
    }

    private static Specification<Category> name(String name) {
        return (root, query, builder) -> builder.equal(root.get("name"), name);
    }

    private static Specification<Category> idNot(Long id) {
        return (root, query, builder) -> builder.notEqual(root.get("id"), id);
    }

    public static Specification<Category> byParentIdAndName(String name, Long parentId) {
        return (root, query, builder) ->
                parentId(parentId).and(name(name))
                        .toPredicate(root, query, builder);
    }

    public static Specification<Category> byParentIdAndNameExcludingId(String name, Long id) {
        return (root, query, builder) ->
                byParentIdAndName(name, null).and(idNot(id))
                        .toPredicate(root, query, builder);
    }

    public static Specification<Category> parentId(Long id) {
        return (root, query, builder) -> {
            Path<Object> parent = root.get("parent");
            return id == null || id == 0 ? builder.isNull(parent) : builder.equal(parent.get("id"), id);
        };
    }

    public static Specification<Category> byIdOrNameAndParentId(String name, Long id) {
        return (root, query, criteriaBuilder) ->
                id(id).or(name(name).and(parentId(id)))
                        .toPredicate(root, query, criteriaBuilder);
    }

    public static Specification<Category> byIdOrNameExcludingId(String name, Long id, Long excludeId) {
        return (root, query, criteriaBuilder) ->
                byIdOrNameAndParentId(name, id).and(idNot(excludeId))
                        .toPredicate(root, query, criteriaBuilder);
    }

}
