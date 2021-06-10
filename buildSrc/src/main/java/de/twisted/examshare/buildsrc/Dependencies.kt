package de.twisted.examshare.buildsrc

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:3.6.1"

    const val junit = "junit:junit:4.13"
    const val mockito = "org.mockito:mockito-core:3.1.0"

    const val timber = "com.jakewharton.timber:timber:4.7.1"

    const val fotoapparat = "io.fotoapparat:fotoapparat:2.6.0"

    object Rx {
        const val kotlin = "io.reactivex.rxjava2:rxkotlin:2.4.0"
        const val android = "io.reactivex.rxjava2:rxandroid:2.1.1"
    }

    object Dagger {
        private const val version = "2.27"
        const val dagger = "com.google.dagger:dagger:$version"
        const val androidSupport = "com.google.dagger:dagger-android-support:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"
        const val androidProcessor = "com.google.dagger:dagger-android-processor:$version"
    }

    object OkHttp {
        private const val version = "4.7.2"
        const val okhttp = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val retrofit = "com.squareup.retrofit2:retrofit:$version"
        const val rxAdapter = "com.squareup.retrofit2:adapter-rxjava2:$version"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:$version"
    }

    object Glide {
        private const val version = "4.11.0"
        const val glide = "com.github.bumptech.glide:glide:$version"
        const val okhttpAdapter = "com.github.bumptech.glide:okhttp3-integration:$version"
        const val compiler = "com.github.bumptech.glide:compiler:$version"
    }

    object Kotlin {
        private const val version = "1.3.61"
        const val gradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:$version"
        const val stdlib = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"
    }

    object Google {
        const val material = "com.google.android.material:material:1.2.0-beta01"

        const val firebaseCore = "com.google.firebase:firebase-core:17.4.2"
        const val firebaseMessaging = "com.google.firebase:firebase-messaging:20.2.0"

        const val gmsGoogleServices = "com.google.gms:google-services:4.3.3"

        const val gson = "com.google.code.gson:gson:2.8.5"
    }

    object AndroidX {

        object Room {
            private const val version = "2.2.5"
            const val runtime = "androidx.room:room-runtime:$version"
            const val rxjava = "androidx.room:room-rxjava2:$version"
            const val compiler = "androidx.room:room-compiler:$version"
            const val testing = "androidx.room:room-testing:$version"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.2.0-beta01"
        const val preference = "androidx.preference:preference:1.1.1"
        const val constraintlayout = "androidx.constraintlayout:constraintlayout:2.0.0-beta4"
        const val swiperefresh = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0-rc01"

        const val coreKtx = "androidx.core:core-ktx:1.3.0-rc01"

        object Fragment {
            private const val version = "1.2.4"
            const val fragmentKtx = "androidx.fragment:fragment-ktx:$version"
        }

        object Test {
            private const val version = "1.2.0"
            const val core = "androidx.test:core:$version"
            const val runner = "androidx.test:runner:$version"
            const val espressoCore = "androidx.test.espresso:espresso-core:3.2.0"
        }
    }

    const val imageZoom = "it.sephiroth.android.library.imagezoom:imagezoom:2.3.0"

    const val inkPageIndicator = "com.pacioianu.david:ink-page-indicator:1.3.0"

    const val pageIndicator = "com.romandanylyk:pageindicatorview:1.0.2"
}