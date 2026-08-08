package com.connect.payroll.buildlogic

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

/**
 * For modules that declare Room `@Entity`/`@Dao` but don't host a `@Database` themselves — just
 * the annotations/runtime, no code-generating compiler. Room only needs the compiler wherever the
 * actual `@Database` class lives (see [AndroidRoomConventionPlugin], applied to :app), since that's
 * the one place Room generates the DAO implementations and validates the schema.
 */
class AndroidRoomEntitiesConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply("com.google.devtools.ksp")

            dependencies {
                add("implementation", libs.findLibrary("room-runtime").get())
                add("implementation", libs.findLibrary("room-ktx").get())
            }
        }
    }
}
