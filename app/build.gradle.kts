plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") //ksp kalo merah kesini
}

android {
    namespace = "com.example.linkedup"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.linkedup"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
//        testInstrumentationRunnerArguments["clearPackageData"] = "true"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.annotation)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation("com.google.android.material:material:1.8.0")
    implementation(libs.androidx.junit.ktx)
    implementation(libs.androidx.media3.test.utils)
    implementation(libs.play.services.cast.framework)
//    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation("androidx.recyclerview:recyclerview:1.3.2")

//    implementation("de.hdodenhof:circleimageview:3.1.0") //buat lingkaran

    implementation("androidx.room:room-runtime:2.5.0")
    testImplementation("androidx.room:room-testing:2.5.0")
    ksp("androidx.room:room-compiler:2.5.0")

    // api fetch retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")  // Versi Retrofit yang digunakan
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")

    // test database
//    testImplementation (libs.junit.v5112)
//    androidTestImplementation(libs.androidx.junit)
//    androidTestImplementation(libs.androidx.espresso.core)
//    androidTestImplementation(libs.androidx.runner)



    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

    implementation("androidx.fragment:fragment-ktx:1.5.6")
    // Tambahkan dependencies lain yang diperlukan

    implementation("androidx.room:room-ktx:2.5.0") //buat korotin

    //glide buat database poto profil
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

//    testImplementation ("junit:junit:5.11.2")
//    androidTestImplementation("androidx.test.ext:junit:1.2.1")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
}