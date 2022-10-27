package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = Category.TABLE_NAME)
public class Category {

    public static final String TABLE_NAME = "categories";
    public static final String SEQUENCE_NAME = "categories_seq";

    @Setter(AccessLevel.NONE)
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "categories_gen")
    @SequenceGenerator(name = "categories_gen", sequenceName = "categories_seq", allocationSize = 1)
    private Long id;

    @NotBlank(message = "Имя категории не должно быть пустым")
    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_cat_category_id_cat_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Category parent;

    public Category(String name) {
        this(name, null);
    }

    public Category(String name, Category parent) {
        this.name = name;
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if(!(o instanceof Category other)) return false;
        return Objects.equals(id, other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
