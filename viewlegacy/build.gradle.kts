plugins {
    kotlin("multiplatform")
    id("tech.skot.library-viewlegacy")
    signing
}

dependencies {
    api("com.google.android.exoplayer:exoplayer:2.18.6")
    implementation("androidx.lifecycle:lifecycle-process:2.6.1")
}


val javadocJar by tasks.registering(Jar::class) {
    archiveClassifier.set("javadoc")
}

if (!localPublication) {
    val publication = getPublication(project)
    publishing {
        publications.withType<MavenPublication> {
            artifact(javadocJar.get())

            pom {
                name.set("SK-Video " + project.name)
                description.set("${project.name} module for SK-Video skot library")
                url.set("https://github.com/skot-framework/sk-video")
                licenses {
                    license {
                        name.set("Apache 2.0")
                        url.set("https://www.apache.org/licenses/LICENSE-2.0")
                    }
                }
                developers {
                    developer {
                        id.set("MathieuScotet")
                        name.set("Mathieu Scotet")
                        email.set("mscotet.lmit@gmail.com")
                    }
                }
                scm {
                    connection.set("scm:git:github.com/skot-framework/sk-video.git")
                    developerConnection.set("scm:git:ssh://github.com/skot-framework/sk-video.git")
                    url.set("https://github.com/skot-framework/sk-video/tree/master")
                }
            }
        }
    }

    signing {
        useInMemoryPgpKeys(
            publication.signingKeyId,
            publication.signingKey,
            publication.signingPassword
        )
        this.sign(publishing.publications)
    }

}