import com.android.build.OutputFile

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

val keyProps = `java.util`.Properties()
val keyPropsFile: File = rootProject.file("keystore/keystore.properties")
if (keyPropsFile.exists()) {
    keyProps.load(`java.io`.FileInputStream(keyPropsFile))
}

android {
    namespace = "me.fycz.fqweb"
    compileSdk = 33

    defaultConfig {
        applicationId = "me.fycz.fqweb"
        minSdk = 24
        targetSdk = 33
        versionCode = 153
        versionName = "1.5.3"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("myConfig") {
            keyAlias = keyProps["keyAlias"].toString()
            keyPassword = keyProps["keyPassword"].toString()
            storeFile = file(keyProps["storeFile"].toString())
            storePassword = keyProps["storePassword"].toString()
            enableV1Signing = true
            enableV2Signing = true
            enableV3Signing = true
        }
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            if (keyPropsFile.exists()) {
                signingConfig = signingConfigs.getByName("myConfig")
            }
        }
    }

    android.applicationVariants.all {
        val isFrpc = versionName.contains("frpc")
        outputs.map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach {
                val abi = if (isFrpc) "_" + (it.getFilter(OutputFile.ABI) ?: "armAll") else ""
                val fileName = "FQWeb_v$versionName$abi.apk"
                it.outputFileName = fileName
            }
    }

    splits {
        abi {
            reset()
            isEnable = project.hasProperty("frpc")
            include("armeabi-v7a", "arm64-v8a")
            isUniversalApk = true
        }
    }

    flavorDimensions.add("mode")

    productFlavors {
        create("app") {
            dimension = "mode"
            manifestPlaceholders["APP_CHANNEL_VALUE"] = "app"
        }

        create("frpc") {
            dimension = "mode"
            versionNameSuffix = "-frpc"
            manifestPlaceholders["APP_CHANNEL_VALUE"] = "frpc"

            ndk {
                abiFilters.add("armeabi-v7a")
                abiFilters.add("arm64-v8a")
            }
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    compileOnly(files("libs/api-82.jar"))

    //webServer
    implementation("org.nanohttpd:nanohttpd:2.3.1")

    //frpc
    "frpcImplementation"(files("libs/frpclib.aar"))

    //AppCenter
    val appCenterSdkVersion = "5.0.0"
    implementation("com.microsoft.appcenter:appcenter-analytics:$appCenterSdkVersion")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}