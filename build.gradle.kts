group = "ru.itmo.ct.dateutil"
version = "1.0.0"

plugins {
}

repositories {
}

dependencies {
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ru.itmo.ct.dateutil.MainKt"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
