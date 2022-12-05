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
import java.util.*;

import static javax.persistence.CascadeType.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "products")
@TypeDefs({
        @TypeDef(name = "jsonb", typeClass = ProductPropertiesJsonType.class)
})
public class Product {

    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "advertisement_id",
            foreignKey = @ForeignKey(name = "fk_ads_id_prod_id"),
            nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Advertisement advertisement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_prod_cat_id_cat_id"),
            nullable = false)
    private Category category;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "description", length = 2048)
    private String description;

    @Type(type = "jsonb")
    @Column(name = "properties", columnDefinition = "jsonb")
    private ProductProperties properties;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "product", cascade = { PERSIST, MERGE, REMOVE }, orphanRemoval = true)
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

    public Attachment removeById(Long id) {
        for (Attachment attachment : attachments) {
            if(attachment.getId().equals(id)) {
                removeAttachment(attachment);
                return attachment;
            }
        }
        return null;
    }

    public List<Attachment> getAttachments() {
        return Collections.unmodifiableList(attachments);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product other)) return false;
        return id != null && id.equals(other.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
