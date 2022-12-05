package ru.stoker.service.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.stoker.database.entity.User;

import java.util.Collection;
import java.util.List;

@Getter
public class StokerUserDetails implements UserDetails {

    private final Long id;

    private final String login;

    private final Collection<? extends GrantedAuthority> authorities;

    private final boolean isConfirmed;

    public StokerUserDetails(User user) {
        this.id = user.getId();
        this.login = user.getCredentials().getLogin();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole().name()));
        this.isConfirmed = user.isConfirmed();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return login;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isConfirmed;
    }
}
