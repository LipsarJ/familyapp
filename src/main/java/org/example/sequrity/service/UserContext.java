package org.example.sequrity.service;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.example.dto.response.ResponseUserDto;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Component
@RequestScope
@Getter
@Setter(AccessLevel.PACKAGE)
public class UserContext {
    private ResponseUserDto userDto;
}

