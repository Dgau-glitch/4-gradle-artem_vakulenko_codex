package ru.itmo.ct.dateutil

import com.github.ajalt.clikt.core.main
import com.github.ajalt.clikt.core.subcommands

/**
 * The entry point for the date utility application.
 * This function initializes the `DateUtility` command, registers its subcommands (`DateAddCommand` and
 * `DateDifferenceCommand`), and starts the command-line interface by processing the provided arguments.
 *
 * @param args The command-line arguments passed to the program.
 */
fun main(args: Array<String>) {
    DateUtility()
        .subcommands(DateAddCommand(), DateDifferenceCommand())
        .main(args)
}
