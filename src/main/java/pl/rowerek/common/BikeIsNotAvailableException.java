package pl.rowerek.common;

public class BikeIsNotAvailableException extends RuntimeException {

    public BikeIsNotAvailableException(String message) {
        super(message);
    }
}
