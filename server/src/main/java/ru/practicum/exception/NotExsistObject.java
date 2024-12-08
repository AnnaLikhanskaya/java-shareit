package ru.practicum.exception;

public class NotExsistObject extends RuntimeException {
    public NotExsistObject(String message) {
        super(message);
    }
}