package org.example.musicplayer.exception.errors;

import org.example.musicplayer.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class IllegalArgumentException extends AbstractException {
    public IllegalArgumentException(ErrorDto error) {
        super(error);
    }

    public IllegalArgumentException(List<ErrorDto> errors) {
        super(errors);
    }

    public IllegalArgumentException() {
    }
}