package com.hot.place.security;

import com.hot.place.model.user.Email;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * 인증 된 사용자 (통행증 역할)
 */
public class JwtAuthentication {

    public final Long id;

    public final Email email;

    public final String name;

    JwtAuthentication(Long id, Email email, String name) {
        checkArgument(id != null, "id must be provided.");
        checkArgument(email != null, "email must be provided.");
        checkArgument(name != null, "name must be provided.");

        this.id = id;
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("email", email)
                .toString();
    }
}
