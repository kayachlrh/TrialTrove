package nana.TrialTrove.exception;

public class CapacityExceededException extends RuntimeException {
    public CapacityExceededException() {
        super("정원 초과");
    }
}
