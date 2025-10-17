# ARKeasyTools : Tools for easy Kotlin Coding

---

# 1> Easy liquidGL

A Kotlin library for Jetpack Compose that provides a simple DSL wrapper around the amazing **[Android Liquid Glass](https://github.com/Kyant0/AndroidLiquidGlass)** and **Capsule** libraries.

This project **does not contain the original Kyant0 source** but wraps it for easier integration in launcher and app projects.

``P.S. Only update at releases tagged Main do not upadate at Alpha or Small Releases as they come  more often. This point is just for saving your sanity. Also if any small or alpha release breaks it will be removed.
Why? Cause Before I add a new file or feature I will create a new release so that if someone wants to tweak that themselves they can do that. You can use Alpha-Main cause that just marks few months before Stable release. THis Repository wil be paused until 20 to #0 days starting from **17/10/25**``

**This library makes coding esy for Kyant'0s AndroidLiquidGlass. But imports should be managed properly... This library was rebuilt for `Alpah-Main` to add new features**

Test apps are also given at the root of file.

---

### Features

- `liquidButton` – Liquid-style button with press animations.
- `liquidSlider` – Smooth liquid glass slider.
- `liquidToggle` – Toggle switch with liquid effect.
- `liquidBottomTabs` – Bottom tabs with liquid glass effect. (In development)
- `liquidMenu` - Menu with liquid glass effect. (In development)
- Fully composable DSL for easy, clean usage.

---

### Credits
-------

* Original **Liquid Glass**: **[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass?utm_source=chatgpt.com)**
* Original **Capsule** library: **[Kyant0/Capsule](https://github.com/Kyant0/Capsule?utm_source=chatgpt.com)**

> This library is a **wrapper** and does not copy the original source code. All credits go to the original author Kyant0.

### License
-------

This project is Apache licensed. 
For the original Liquid Glass library license, see **[here](https://github.com/Kyant0/AndroidLiquidGlass?tab=Apache-2.0-1-ov-file)**.
For the original Capsule library license, see **[here](https://github.com/Kyant0/Capsule?tab=Apache-2.0-1-ov-file)**.

---

### Installation

``P.S. If you really need a low based project use Alpha before stable as they are the best for rigit and hardcoding developers who love to get stuck in tiny problems. If you are a begeinner wait for stable to role out``

Create a new project and move the two folders under the com directory which were downloaded form this repository which are ``arkeverything`` and ``kyant`` under the com directory of your project. Also exchange the res folder in your project with the res folder provided.

Once you have moved the two projects in You will see that under com two new directories have appeared.

```

com
|--arkeverything
|--kyant

```

Make the following changes to your AndroidMnifest.xml

``This will prevent errors that come when you replace your res folder if you do nto put the following in your `AndroidManifest.xml` then be ready to face errors``

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

``You need to add this so that you can fetch from github (Not all repositories... there are some rules)``

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

``I have told you to add the kyant folder under com so no need to do this but still. I insist``

```gradle
        
        implementation("com.github.Kyant0:AndroidLiquidGlass:1.0.0-beta02")
        implementation("com.github.Kyant0:Capsule:2.1.0")

```
---

### Usage

Always use Patches provided with the ARKeasyTools.
Imports should be made for everything do not trust LiquidGL.kt to import everything you need.
Some Backdrops are made by AI and can fail so please send fixes if any Bugs are spotted.
There is a problem with `toBackdrop` in some files...

---
Here is some example code.

```kotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkeverything.arkdev.arkeasytools.LiquidGL.BackdropLayered
import com.arkeverything.arkdev.arkeasytools.LiquidScope
import com.arkeverything.arkdev.arkeasytools.patch.rememberDummyBackdrop
import com.arkeverything.arkdev.arkeasytools.components.*

class LiquidTestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            // remember a dummy backdrop for components
            val dummyBackdrop = rememberDummyBackdrop()

            // Full backdrop with layered effect
            BackdropLayered(
                modifier = Modifier.fillMaxSize(),
                blurRadius = LiquidScope.liquidModifier.blurRadius,
                tint = Color(0x88FFFFFF)
            ) {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    verticalArrangement = Arrangement.SpaceEvenly,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // --- Liquid Button ---
                    // --- Liquid Button ---
                    LiquidButton(
                        onClick = { println("Button Pressed!") },
                        backdrop = dummyBackdrop
                    ) {
                        Text("Press Me")
                    }

// --- Liquid Slider ---
                    var sliderValue by remember { mutableFloatStateOf(0.5f) }
                    LiquidSlider(
                        value = { sliderValue },
                        onValueChange = { sliderValue = it },
                        valueRange = 0f..1f,
                        backdrop = dummyBackdrop
                    )
                    // optional slider content


// --- Liquid Toggle ---
                    var toggleState by remember { mutableStateOf(false) }
                    LiquidToggle(
                        checked = toggleState,
                        onCheckedChange = { toggleState = it },
                        backdrop = dummyBackdrop
                    )
                    // optional toggle content




// --- Liquid Bottom Tabs ---
                    var selectedTab by remember { mutableIntStateOf(0) }
                    LiquidBottomTabs(
                        selectedTabIndex = { selectedTab },
                        onTabSelected = { selectedTab = it },
                        tabsCount = 3,
                        backdrop = dummyBackdrop
                    )
                    // optional tab content
                    {
                        Text("Tab 1")
                        Text("Tab 2")
                        Text("Tab 3")
                    }
                }
            }
        }
    }
}


```

``P.S. Please make sure to give a .job handler. If object is test keep .job as null``
          
## END ##




















