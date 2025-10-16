# ARKeasyTools : Tools for easy Kotlin Coding

---

# 1> Easy liquidGL

A Kotlin library for Jetpack Compose that provides a simple DSL wrapper around the amazing **[Android Liquid Glass](https://github.com/Kyant0/AndroidLiquidGlass)** and **Capsule** libraries.

This project **does not contain the original Kyant0 source** but wraps it for easier integration in launcher and app projects.

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

Create a new project and move the two folders under the com directory which were downloaded form this repository under the com directory of your project:

```

com
|--arkeverything
|--kyant

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
        
        implementation("com.github.Kyant0:AndroidLiquidGlass:<version>")
        implementation("com.github.Kyant0:Capsule:<version>")

```
---

### Usage

```kotlin

import com.arkeverything.arkdev.arkeasytools.liquidgl.LiquidGL
import com.kyant.backdrop.Backdrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color

@Composable
fun MyLiquidScreen(backdrop: Backdrop) {
    LiquidGL.Button(
        backdrop = backdrop,
        onClick = { println("Button pressed!") }
    ) {
        // RowScope content here
    }

    LiquidGL.Toggle(
        backdrop = backdrop,
        selected = { true },
        onSelect = { selected -> println("Toggle is $selected") }
    )

    LiquidGL.Slider(
        backdrop = backdrop,
        value = { 0.5f },
        onValueChange = { value -> println("Slider: $value") },
        valueRange = 0f..1f
    )

    LiquidGL.BottomTabs(
        backdrop = backdrop,
        tabsCount = 3,
        content = { index ->
            println("Tab selected: $index")
        }
    )

    LiquidGL.Menu(
        backdrop = backdrop,
        options = listOf(
            LiquidMenuOption("Option 1") { println("Option 1 clicked") },
            LiquidMenuOption("Option 2") { println("Option 2 clicked") }
        )
    )
}

```

``P.S. Please make sure to give a .job handler. If object is test keep .job as null``
          
## END ##









