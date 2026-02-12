package nana.TrialTrove.service;

public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException() {
        super("정원 초과");
    }
}
