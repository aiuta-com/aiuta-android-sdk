import com.aiuta.fashionsdk.compileSdk
import com.aiuta.fashionsdk.targetSdk
import com.android.build.api.dsl.ManagedVirtualDevice

plugins {
    // AGP 9+ provides built-in Kotlin support, so no Kotlin plugin is applied.
    id("com.android.test")
    id("androidx.baselineprofile.producer")
}

android {
    namespace = "com.aiuta.fashionsdk.benchmark"
    compileSdk = project.compileSdk

    defaultConfig {
        // Baseline Profile generation/measurement requires API 24+.
        minSdk = 24
        targetSdk = project.targetSdk
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    // The application module whose journeys generate the profile. The
    // androidx.baselineprofile.apptarget plugin on it creates the
    // nonMinifiedRelease / benchmarkRelease variants this module runs against.
    targetProjectPath = ":samples:tryon-kmp-android"

    // Run the test APK inside the target app's process so UiAutomator can
    // drive Compose UI without a separate instrumentation app.
    experimentalProperties["android.experimental.self-instrumenting"] = true

    testOptions {
        managedDevices {
            allDevices {
                // Gradle-managed virtual device. AOSP image (no Google Play)
                // is required for benchmarking — it grants the shell access
                // Macrobenchmark needs.
                create<ManagedVirtualDevice>("pixel6Api34") {
                    device = "Pixel 6"
                    apiLevel = 34
                    systemImageSource = "aosp"
                }
            }
        }
    }
}

// Producer plugin: generate and measure on the managed virtual device by
// default so no physical device has to be attached.
baselineProfile {
    managedDevices += "pixel6Api34"
    useConnectedDevices = false
}

dependencies {
    implementation(projects.internal.internalBenchmarkTags)
    implementation(libs.androidx.benchmark.macro)
    implementation(libs.androidx.test.ext.junit)
    implementation(libs.androidx.test.uiautomator)
    implementation(libs.androidx.test.espresso.core)
}

// Aggregates the Macrobenchmark *-benchmarkData.json results from the last run
// into a Coil-style comparison table at internal/benchmark/benchmark_output.md.
//
// Usage (two steps):
//   ./gradlew :internal:benchmark:pixel6Api34BenchmarkReleaseAndroidTest
//   ./gradlew :internal:benchmark:generateBenchmarkReport
tasks.register("generateBenchmarkReport") {
    description = "Parses *-benchmarkData.json into benchmark_output.md."
    group = "verification"

    val outputsDir = layout.buildDirectory.dir("outputs").get().asFile
    val reportFile = layout.projectDirectory.file("benchmark_output.md").asFile

    doLast {
        val jsonFiles = outputsDir.walkTopDown()
            .filter { it.isFile && it.name.endsWith("-benchmarkData.json") }
            .toList()
        if (jsonFiles.isEmpty()) {
            logger.warn(
                "No *-benchmarkData.json found under $outputsDir. Run a benchmark " +
                    "first, e.g. ./gradlew :internal:benchmark:pixel6Api34BenchmarkReleaseAndroidTest",
            )
            return@doLast
        }

        @Suppress("UNCHECKED_CAST")
        fun map(v: Any?): Map<String, Any?> = (v as? Map<String, Any?>).orEmpty()
        fun num(v: Any?): Double? = (v as? Number)?.toDouble()
        fun fmt(d: Double?): String = d?.let { String.format("%.1f", it) } ?: "—"

        // Headline stat for a metric block: P95 for sampled (frame) metrics,
        // median for everything else.
        fun headline(block: Map<String, Any?>): Pair<String, Double?> = when {
            block.containsKey("P95") -> "P95" to num(block["P95"])
            block.containsKey("median") -> "median" to num(block["median"])
            else -> "value" to block.values.firstNotNullOfOrNull { num(it) }
        }

        fun modeOf(name: String): String = when {
            name.endsWith("BaselineProfile") -> "Baseline Profile"
            name.endsWith("NoCompilation") -> "None"
            name.endsWith("FullCompilation") -> "Full"
            else -> "Other"
        }
        fun baseOf(name: String): String = name
            .removeSuffix("BaselineProfile")
            .removeSuffix("NoCompilation")
            .removeSuffix("FullCompilation")
            .ifEmpty { name }

        fun delta(none: Double?, bp: Double?): String {
            if (none == null || bp == null || none == 0.0) return "—"
            val pct = (bp - none) / none * 100.0
            val sign = if (pct > 0) "+" else "−"
            return "$sign${String.format("%.1f", if (pct < 0) -pct else pct)}%"
        }

        val all = mutableListOf<Map<String, Any?>>()
        var context: Map<String, Any?> = emptyMap()
        jsonFiles.forEach { file ->
            val root = map(groovy.json.JsonSlurper().parse(file))
            map(root["context"]).takeIf { it.isNotEmpty() }?.let { context = it }
            (root["benchmarks"] as? List<*>)?.forEach { all += map(it) }
        }

        // base -> mode -> (metric -> stats block)
        val grouped = linkedMapOf<String, MutableMap<String, Map<String, Map<String, Any?>>>>()
        all.forEach { b ->
            val name = b["name"] as? String ?: return@forEach
            val blocks = linkedMapOf<String, Map<String, Any?>>()
            map(b["metrics"]).forEach { (k, v) -> blocks[k] = map(v) }
            map(b["sampledMetrics"]).forEach { (k, v) -> blocks[k] = map(v) }
            grouped.getOrPut(baseOf(name)) { linkedMapOf() }[modeOf(name)] = blocks
        }

        val sb = StringBuilder()
        sb.appendLine("# Baseline Profile benchmark results")
        sb.appendLine()
        val build = map(context["build"])
        val sdk = map(build["version"])["sdk"] ?: build["version"]
        val device = listOfNotNull(
            build["model"]?.toString(),
            sdk?.let { "API $it" },
        ).joinToString(", ")
        if (device.isNotBlank()) sb.appendLine("Device: $device  ")
        sb.appendLine(
            "CPU locked: ${context["cpuLocked"] ?: "—"} · " +
                "sustained performance: ${context["sustainedPerformanceModeEnabled"] ?: "—"} · " +
                "source: ${jsonFiles.size} file(s)",
        )
        sb.appendLine()

        grouped.forEach { (base, modes) ->
            sb.appendLine("## $base")
            sb.appendLine()
            sb.appendLine("| Metric | None | Baseline Profile | Δ (BP vs None) |")
            sb.appendLine("|---|---:|---:|---:|")
            val metrics = (modes["None"]?.keys.orEmpty() + modes["Baseline Profile"]?.keys.orEmpty())
                .toSortedSet()
            metrics.forEach { metric ->
                val noneBlock = modes["None"]?.get(metric)
                val bpBlock = modes["Baseline Profile"]?.get(metric)
                val stat = (noneBlock ?: bpBlock)?.let { headline(it).first } ?: "median"
                val noneVal = noneBlock?.let { headline(it).second }
                val bpVal = bpBlock?.let { headline(it).second }
                sb.appendLine("| $metric ($stat) | ${fmt(noneVal)} | ${fmt(bpVal)} | ${delta(noneVal, bpVal)} |")
            }
            sb.appendLine()
        }

        sb.appendLine("## Raw metrics")
        sb.appendLine()
        all.sortedBy { it["name"] as? String ?: "" }.forEach { b ->
            sb.appendLine("### ${b["name"]}")
            sb.appendLine()
            sb.appendLine("| Metric | min | median | max | P50 | P90 | P95 | P99 |")
            sb.appendLine("|---|---:|---:|---:|---:|---:|---:|---:|")
            val blocks = linkedMapOf<String, Map<String, Any?>>()
            map(b["metrics"]).forEach { (k, v) -> blocks[k] = map(v) }
            map(b["sampledMetrics"]).forEach { (k, v) -> blocks[k] = map(v) }
            blocks.forEach { (metric, s) ->
                sb.appendLine(
                    "| $metric | ${fmt(num(s["minimum"]))} | ${fmt(num(s["median"]))} | " +
                        "${fmt(num(s["maximum"]))} | ${fmt(num(s["P50"]))} | ${fmt(num(s["P90"]))} | " +
                        "${fmt(num(s["P95"]))} | ${fmt(num(s["P99"]))} |",
                )
            }
            sb.appendLine()
        }

        reportFile.writeText(sb.toString())
        logger.lifecycle("Wrote ${reportFile.absolutePath} from ${jsonFiles.size} benchmarkData.json file(s).")
    }
}
