plugins {
    kotlin("multiplatform")
    id("tech.skot.library")
    signing
}

kotlin {
    jvmToolchain(17)
    android {
        compileSdk = 36
        namespace = "tech.skot.libraries.skvideo"
    }
}
tasks.dokkaHtmlPartial.configure {
    suppressInheritedMembers.set(true)
}

tasks.dokkaGfmPartial.configure {
    suppressInheritedMembers.set(true)
}

val dokkaOutputDir = layout.buildDirectory.dir("dokka")

tasks.named<org.jetbrains.dokka.gradle.DokkaTask>("dokkaHtml") {
    outputDirectory.set(dokkaOutputDir.map { it.asFile })
}

val deleteDokkaOutputDir by tasks.register<Delete>("deleteDokkaOutputDirectory") {
    group = "documentation"
    description = "Deletes the Dokka HTML output directory"
    delete(dokkaOutputDir)
}

val javadocJar = tasks.register<Jar>("javadocJar") {
    group = "publishing"
    description = "Assembles a jar archive containing the Javadoc/Dokka HTML documentation"
    dependsOn(deleteDokkaOutputDir, tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
    from(dokkaOutputDir)
}