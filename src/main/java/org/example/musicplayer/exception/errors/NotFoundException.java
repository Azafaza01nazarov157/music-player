package org.example.musicplayer.exception.errors;

import org.example.musicplayer.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends AbstractException {
    public NotFoundException(ErrorDto error) {
        super(error);
    }

    public NotFoundException(List<ErrorDto> errors) {
        super(errors);
    }

    public NotFoundException() {
    }
}