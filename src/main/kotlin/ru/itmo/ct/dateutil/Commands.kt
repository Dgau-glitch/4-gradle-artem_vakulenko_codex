package ru.itmo.ct.dateutil

import com.github.ajalt.clikt.core.*
import com.github.ajalt.clikt.parameters.arguments.*
import com.github.ajalt.clikt.parameters.types.*
import kotlin.time.ExperimentalTime

/**
 * Main command class for the date utility. This is the root command that doesn't perform any operations itself.
 * It serves as a container for subcommands.
 */
class DateUtility : CliktCommand() {
    override fun run() = Unit
}

/**
 * Command for adding a specified amount of time to a given date.
 * This command takes a date, an amount, and a unit of time (seconds, minutes, hours, or days),
 * and calculates the resulting date after the specified duration is added.
 */
@OptIn(ExperimentalTime::class)
class DateAddCommand : CliktCommand("add") {
    /**
     * Custom help message for the DateAddCommand.
     * Provides information about the purpose of the command.
     */
    override fun help(context: Context) = "Add some duration to a given date and time"

    private val datetime: String by argument(help = "The date and time in the format yyyy-MM-dd HH:mm:ss or same")
    private val amount: Long by argument(help = "Amount of time to add").long()
    private val unit: String by argument(help = "Unit of time (seconds, minutes, hours, days)")
        .choice("seconds", "minutes", "hours", "days")

    /**
     * Runs the command, parsing the provided date, adding the specified duration, and printing the resulting date.
     */
    override fun run() {
        val parsedDate = DateFunctions.parseInstant(datetime)
        val newDate = DateFunctions.addToDate(parsedDate, amount, unit)
        println("New Date: $newDate")
    }
}

/**
 * Command for calculating the difference between two dates, optionally with time.
 * This command compares two dates and outputs the difference in terms of days, hours, minutes, and seconds.
 */
@OptIn(ExperimentalTime::class)
class DateDifferenceCommand : CliktCommand("diff") {
    /**
     * Custom help message for the DateDifferenceCommand.
     * Provides information about the purpose of the command.
     */
    override fun help(context: Context) = "Find the difference between two dates with time"

    private val date1: String by argument(help = "The first date in the format yyyy-MM-dd HH:mm:ss or same")
    private val date2: String? by argument(help = "The second date in the format yyyy-MM-dd HH:mm:ss or same")
        .optional()

    /**
     * Runs the command, calculating the difference between two dates in terms of days, hours, minutes, and seconds.
     * The second date is optional; if not provided, the current date and time are used.
     */
    override fun run() {
        // Parse both input dates
        val parsedDate1 = DateFunctions.parseInstant(date1)
        val parsedDate2 = date2?.let { DateFunctions.parseInstant(it) } ?: DateFunctions.now()

        // Calculate the difference in days
        val difference = DateFunctions.diff(parsedDate1, parsedDate2)

        val totalDays = difference.inWholeDays
        val totalHours = difference.inWholeHours
        val totalMinutes = difference.inWholeMinutes
        val totalSeconds = difference.inWholeSeconds

        // Print the result in separate lines
        println("Difference:")
        println("$totalDays days")
        println("$totalHours hours")
        println("$totalMinutes minutes")
        println("$totalSeconds seconds")
    }
}
