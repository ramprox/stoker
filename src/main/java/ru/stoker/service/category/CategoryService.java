package ru.stoker.service.category;

import ru.stoker.dto.category.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto save(CategoryDto category);

    CategoryDto findById(Long id);

    CategoryDto update(CategoryDto categoryDto);

    void deleteById(Long id);

    List<CategoryDto> findAll(Long parentId);

}
