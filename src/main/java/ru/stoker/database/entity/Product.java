package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.*;
import ru.stoker.database.entity.productproperties.ProductProperties;
import ru.stoker.database.util.ProductPropertiesJsonType;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "products")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = ProductPropertiesJsonType.class)
})
public class Product {

    @Setter(AccessLevel.NONE)
    @Id
    private Long id;

    @NotNull(message = "Объявление не должно быть пустым")
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "advertisement_id",
            foreignKey = @ForeignKey(name = "fk_ads_id_prod_id"),
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Advertisement advertisement;

    @NotNull(message = "Категория не должна быть пустой")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_prod_cat_id_cat_id"),
            nullable = false)
    private Category category;

    @NotNull(message = "Цена не должна быть пустой")
    @Min(value = 0, message = "Цена должна быть больше или равна 0")
    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", length = 2048)
    private String description;

    @Valid
    @Type(type = "jsonb")
    @Column(name = "properties", columnDefinition = "jsonb")
    private ProductProperties properties;

    @Size(max = 10, message = "Количество изображений не должно превышать 10")
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "product", cascade = { PERSIST, MERGE })
    private List<Attachment> attachments = new ArrayList<>();

    public Product(Category category, BigDecimal price) {
        this.category = category;
        this.price = price;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
        this.id = advertisement.getId();
    }

    public void addAttachment(Attachment attachment) {
        Objects.requireNonNull(attachment, "Изображение не должно быть пустым");
        this.attachments.add(attachment);
        attachment.setProduct(this);
    }

    public void removeAttachment(Attachment attachment) {
        if(attachments.remove(attachment)) {
            attachment.setProduct(null);
        }
    }

    public void addAttachments(Collection<Attachment> attachments) {
        attachments.forEach(this::addAttachment);
    }

    public void removeAll(Collection<Attachment> attachments) {
        attachments.forEach(this::removeAttachment);
    }

}
