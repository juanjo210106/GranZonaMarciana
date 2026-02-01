plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.granzonamarciana"


    compileSdk = 34


    defaultConfig {
        applicationId = "com.granzonamarciana"
        minSdk = 26


        targetSdk = 34


        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // O VERSION_11 si prefieres
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // --- ERROR CORREGIDO: He quitado la línea 'libs.room.runtime.jvm' ---

    // Configuración CORRECTA de Room para Android
    val roomVersion = "2.6.1"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Otras librerías
    implementation("de.svenkubiak:jBCrypt:0.4.3")
    implementation("com.squareup.picasso:picasso:2.8")
}