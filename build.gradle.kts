import io.gitlab.arturbosch.detekt.extensions.DetektExtension
import org.jlleitschuh.gradle.ktlint.KtlintExtension

group = "ru.itmo.ct.dateutil"
version = "1.0.0"

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.dokka)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.detekt)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.implementation)
    testImplementation(libs.bundles.test.implementation)
}

val ktlintVersion = libs.versions.ktlintEngine.get()

configure<KtlintExtension> {
    version.set(ktlintVersion)
}

configure<DetektExtension> {
    config.setFrom(rootProject.file("detekt.yml"))
    buildUponDefaultConfig = true
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "ru.itmo.ct.dateutil.MainKt"
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.register("release") {
    group = "build"
    description = "Builds the executable JAR and HTML documentation."
    dependsOn(tasks.shadowJar, tasks.dokkaHtml)
}
