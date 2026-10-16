plugins {
    id("tech.skot.library-viewlegacy")
    signing
}

android {
    namespace = "tech.skot.libraries.skvideo.viewlegacy"
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
