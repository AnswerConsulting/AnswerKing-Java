package com.answerdigital.benhession.academy.answerkingweek2.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.ManyToOne;
import javax.persistence.JoinColumn;

import java.util.Objects;

@Entity
@Table(name = "item_category")
public class ItemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "id")
    private Item item;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    private Category category;

    ItemCategory(final Item item, final Category category) {
        this.item = item;
        this.category = category;
        category.addItemCategory(this);
    }

    public ItemCategory() {

    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public Item getItem() {
        return item;
    }

    public Category getCategory() {
        return category;
    }

    public void setItem(final Item item) {
        this.item = item;
    }

    public void setCategory(final Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final ItemCategory that = (ItemCategory) o;
        return item.equals(that.item) && category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, category);
    }

    @Override
    public String toString() {
        return "ItemCategory{" +
                "id=" + id +
                ", item=" + item +
                ", category=" + category +
                '}';
    }
}
