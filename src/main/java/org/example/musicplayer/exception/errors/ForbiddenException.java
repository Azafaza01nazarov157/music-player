package org.example.musicplayer.exception.errors;

import org.example.musicplayer.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenException extends RuntimeException {

    private final ErrorDto errorDto;

    public ForbiddenException(ErrorDto errorDto) {
        super(errorDto.getMessage());
        this.errorDto = errorDto;
    }

    public ErrorDto getErrorDto() {
        return errorDto;
    }
} 