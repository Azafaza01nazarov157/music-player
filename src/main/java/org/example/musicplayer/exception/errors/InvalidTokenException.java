package org.example.musicplayer.exception.errors;

import org.example.musicplayer.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class InvalidTokenException extends AbstractException {
    public InvalidTokenException(ErrorDto error) {
        super(error);
    }

    public InvalidTokenException(List<ErrorDto> errors) {
        super(errors);
    }

    public InvalidTokenException() {
    }
}