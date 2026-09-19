package ru.itmo.ct.dateutil

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.*
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

/**
 * Utility object for performing various date and time operations, such as calculating the difference
 * between two dates, adding a time duration to a given date, and parsing date-time strings into `Instant`.
 */
@OptIn(ExperimentalTime::class)
object DateFunctions {
    /**
     * Calculates the difference between two `Instant` objects.
     *
     * @param date1 The first date (earlier date).
     * @param date2 The second date (later date).
     * @return The duration between `date1` and `date2`. The result will be positive if `date2` is later than `date1`.
     */
    fun diff(
        date1: Instant,
        date2: Instant,
    ): Duration = date2 - date1

    /**
     * Returns the current date and time as an `Instant`.
     *
     * @return The current `Instant` representing the current system time.
     */
    fun now(): Instant = Clock.System.now()

    /**
     * Adds a specified amount of time to a given date, based on the provided unit.
     *
     * @param date The base date to which the duration will be added.
     * @param amount The amount of time to add (can be negative to subtract time).
     * @param unit The unit of time to add. This can be one of the following values:
     *             "seconds", "minutes", "hours", or "days".
     * @return The resulting `Instant` after the specified duration is added.
     * @throws IllegalArgumentException If the `unit` is not one of the supported values.
     */
    fun addToDate(
        date: Instant,
        amount: Long,
        unit: String,
    ): Instant =
        date +
            when (unit) {
                "seconds" -> amount.seconds
                "minutes" -> amount.minutes
                "hours" -> amount.hours
                "days" -> amount.days
                else -> Duration.ZERO
            }

    /**
     * Parses a string representation of a date-time and converts it to an `Instant`.
     * The string can be in ISO 8601 format (e.g., "yyyy-MM-dd HH:mm:ss") or a variation with spaces
     * (e.g., "2024-09-20 12:23:55").
     *
     * @param datetime The string representation of the date-time to parse.
     * @return The parsed `Instant` corresponding to the given date-time string.
     * @throws IllegalArgumentException If the provided string cannot be parsed into a valid date-time.
     */
    fun parseInstant(datetime: String): Instant {
        val formattedDatetime =
            datetime
                .trim()
                .replaceFirst(" ", "T")
                .replace(" ", "")

        // Try parsing the datetime string into Instant or LocalDateTime
        return catchToNull { Instant.parse(datetime) }
            ?: catchToNull { Instant.parse(formattedDatetime) }
            ?: (catchToNull { LocalDateTime.parse(datetime) } ?: catchToNull { LocalDateTime.parse(formattedDatetime) })
                ?.toInstant(TimeZone.currentSystemDefault())
            ?: throw IllegalArgumentException("Invalid datetime")
    }
}

/**
 * Utility function to safely execute a block of code and return the result or null if an exception occurs.
 *
 * @param f The function to execute.
 * @return The result of the function if successful, or `null` if an exception was thrown.
 */
private inline fun <reified T> catchToNull(f: () -> T) = runCatching { f() }.getOrNull()
