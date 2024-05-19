plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
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
        viewBinding = true
    }
    dataBinding {
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
    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.0.0"))
    //Realtime
    implementation("com.google.firebase:firebase-database")

    // TODO: Add the dependencies for Firebase products you want to use
    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    implementation ("com.google.android.material:material:1.12.0")


    implementation("com.google.firebase:firebase-auth:23.0.0")


    implementation ("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.google.firebase:firebase-storage:21.0.0")
    implementation ("com.firebaseui:firebase-ui-storage:7.2.0")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.28")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    implementation("androidx.leanback:leanback:1.0.0")
    implementation("androidx.activity:activity:1.9.0")
    testImplementation("junit:junit:4.13.2")
    implementation ("androidx.databinding:databinding-runtime:8.4.0")
    implementation ("androidx.fragment:fragment:1.7.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("me.shanghai.android.material ratingbar:library:1.4.0")
    //Swipe to refesh Layout
    implementation ("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    //Paging 3
    implementation ("androidx.paging:paging-runtime:3.3.0")
    //thư viện xử lí ảnh ở login
    //implementation (com.android.support:cardview-v7:28.0.0)
}