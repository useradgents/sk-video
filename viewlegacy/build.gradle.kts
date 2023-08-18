plugins {
    kotlin("multiplatform")
    id("tech.skot.library-viewlegacy")
    signing
}

android {
    namespace = "tech.skot.libraries.skvideo.viewlegacy"
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    api("com.google.android.exoplayer:exoplayer:2.18.6")
    api("com.google.android.exoplayer:extension-mediasession:2.18.6")
    implementation("androidx.lifecycle:lifecycle-process:2.6.1")
}


val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}
