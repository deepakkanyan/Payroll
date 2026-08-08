plugins {
    `kotlin-dsl`
}

group = "com.connect.payroll.buildlogic"

kotlin {
    jvmToolchain(17)
}

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.compose.compiler.gradlePlugin)
    implementation(libs.ksp.gradlePlugin)
    implementation(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "payroll.android.application"
            implementationClass = "com.connect.payroll.buildlogic.AndroidApplicationConventionPlugin"
        }
        register("androidLibrary") {
            id = "payroll.android.library"
            implementationClass = "com.connect.payroll.buildlogic.AndroidLibraryConventionPlugin"
        }
        register("androidCompose") {
            id = "payroll.android.compose"
            implementationClass = "com.connect.payroll.buildlogic.AndroidComposeConventionPlugin"
        }
        register("jvmLibrary") {
            id = "payroll.jvm.library"
            implementationClass = "com.connect.payroll.buildlogic.JvmLibraryConventionPlugin"
        }
        register("hilt") {
            id = "payroll.hilt"
            implementationClass = "com.connect.payroll.buildlogic.HiltConventionPlugin"
        }
        register("androidRoom") {
            id = "payroll.android.room"
            implementationClass = "com.connect.payroll.buildlogic.AndroidRoomConventionPlugin"
        }
        register("androidRoomEntities") {
            id = "payroll.android.room.entities"
            implementationClass = "com.connect.payroll.buildlogic.AndroidRoomEntitiesConventionPlugin"
        }
        register("test") {
            id = "payroll.test"
            implementationClass = "com.connect.payroll.buildlogic.TestConventionPlugin"
        }
    }
}
