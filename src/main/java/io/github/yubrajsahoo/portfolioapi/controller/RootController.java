package io.github.yubrajsahoo.portfolioapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Controller to redirect root requests.
 */
@Hidden
@Controller
public class RootController {

    /**
     * Redirects the root path to the health endpoint.
     *
     * @return RedirectView to /actuator/health
     */
    @GetMapping("/")
    public RedirectView redirectToHealth() {
        return new RedirectView("/actuator/health");
    }
}
