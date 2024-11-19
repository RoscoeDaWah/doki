package net.hypr.doki.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
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

    public static String getTimeDifference(OffsetDateTime offsetDateTime) {
        if (offsetDateTime == null) return "0 seconds";

        // Get the current Instant (current time)
        Instant now = Instant.now();

        // Calculate the duration between the current time and the given OffsetDateTime
        Duration duration = Duration.between(now, offsetDateTime.toInstant());

        // Get the difference in various units
        long days = duration.toDays();
        long hours = duration.toHours() % 24;  // Modulo 24 to get hours within a single day
        long minutes = duration.toMinutes() % 60;  // Modulo 60 to get minutes within a single hour
        long seconds = duration.getSeconds() % 60;  // Modulo 60 to get remaining seconds

        // Build the formatted time difference string
        StringBuilder result = new StringBuilder();

        // Append each time unit if it is non-zero
        if (days > 0) {
            result.append(days).append(" day").append(days > 1 ? "s" : "").append(" ");
        }
        if (hours > 0) {
            result.append(hours).append(" hour").append(hours > 1 ? "s" : "").append(" ");
        }
        if (minutes > 0) {
            result.append(minutes).append(" minute").append(minutes > 1 ? "s" : "").append(" ");
        }
        if (seconds > 0) {
            result.append(seconds).append(" second").append(seconds > 1 ? "s" : "");
        }

        // If the duration is zero, return "0 seconds"
        if (result.isEmpty()) {
            result.append("0 second");
        }

        return result.toString().trim();
    }
}
