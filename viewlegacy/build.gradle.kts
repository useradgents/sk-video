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
    api("com.google.android.exoplayer:exoplayer:2.19.1")
    api("com.google.android.exoplayer:extension-mediasession:2.19.1")
    implementation("androidx.lifecycle:lifecycle-process:2.10.0")
}


val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}
