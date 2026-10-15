/*
 *
 *  * Copyright (c) 2026 Yubraj Sahoo. All rights reserved.
 *
 */

package io.github.yubrajsahoo.portfolioapi.service.impl;

import io.github.yubrajsahoo.portfolioapi.entity.Role;
import io.github.yubrajsahoo.portfolioapi.entity.User;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsDescriptions;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsKeys;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsNames;
import io.github.yubrajsahoo.portfolioapi.metrics.MetricsValues;
import io.github.yubrajsahoo.portfolioapi.repository.UserRepository;
import io.github.yubrajsahoo.smf4j.api.annotation.Gauge;
import io.github.yubrajsahoo.smf4j.api.annotation.Tags;
import io.github.yubrajsahoo.smf4j.api.annotation.Timer;
import io.github.yubrajsahoo.smf4j.api.constant.Smf4jSpelConstants;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Custom implementation of UserDetailsService to load user details from the database.
 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Gauge(
            name = MetricsNames.GAUGE_LOGINS,
            description = MetricsDescriptions.TOTAL_LOGINS,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.NO_OF_USER_LOGIN),
            }
    )
    private final AtomicLong totalLogins = new AtomicLong(0);

    /**
     * Locates the user based on the username. In the actual implementation, the search
     * may possibly be case sensitive, or case insensitive depending on how the
     * implementation instance is configured. In this case, the <code>UserDetails</code>
     * object that comes back may have a username that is of a different case than what
     * was actually requested..
     *
     * @param email the username identifying the user whose data is required.
     * @return a fully populated user record (never <code>null</code>)
     * @throws UsernameNotFoundException if the user could not be found or the user has no
     *                                   GrantedAuthority
     */
    @Timer(
            name = MetricsNames.SERVICE,
            description = MetricsDescriptions.SERVICE_METHOD,
            tags = {
                    @Tags(key = MetricsKeys.API, value = MetricsValues.CUSTOM_USER_DETAILS_SERVICE),
                    @Tags(key = MetricsKeys.OPERATION, value = MetricsValues.LOAD_USER_BY_USERNAME),
                    @Tags(key = MetricsKeys.METHOD, value = Smf4jSpelConstants.METHOD_NAME),
                    @Tags(key = MetricsKeys.OUTCOME, value = MetricsValues.GENERAL_OUTCOME)
            }
    )
    @Override
    public UserDetails loadUserByUsername(@NonNull String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        // Increment the successful login gauge counter
        totalLogins.incrementAndGet();

        return org.springframework.security.core.userdetails.User.withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(prepareAuthorities(user.getRoles()))
                .build();
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void resetTotalLogins() {
        totalLogins.set(0);
    }

    private List<SimpleGrantedAuthority> prepareAuthorities(List<Role> roles) {
        return roles.stream()
                .flatMap(role -> role.getPrivileges().stream())
                .map(privilege -> new SimpleGrantedAuthority(privilege.getName()))
                .toList();
    }
}
