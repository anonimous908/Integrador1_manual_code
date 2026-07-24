import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    js {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.shared)

            implementation(libs.compose.ui)
        }

        val webMain by creating {
            dependsOn(commonMain.get())
        }

        val wasmJsMain by getting {
            dependsOn(webMain)
        }

        val jsMain by getting {
            dependsOn(webMain)
            dependencies {
                implementation(npm("firebase", "11.6.0"))
            }
        }
    }
}