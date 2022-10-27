package ru.stoker.database.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.embeddable.FavoriteId;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "favorites")
public class Favorite {

    @Getter
    @Setter(AccessLevel.PROTECTED)
    @Valid
    @EmbeddedId
    private FavoriteId favoriteId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_favorites_user_id_users_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", updatable = false, insertable = false,
            foreignKey = @ForeignKey(name = "fk_favorites_ads_id_adss_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Advertisement advertisement;

    public Favorite(Long userId, Long advertisementId) {
        favoriteId = new FavoriteId(userId, advertisementId);
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;
        if(!(obj instanceof Favorite other)) return false;
        return Objects.equals(favoriteId, other.favoriteId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(favoriteId);
    }

}
