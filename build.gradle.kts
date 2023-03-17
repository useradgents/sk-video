buildscript {
    repositories {
        google()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
//        mavenLocal()

    }

    dependencies {
        classpath("${Versions.frameworkGroup}:plugin:${Versions.framework}")
        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.5.30")
    }
}

plugins {
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

allprojects {

    group = Versions.group
    version = "${Versions.library}_${Versions.framework}"

    repositories {
        google()
//        mavenLocal()
        maven { setUrl("https://jitpack.io") }
        mavenCentral()
    }

    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.dokka")

}

if(!localPublication) {
    val publication = getPublication(project)

    nexusPublishing {
        repositories {
            sonatype {
                stagingProfileId.set(publication.sonatypeStagingProfileId)
                username.set(publication.ossrhUsername)
                password.set(publication.ossrhPassword)
                nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
                snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            }
        }
    }
}