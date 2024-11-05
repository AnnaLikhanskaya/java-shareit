package ru.practicum.shareit.exception;

public class NotExsistObject extends RuntimeException {
    public NotExsistObject(String message) {
        super(message);
    }
}