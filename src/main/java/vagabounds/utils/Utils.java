package vagabounds.utils;

import java.util.Collection;
import java.util.function.Predicate;

public class Utils {
    public static <T> T find(Collection<T> collection, Predicate<T> predicate) {
        return collection.stream()
            .filter(predicate)
            .findFirst()
            .orElse(null);
    }

    public enum JobType {
        INTERNSHIP,
        TRAINEE,
        FULL_TIME,
        PART_TIME
    }

}

