plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.airbnbsimulapp_androidstudiopart"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.airbnbsimulapp_androidstudiopart"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(files("libs\\vscodepart.jar"))
    implementation(files("libs\\vscodepart.jar"))
    //   implementation(files("lib\\C:\\Users\\Nikos\\vscode-workspace.jar"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}