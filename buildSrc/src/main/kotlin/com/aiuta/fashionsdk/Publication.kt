package com.aiuta.fashionsdk

import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import com.aiuta.fashionsdk.groupId as currentGroupId
import com.aiuta.fashionsdk.artifactId as currentArtifactId
import com.aiuta.fashionsdk.versionName as currentVersionName
import com.vanniktech.maven.publish.MavenPublishBaseExtension

fun Project.setupPublishing(
    action: MavenPublishBaseExtension.() -> Unit = {},
) {
    extensions.configure<MavenPublishBaseExtension> {
        publishToMavenCentral()
        signAllPublications()
        action()

        coordinates(
            groupId = currentGroupId,
            artifactId =  currentArtifactId,
            version = currentVersionName,
        )

        pom {
            name.set(project.pomName)
            description.set(project.pomDescription)
            url.set(project.pomUrl)

            issueManagement {
                url.set(project.pomUrlIssue)
            }

            scm {
                url.set(project.scmUrl)
                connection.set(project.scmConnection)
                developerConnection.set(project.scmDevConnection)
            }

            licenses {
                license {
                    name.set(project.licenceName)
                    url.set(project.licenceUrl)
                    distribution.set(project.licenceDist)
                }
            }

            developers {
                developer {
                    id.set("MishkaV")
                    name.set("Mikhail Vorozhtcov")
                    email.set("mike.vorozhtcov@aiuta.com")
                }
            }
        }
    }
}
