package ru.stoker.util;

import ru.stoker.database.entity.embeddable.Credentials;

public class CredentialsBuilder implements EntityBuilder<Credentials> {

    private String login = "login";

    private String password = "password";

    public static CredentialsBuilder credentials() {
        return new CredentialsBuilder();
    }

    private CredentialsBuilder() { }

    private CredentialsBuilder(CredentialsBuilder builder) {
        this.login = builder.login;
        this.password = builder.password;
    }

    public CredentialsBuilder withLogin(String login) {
        CredentialsBuilder builder = new CredentialsBuilder(this);
        builder.login = login;
        return builder;
    }

    public CredentialsBuilder withPassword(String password) {
        CredentialsBuilder builder = new CredentialsBuilder(this);
        builder.password = password;
        return builder;
    }

    @Override
    public Credentials build() {
        return new Credentials(login, password);
    }
}
