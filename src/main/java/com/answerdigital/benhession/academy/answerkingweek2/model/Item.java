package com.answerdigital.benhession.academy.answerkingweek2.model;

import com.answerdigital.benhession.academy.answerkingweek2.dto.AddItemDTO;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GenerationType;
import javax.persistence.Column;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "item")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    @Column(precision = 12, scale = 2)
    @Digits(integer = 12, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Boolean available;

    @OneToMany(mappedBy = "item")
    private Set<ItemCategory> categories;

    public Item(final String name, final String description, final BigDecimal price, final boolean isAvailable) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.categories = new HashSet<>();
        this.available = isAvailable;
    }

    public Item(final AddItemDTO addItemDTO) {
        this.name = addItemDTO.getName();
        this.description = addItemDTO.getDescription();
        this.price = new BigDecimal(addItemDTO.getPrice());
        this.available = addItemDTO.isAvailable();
        this.categories = new HashSet<>();
    }

    public Item() {

    }

    public void changeCategoriesTo(final Set<Category> categoriesToAdd) {
        categories.clear();
        categories.addAll(categoriesToAdd
                .stream()
                .map(category -> new ItemCategory(this, category)).collect(Collectors.toSet())
        );
    }

    public void addCategory(final Category category) {
        categories.add(new ItemCategory(this, category));
    }

    public Set<ItemCategory> clearCategories() {
        final Set<ItemCategory> removedItemCategories = new HashSet<>(categories);
        categories.forEach(itemCategory -> itemCategory.getCategory().removeItem(this));
        categories.clear();
        return removedItemCategories;
    }

    public void remove(final Category category) {
        final Optional<ItemCategory> itemCategory = categories.stream()
                .filter(i -> i.getCategory().equals(category))
                .findFirst();

        itemCategory.ifPresent(i -> {
            i.getCategory().removeItem(this);
            categories.remove(i);
        });
    }

    public Set<Category> getCategories() {
        return categories.stream().map(ItemCategory::getCategory).collect(Collectors.toSet());
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public void setAvailable(final Boolean available) {
        this.available = available;
    }

    public void setCategories(final Set<ItemCategory> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
