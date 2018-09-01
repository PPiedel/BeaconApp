package pl.yahoo.pawelpiedel.util.rx.scheduler;

public class SchedulerUtils {

    public static <T> IoMainScheduler<T> ioToMain() {
        return new IoMainScheduler<>();
    }
}
