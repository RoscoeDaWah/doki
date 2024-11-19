package net.hypr.doki.utils;

import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtils {
    private static final Pattern DURATION_PATTERN = Pattern.compile(
            "(\\d+d\\s*)?(\\d+h\\s*)?(\\d+m\\s*)?(\\d+s\\s*)?");

    public static Duration parseDuration(String durationString) {
        durationString = durationString.trim();

        Matcher matcher = DURATION_PATTERN.matcher(durationString);
        long days = 0;
        long hours = 0;
        long minutes = 0;
        long seconds = 0;

        if (matcher.matches()) {
            // Extract days if present
            if (matcher.group(1) != null) {
                days = Long.parseLong(matcher.group(1).replace("d", "").trim());
            }

            // Extract hours if present
            if (matcher.group(2) != null) {
                hours = Long.parseLong(matcher.group(2).replace("h", "").trim());
            }

            // Extract minutes if present
            if (matcher.group(3) != null) {
                minutes = Long.parseLong(matcher.group(3).replace("m", "").trim());
            }

            // Extract seconds if present
            if (matcher.group(4) != null) {
                seconds = Long.parseLong(matcher.group(4).replace("s", "").trim());
            }
        }

        return Duration.ofDays(days)
                .plusHours(hours)
                .plusMinutes(minutes)
                .plusSeconds(seconds);
    }

    public static boolean isDurationBetween(Duration target, Duration lowerThreshold, Duration upperThreshold) {
        if (target == lowerThreshold || target == upperThreshold) return true;
        return !target.isNegative() &&
                (target.compareTo(lowerThreshold) >= 0) &&
                (target.compareTo(upperThreshold) <= 0);
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
