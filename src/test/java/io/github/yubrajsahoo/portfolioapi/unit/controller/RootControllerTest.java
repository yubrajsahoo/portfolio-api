package io.github.yubrajsahoo.portfolioapi.unit.controller;

import io.github.yubrajsahoo.portfolioapi.controller.RootController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.view.RedirectView;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Unit: Root Controller")
class RootControllerTest {

    @Test
    @DisplayName("Should Redirect to Health Endpoint")
    void redirectToHealth_ReturnsHealthRedirect() {
        RootController rootController = new RootController();
        RedirectView redirectView = rootController.redirectToHealth();

        assertNotNull(redirectView);
        assertEquals("/actuator/health", redirectView.getUrl());
    }
}
