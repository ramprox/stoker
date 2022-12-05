package ru.stoker.database.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import ru.stoker.database.entity.User;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class UserEstimationId implements Serializable {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_user_id", updatable = false, insertable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_esting_by_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User estOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", updatable = false, insertable = false, nullable = false,
            foreignKey = @ForeignKey(name = "fk_user_ested_user_user_id"))
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;

    public UserEstimationId(Long estOwnerId, Long userId) {
        User owner = new User();
        owner.setId(estOwnerId);
        this.estOwner = owner;
        User user = new User();
        user.setId(userId);
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEstimationId other)) return false;
        return Objects.equals(getEstOwner(), other.getEstOwner()) &&
                Objects.equals(getUser(), other.getUser());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getEstOwner(), getUser());
    }
}
