import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleServices)
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_11
    }
}
dependencies {
    implementation(projects.shared)
    implementation("io.insert-koin:koin-core:4.0.0")
    implementation("io.insert-koin:koin-android:4.0.0")

    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)

    implementation(libs.compose.uiToolingPreview)
    debugImplementation(libs.compose.uiTooling)
}

android {
    namespace = "org.example.project"
    compileSdk = 37

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = 24
        targetSdk = 37
        versionCode = 3
        versionName = project.properties["app.version"].toString()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

val generateMockGoogleServicesIfNeeded by tasks.registering {
    val projectDirLayout = layout.projectDirectory
    doLast {
        val googleServicesFile = projectDirLayout.file("google-services.json").asFile
        if (!googleServicesFile.exists()) {
            val srcGoogleServices = projectDirLayout.file("src/debug/google-services.json").asFile
            if (!srcGoogleServices.exists()) {
                googleServicesFile.writeText("""
                {
                  "project_info": {
                    "project_number": "000000000000",
                    "project_id": "codenest-placeholder",
                    "storage_bucket": "codenest-placeholder.appspot.com"
                  },
                  "client": [
                    {
                      "client_info": {
                        "mobilesdk_app_id": "1:000000000000:android:0000000000000000",
                        "android_client_info": {
                          "package_name": "org.example.project"
                        }
                      },
                      "api_key": [
                        {
                          "current_key": "AIzaSyDummyKeyForGoogleServicesPluginBuild"
                        }
                      ]
                    }
                  ],
                  "configuration_version": "1"
                }
                """.trimIndent())
            }
        }
    }
}

tasks.matching { it.name.startsWith("process") && it.name.endsWith("GoogleServices") }.configureEach {
    dependsOn(generateMockGoogleServicesIfNeeded)
}