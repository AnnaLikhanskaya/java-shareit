package ru.practicum.shareit.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.exception.*;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.user.controller.UserController;

import java.util.Map;

@Slf4j
@RestControllerAdvice(assignableTypes = {UserController.class,
        ItemController.class,
        BookingController.class})
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleMethodArgumentNotValidException(final MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        Map<String, String> error = Map.of("error", errorMessage);
        log.warn("Validation error: {}", errorMessage);
        return error;
    }

    @ExceptionHandler(NotExsistObject.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleNotExsistObject(final NotExsistObject e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn("Not found error: {}", e.getMessage());
        return error;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUserNotFoundException(final UserNotFoundException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn("User not found error: {}", e.getMessage());
        return error;
    }

    @ExceptionHandler(DuplicateException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleDuplicateException(final DuplicateException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn("Conflict error: {}", e.getMessage());
        return error;
    }


    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(final InvalidRequestException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return error;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handle(final OtherDataException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return error;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handle(final IllegalArgumentException e) {
        Map<String, String> error = Map.of("error", "Unknown state: UNSUPPORTED_STATUS");
        log.warn(e.getMessage());
        return error;
    }

    @ExceptionHandler()
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handle(final ItemUnavalibleException e) {
        Map<String, String> error = Map.of("error", e.getMessage());
        log.warn(e.getMessage());
        return error;
    }


    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleException(final Exception e) {
        Map<String, String> error = Map.of("error", "Internal server error");
        log.error("Internal server error: {}", e.getMessage(), e);
        return error;
    }
}
