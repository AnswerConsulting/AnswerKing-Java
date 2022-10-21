package com.answerdigital.benhession.academy.answerkingweek2.services;

import com.answerdigital.benhession.academy.answerkingweek2.exceptions.UnableToSaveEntityException;
import com.answerdigital.benhession.academy.answerkingweek2.model.Category;
import com.answerdigital.benhession.academy.answerkingweek2.model.Item;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.CategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemCategoryRepository;
import com.answerdigital.benhession.academy.answerkingweek2.repositories.ItemRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final CategoryRepository categoryRepository;
    private final ItemCategoryRepository itemCategoryRepository;

    Logger logger = LoggerFactory.getLogger("ItemService");

    @Autowired
    public ItemService(final ItemRepository itemRepository, final CategoryRepository categoryRepository,
                       final ItemCategoryRepository itemCategoryRepository) {
        this.itemRepository = itemRepository;
        this.categoryRepository = categoryRepository;
        this.itemCategoryRepository = itemCategoryRepository;
    }

    public Optional<Item> findById(final Integer itemId) {
        return itemRepository.findById(itemId);
    }

    public Optional<Item> addItem(final Item item) throws UnableToSaveEntityException {
        if (itemRepository.existsByName(item.getName())) {
            return Optional.empty();
        } else {
            return Optional.of(save(item));
        }
    }

    public Optional<Item> updateItem(final Item item) throws UnableToSaveEntityException {

        final boolean hasNameConflict = itemRepository.existsByNameAndIdIsNot(item.getName(), item.getId());

        if (hasNameConflict) {
            return Optional.empty();
        } else {
            return Optional.of(save(item));
        }
    }

    private Item save(final Item item) throws UnableToSaveEntityException {

        try {
            return itemRepository.saveAndFlush(item);
        } catch (Exception e) {
            logger.error("save item: save operation failed");
            throw new UnableToSaveEntityException(String.format("Unable to save item = %s", item.getName()));
        }

    }

    public Optional<List<Item>> findAll() {

        final List<Item> items = itemRepository.findAll();

        if (items.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(items);


    }

//    public Optional<Item> deleteItem(Integer itemId) {
//        Optional<Item> item = itemRepository.findById(itemId);
//        Optional<Set<ItemCategory>> itemCategories = item.map(Item::clearCategories);
//        Optional<Set<Category>> categories = itemCategories.map(ic ->
//                ic.stream()
//                        .map(ItemCategory::getCategory)
//                        .collect(Collectors.toSet())
//        );
//
//        itemCategories.ifPresent(itemCategoryRepository::deleteAll);
//        categories.ifPresent(categoryRepository::saveAll);
//
//        return item.map(i -> {
//            itemRepository.delete(i);
//            return i;
//        });
//    }


    public Optional<Item> setCategories(final Item item, final Set<Integer> categoryIds) {
        final Set<Category> categorySet = new HashSet<>();

        categoryIds.forEach(categoryId -> {
            final Optional<Category> categoryOptional = categoryRepository.findById(categoryId);
            categoryOptional.ifPresent(categorySet::add);
        });

        if (categoryIds.size() == categorySet.size()) {
            item.changeCategoriesTo(categorySet);
            itemCategoryRepository.saveAll(categorySet
                    .stream()
                    .flatMap(category -> category.getItemCategories().stream())
                    .collect(Collectors.toSet()));
//            itemRepository.save(item);
            categoryRepository.saveAll(categorySet);
            return Optional.of(item);
        }

        return Optional.empty();
    }

    public Category addCategory(final Item item, final Category category) {
        item.addCategory(category);
        itemCategoryRepository.saveAll(category.getItemCategories());
        itemRepository.save(item);
        return categoryRepository.save(category);
    }

    public Category removeCategory(final Item item, final Category category) {
        itemCategoryRepository.deleteAll(item.clearCategories());
        itemRepository.save(item);
        return categoryRepository.save(category);
    }

}
