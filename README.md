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
- Fully composable DSL for easy, clean usage.

---

### Credits
-------

* Original **Liquid Glass**: [Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass?utm_source=chatgpt.com)
* Original **Capsule** library: [Kyant0/Capsule](https://github.com/Kyant0/Capsule?utm_source=chatgpt.com)

> This library is a **wrapper** and does not copy the original source code. All credits go to the original author Kyant0.

### License
-------

This project is MIT licensed. For the original Liquid Glass library license, see [here](https://github.com/Kyant0/AndroidLiquidGlass/blob/main/LICENSE.md).

---

### Installation

Make sure to put this in your 'build.gradle.kts (:app)' :

                allprojects {
            repositories {
                google()
                mavenCentral()
                maven { url 'https://jitpack.io' }
            }
        }

Add the JitPack dependency (replace `<version>` with the release version):


        implementation("com.github.Aarav90-cpu:ARKeasyTools:<version>")
        implementation("com.github.Kyant0:AndroidLiquidGlass:<version>")
        implementation("com.github.Kyant0:Capsule:<version>")


---

### Usage

    class MainActivity : ComponentActivity() {

    class LiquidCatalogTestActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContent {
                // Track UI states
                var toggleState by remember { mutableStateOf(false) }
                var sliderValue by remember { mutableStateOf(0.5f) }
                var selectedTab by remember { mutableStateOf(0) }
    
                LiquidGL.catalog {
                    // Button example
                    button(onClick = { println("Button pressed!") }) {
                        Text("Press Me", modifier = Modifier.padding(8.dp))
                    }

                    // Slider example
                    slider(
                        value = sliderValue,
                        onValueChange = { sliderValue = it },
                        valueRange = 0f..1f
                  )

                    // Toggle example
                    toggle(selected = toggleState, onSelect = { toggleState = it })
    
                    // Bottom Tabs example
                    bottomTabs(
                        selectedIndex = selectedTab,
                        onTabSelected = { selectedTab = it },
                        tabsCount = 3
                    ) {
                        Row {
                            Text("Tab 1", modifier = Modifier.padding(4.dp))
                            Text("Tab 2", modifier = Modifier.padding(4.dp))
                            Text("Tab 3", modifier = Modifier.padding(4.dp))
                        }
                    }
                }
            }
        }
    }
          
## END ##





