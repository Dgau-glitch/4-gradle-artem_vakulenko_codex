package ru.itmo.ct.dateutil

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkObject
import java.io.ByteArrayOutputStream
import java.io.PrintStream
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class DateUtilityTest :
    FunSpec({
        test("MockK replaces the system clock with a fixed instant") {
            val fixedNow = Instant.parse("2024-09-02T12:00:00Z")
            val output = ByteArrayOutputStream()
            val originalOutput = System.out
            mockkObject(Clock.System)

            try {
                every { Clock.System.now() } returns fixedNow
                System.setOut(PrintStream(output))

                DateFunctions.now() shouldBe fixedNow
                DateFunctions
                    .diff(
                        DateFunctions.parseInstant("2024-09-01T12:00:00Z"),
                        DateFunctions.now(),
                    ).inWholeHours shouldBe 24
                main(arrayOf("diff", "2024-09-01T12:00:00Z"))
                output.toString().trim().lines() shouldBe
                    listOf("Difference:", "1 days", "24 hours", "1440 minutes", "86400 seconds")
            } finally {
                System.setOut(originalOutput)
                unmockkObject(Clock.System)
            }
        }
    })
