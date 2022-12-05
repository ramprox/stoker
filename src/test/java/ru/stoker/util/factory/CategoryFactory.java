package ru.stoker.util.factory;

import ru.stoker.database.entity.Category;

public class CategoryFactory {

    public static Category category(String name) {
        Category category = new Category();
        category.setName(name);
        return category;
    }

    public static Category category(String name, Category parent) {
        Category category = category(name);
        category.setParent(parent);
        return category;
    }

}
