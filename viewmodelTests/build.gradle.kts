plugins {
    kotlin("multiplatform")
    signing
}


kotlin {
    jvm("jvm")

    sourceSets {
        val jvmMain by getting {

            kotlin.srcDir("src/jvmMain/kotlin")

            dependencies {
                implementation(project(":viewmodel"))
                implementation(libs.skot.viewmodelTests)
                implementation(libs.skot.core.jvm)
            }
        }
    }
}
