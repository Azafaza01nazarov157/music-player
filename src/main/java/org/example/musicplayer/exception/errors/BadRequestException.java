package org.example.musicplayer.exception.errors;


import org.example.musicplayer.exception.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends AbstractException {
    public BadRequestException(ErrorDto error) {
        super(error);
    }

    public BadRequestException(List<ErrorDto> errors) {
        super(errors);
    }

    public BadRequestException() {
    }
}