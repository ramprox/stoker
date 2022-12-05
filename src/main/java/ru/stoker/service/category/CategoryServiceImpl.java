package ru.stoker.service.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.stoker.database.entity.Category;
import ru.stoker.database.repository.CategoryRepository;
import ru.stoker.dto.category.CategoryDto;
import ru.stoker.mapper.CategoryMapper;

import java.util.List;

import static ru.stoker.database.specification.CategorySpecification.*;
import static ru.stoker.exceptions.Category.*;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository,
                               CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    @Transactional
    public CategoryDto save(CategoryDto categoryDto) {
        checkSaveCategoryDto(categoryDto);
        Category category = categoryMapper.toCategory(categoryDto);
        categoryRepository.save(category);
        return categoryMapper.updateFromCategory(category, categoryDto);
    }

    @Override
    public CategoryDto findById(Long id) {
        Category category = getById(id);
        return categoryMapper.fromCategory(category);
    }

    @Override
    @Transactional
    public CategoryDto update(CategoryDto categoryDto) {
        checkUpdateCategoryDto(categoryDto);
        Long id = categoryDto.getId();
        Category category = getById(id);
        categoryMapper.updateCategoryFromDto(categoryDto, category);
        return categoryDto;
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Category category = getById(id);
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryDto> findAll(Long parentId) {
        Specification<Category> spec = parentId(parentId);
        List<Category> categories = categoryRepository.findAll(spec);
        return categoryMapper.toList(categories);
    }

    private Category getById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    private void checkSaveCategoryDto(CategoryDto category) {
        Long parentId = category.getParentId();
        String name = category.getName();
        if (parentId == null) {
            checkNotExistRootCategory(name, byParentIdAndName(name, parentId));
        } else {
            checkExistParentAndNotExistByName(category, byIdOrNameAndParentId(name, parentId));
        }
    }

    private void checkUpdateCategoryDto(CategoryDto category) {
        String name = category.getName();
        Long parentId = category.getParentId();
        Long id = category.getId();
        if(parentId == null) {
            checkNotExistRootCategory(name, byParentIdAndNameExcludingId(name, id));
        } else {
            Specification<Category> specification = byIdOrNameExcludingId(name, parentId, id);
            checkExistParentAndNotExistByName(category, specification);
        }
    }

    private void checkNotExistRootCategory(String name, Specification<Category> specification) {
        boolean exist = categoryRepository.exists(specification);
        if(exist) {
            throw new RootAlreadyExistException(name);
        }
    }

    private void checkExistParentAndNotExistByName(CategoryDto categoryDto, Specification<Category> specification) {
        long count = categoryRepository.count(specification);
        Long parentId = categoryDto.getParentId();
        if(count == 0) {
            throw new ParentNotFoundException(parentId);
        } else if(count == 2) {
            throw new AlreadyExistException(parentId, categoryDto.getName());
        }
    }

}
