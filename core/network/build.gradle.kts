plugins {
    id("payroll.android.library")
    id("payroll.hilt")
    id("payroll.test")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.connect.payroll.core.network"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.kotlinx.serialization)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
}
