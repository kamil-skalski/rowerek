package pl.rowerek.common;

public class BikeNotFoundException extends RuntimeException {

    public static String createExceptionMessage(BikeId bikeId) {
        return String.format("Could not find bike with bikeId: %s", bikeId.id());
    }

    public BikeNotFoundException(final BikeId bikeId) {
        super(createExceptionMessage(bikeId));
    }
}
