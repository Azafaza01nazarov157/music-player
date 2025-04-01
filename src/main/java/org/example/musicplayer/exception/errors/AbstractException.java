package org.example.musicplayer.exception.errors;



import lombok.Getter;
import lombok.Setter;
import org.example.musicplayer.exception.dto.ErrorDto;

import java.util.List;

@Setter
@Getter
public abstract class AbstractException extends RuntimeException {

    protected List<ErrorDto> errors;

    public AbstractException(ErrorDto error) {
        super(error.getMessage());
        this.errors = List.of(error);
    }

    public AbstractException(ErrorDto error, Throwable cause) {
        super(error.getMessage(), cause);
        this.errors = List.of(error);
    }

    public AbstractException(List<ErrorDto> errors) {
        super(errors.toString());
        this.errors = errors;
    }

    public AbstractException() {
    }
}
