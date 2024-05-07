plugins {
    id("com.android.application")
}

android {
    namespace = "vn.edu.tdc.doan_d2"
    compileSdk = 34

    defaultConfig {
        applicationId = "vn.edu.tdc.doan_d2"
        minSdk = 30
        targetSdk = 33
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
    buildFeatures{
        dataBinding = true
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    // Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    // Gson (for JSON parsing)
    implementation ("com.google.code.gson:gson:2.10.1")

    // OkHttp (for networking)


    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    //Swipe to refesh Layout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.0")
    //thư viện xử lí ảnh ở login
    //implementation (com.android.support:cardview-v7:28.0.0)
}