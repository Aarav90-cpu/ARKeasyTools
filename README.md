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

        private val layerBackdrop = LayerBackdrop().apply {
            // Optional: add your custom onDraw here
            onDraw { drawScope ->
                drawScope.drawRect(
                    color = Color(0x550000FF)
                )
            }
        }

          override fun onCreate(savedInstanceState: Bundle?) {
              super.onCreate(savedInstanceState)
              setContent {
              AppContent(layerBackdrop)
            }
        }
    }

@Composable
fun AppContent(backdrop: LayerBackdrop) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .layerBackdrop(backdrop), // plug in your LayerBackdrop
        color = Color.White
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp)
        ) {
            Text(text = "Yo bro, ARKEasyTools is live!", style = MaterialTheme.typography.headlineSmall)
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(Color.LightGray)
            )
        }
    }
}

## END ##




