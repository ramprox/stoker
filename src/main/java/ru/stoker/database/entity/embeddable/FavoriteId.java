package ru.stoker.database.entity.embeddable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.Advertisement;
import ru.stoker.database.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Embeddable
public class FavoriteId implements Serializable {

    public static final String FK_FAVORITES_USER_ID_USERS_ID = "fk_favorites_user_id_users_id";

    public static final String FK_FAVORITES_ADS_ID_ADSS_ID = "fk_favorites_ads_id_adss_id";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = FK_FAVORITES_USER_ID_USERS_ID))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    @Getter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "advertisement_id", foreignKey = @ForeignKey(name = FK_FAVORITES_ADS_ID_ADSS_ID))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Advertisement advertisement;

    public FavoriteId(Long userId, Long advtId) {
        User user = new User();
        user.setId(userId);
        this.user = user;
        Advertisement advt = new Advertisement();
        advt.setId(advtId);
        this.advertisement = advt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FavoriteId that)) return false;
        return Objects.equals(getUser(), that.getUser())
                && Objects.equals(getAdvertisement(), that.getAdvertisement());
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, advertisement);
    }
}
