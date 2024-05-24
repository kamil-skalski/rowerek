package pl.rowerek.common;

public class BikeIsNotAvailableException extends RuntimeException {

    public BikeIsNotAvailableException(BikeId bikeId, String reason) {
        super("Bike is currently not available, bikeId: " + bikeId.id() + ", reason: " + reason);
    }
}
