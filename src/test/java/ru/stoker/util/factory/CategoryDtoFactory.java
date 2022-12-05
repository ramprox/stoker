package ru.stoker.util.factory;

import ru.stoker.dto.category.CategoryDto;

public class CategoryDtoFactory {

    public static CategoryDto categoryDto(Long id, String name, Long parentId) {
        CategoryDto categoryDto = categoryDto(name, parentId);
        categoryDto.setId(id);
        return categoryDto;
    }

    public static CategoryDto categoryDto(String name, Long parentId) {
        CategoryDto categoryDto = categoryDto(name);
        categoryDto.setParentId(parentId);
        return categoryDto;
    }

    public static CategoryDto categoryDto(String name) {
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName(name);
        return categoryDto;
    }

}
