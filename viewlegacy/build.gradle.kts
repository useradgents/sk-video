plugins {
    id("tech.skot.library-viewlegacy")
    signing
}

android {
    compileSdk { version = release(36) }
    namespace = "tech.skot.libraries.skvideo.viewlegacy"
}
kotlin {
    jvmToolchain(17)
    compilerOptions {
        apiVersion.set(org.jetbrains.kotlin.gradle.dsl.KotlinVersion.KOTLIN_2_3)
    }
}

dependencies {
    api(libs.media3.exoplayer)
    api(libs.media3.ui)
    api(libs.media3.session)
    implementation(libs.androidx.media)
    implementation(libs.lifecycle.process)
}


val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}
