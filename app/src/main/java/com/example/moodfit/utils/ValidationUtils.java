package com.example.moodfit.utils;

import java.util.regex.Pattern;

/**
 * Utility class for input validation
 */
public class ValidationUtils {

    // Username validation constants
    private static final int USERNAME_MIN_LENGTH = 2;
    private static final int USERNAME_MAX_LENGTH = 20;
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-zA-Z0-9_\\s]+$");

    // Reserved usernames to avoid
    private static final String[] RESERVED_USERNAMES = {
            "admin", "user", "test", "guest", "null", "undefined", "anonymous"
    };

    /**
     * Validate username according to app requirements
     */
    public static boolean isUsernameValid(String username) {
        if (username == null || username.trim().isEmpty()) {
            return false;
        }

        String trimmedUsername = username.trim();

        // Check length
        if (trimmedUsername.length() < USERNAME_MIN_LENGTH ||
                trimmedUsername.length() > USERNAME_MAX_LENGTH) {
            return false;
        }

        // Check pattern (letters, numbers, underscore, space only)
        if (!USERNAME_PATTERN.matcher(trimmedUsername).matches()) {
            return false;
        }

        // Check for reserved usernames
        String lowerUsername = trimmedUsername.toLowerCase();
        for (String reserved : RESERVED_USERNAMES) {
            if (reserved.equals(lowerUsername)) {
                return false;
            }
        }

        // Check that it's not just spaces or underscores
        if (trimmedUsername.replaceAll("[\\s_]", "").isEmpty()) {
            return false;
        }

        return true;
    }

    /**
     * Get specific validation error message for username
     */
    public static String getUsernameValidationError(String username) {
        if (username == null || username.trim().isEmpty()) {
            return "Please enter a username";
        }

        String trimmedUsername = username.trim();

        if (trimmedUsername.length() < USERNAME_MIN_LENGTH) {
            return "Username must be at least " + USERNAME_MIN_LENGTH + " characters";
        }

        if (trimmedUsername.length() > USERNAME_MAX_LENGTH) {
            return "Username must be less than " + USERNAME_MAX_LENGTH + " characters";
        }

        if (!USERNAME_PATTERN.matcher(trimmedUsername).matches()) {
            return "Username can only contain letters, numbers, spaces, and underscores";
        }

        String lowerUsername = trimmedUsername.toLowerCase();
        for (String reserved : RESERVED_USERNAMES) {
            if (reserved.equals(lowerUsername)) {
                return "This username is not available";
            }
        }

        if (trimmedUsername.replaceAll("[\\s_]", "").isEmpty()) {
            return "Username must contain at least one letter or number";
        }

        return "Username is valid";
    }

    /**
     * Sanitize username for display (trim and clean)
     */
    public static String sanitizeUsername(String username) {
        if (username == null) return "";

        return username.trim()
                .replaceAll("\\s+", " ")  // Replace multiple spaces with single space
                .replaceAll("_{2,}", "_"); // Replace multiple underscores with single underscore
    }

    /**
     * Check if a string is null or empty
     */
    public static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Check if a string contains only allowed characters
     */
    public static boolean containsOnlyAllowedChars(String str, String allowedCharsRegex) {
        if (isEmpty(str)) return false;
        return Pattern.matches(allowedCharsRegex, str);
    }

    /**
     * Capitalize first letter of each word
     */
    public static String capitalizeWords(String str) {
        if (isEmpty(str)) return str;

        String[] words = str.toLowerCase().split("\\s+");
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < words.length; i++) {
            if (i > 0) result.append(" ");
            if (words[i].length() > 0) {
                result.append(Character.toUpperCase(words[i].charAt(0)));
                if (words[i].length() > 1) {
                    result.append(words[i].substring(1));
                }
            }
        }

        return result.toString();
    }
}
