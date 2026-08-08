plugins {
    id("payroll.android.library")
    id("payroll.hilt")
}

android {
    namespace = "com.connect.payroll.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
}
