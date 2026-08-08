plugins {
    id("payroll.android.library")
    id("payroll.android.compose")
    id("payroll.hilt")
    id("payroll.test")
}

android {
    namespace = "com.connect.payroll.another"
}

dependencies {
    implementation(project(":core:designsystem"))
}
