plugins {
    kotlin("multiplatform")
    id("tech.skot.library-viewlegacy")
    signing
}

android {
    namespace = "tech.skot.libraries.skvideo.viewlegacy"
}
kotlin {
    jvmToolchain(17)
}

dependencies {
    api("androidx.media3:media3-exoplayer:1.9.2")
    api("androidx.media3:media3-ui:1.9.2")
    api("androidx.media3:media3-session:1.9.2")
    implementation("androidx.media:media:1.7.0")
    implementation("androidx.lifecycle:lifecycle-process:2.10.0")
}


val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}
