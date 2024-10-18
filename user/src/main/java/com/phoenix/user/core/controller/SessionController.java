package com.phoenix.user.core.controller;

import com.phoenix.common.annotation.AuthorizationRequired;
import com.phoenix.common.enumeration.Role;
import com.phoenix.user.core.service.SessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class SessionController {
    final SessionService sessionService;

    @PutMapping("/inner/session")
    @AuthorizationRequired(Role.VISITOR)
    String setDefaultSession() {
        return sessionService.setSession();
    }

    @GetMapping("/inner/token/{sessionId}")
    @AuthorizationRequired(Role.VISITOR)
    String generateToken(@PathVariable String sessionId) {
        return sessionService.generateToken(sessionId);
    }
}
