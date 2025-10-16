# ARKeasyTools : Tools for easy Kotlin Coding

---

# 1> Easy liquidGL

A Kotlin library for Jetpack Compose that provides a simple DSL wrapper around the amazing **[Android Liquid Glass](https://github.com/Kyant0/AndroidLiquidGlass)** and **Capsule** libraries.

This project **does not contain the original Kyant0 source** but wraps it for easier integration in launcher and app projects.

``P.S. Only update at releases tagged Main do not upadate at Alpha or Small Releases as they come  more often. This point is just for saving your sanity. Also if any small or alpha release breaks it will be removed.
Why? Cause Before I add a new file or feature I will create a new release so that if someone wants to tweak that themselves they can do that. ``

---

### Features

- `liquidButton` – Liquid-style button with press animations.
- `liquidSlider` – Smooth liquid glass slider.
- `liquidToggle` – Toggle switch with liquid effect.
- `liquidBottomTabs` – Bottom tabs with liquid glass effect.
- `liquidMenu` - Menu with liquid glass effect.
- Fully composable DSL for easy, clean usage.

---

### Credits
-------

* Original **Liquid Glass**: [Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass?utm_source=chatgpt.com)
* Original **Capsule** library: [Kyant0/Capsule](https://github.com/Kyant0/Capsule?utm_source=chatgpt.com)

> This library is a **wrapper** and does not copy the original source code. All credits go to the original author Kyant0.

### License
-------

This project is Apache licensed. For the original Liquid Glass library license, see [here](https://github.com/Kyant0/AndroidLiquidGlass/blob/main/LICENSE.md).

---

### Installation

``P.S. If you really need a low based project use Alpha before stable as they are the best for rigit and hardcoding developers who love to get stuck in tiny problems. If you are a begeinner wait for stable to role out``

Create a new project and move the two folders under the com directory which were downloaded form this repository under the com directory of your project. Also exchange the res folder in your project with the res folder provided:

```

com
|--arkeverything
|--kyant

```

Make the following changes to your AndroidMnifest.xml

```xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true">
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
```

Make sure to add the following to your ``settings.gradle.kts``

```gradle

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

```

Add the Following dependencies (replace `<version>` with the release version):

```gradle
        
        implementation("com.github.Kyant0:AndroidLiquidGlass:1.0.0-beta02")
        implementation("com.github.Kyant0:Capsule:2.1.0")

```
---

### Usage

```kotlin

package com.arkeverything.arkdev.test

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.liquidgl.LiquidGL

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // State holders
            val sliderValue = remember { mutableFloatStateOf(0.5f) }
            val toggleValue = remember { mutableStateOf(false) }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {

                // --- Slider controlling the tint ---
                LiquidGL.Sld(
                    value = { sliderValue.value },
                    onValueChange = {
                        sliderValue.value = it
                        LiquidGL.tint = Color(
                            red = it,
                            green = 0.3f,
                            blue = 1f - it
                        ) // dynamic tint
                    },
                    job = { println("Slider moved: $it") }
                )

                // --- Toggle ---
                LiquidGL.Tgl(
                    selected = { toggleValue.value },
                    onSelect = { toggleValue.value = it },
                    job = { println("Toggle is now $toggleValue") }
                )

                // --- Button using current tint ---
                LiquidGL.Btn(
                    job = { println("Button pressed!") }
                ) {
                    Text(text = "Liquid Button")
                }
            }
        }
    }
}


```

``P.S. Please make sure to give a .job handler. If object is test keep .job as null``
          
## END ##















