package net.hypr.doki.utils;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtils {
    private static final Pattern timePattern = Pattern.compile("(\\d+)(?:([dhms]))?");
    public static Duration parseDuration(String input) throws IllegalArgumentException {
        Matcher matcher = timePattern.matcher(input);

        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid duration format");
        }

        int value = Integer.parseInt(matcher.group(1));
        String unit = matcher.group(2);

        return switch (unit == null || unit.isEmpty() ? "m" : unit.toLowerCase()) {
            case "d" -> Duration.ofDays(value);
            case "h" -> Duration.ofHours(value);
            case "m" -> Duration.ofMinutes(value);
            case "s" -> Duration.ofSeconds(value);
            default -> throw new IllegalArgumentException("Invalid unit");
        };
    }
}
