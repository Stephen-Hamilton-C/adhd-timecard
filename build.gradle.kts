/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Kotlin application project to get you started.
 * For more details take a look at the 'Building Java & JVM projects' chapter in the Gradle
 * User Manual available at https://docs.gradle.org/7.4.2/userguide/building_java_projects.html
 */

plugins {
	// Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
	id("org.jetbrains.kotlin.jvm") version "1.7.0"

	// Kotlinx Serialization
	kotlin("plugin.serialization") version "1.7.0"

	// Apply the application plugin to add support for building a CLI application in Java.
	application

	//#region Windows
	// Build EXE
	// https://github.com/TheBoegl/gradle-launch4j
	id("edu.sc.seis.launch4j") version "2.5.3"

	// NSIS Windows Installer creator
	// https://github.com/langmo/gradle-nsis
	id("com.github.langmo.gradlensis") version "0.1.0"
	//#endregion
}

repositories {
	// Use Maven Central for resolving dependencies.
	mavenCentral()
	maven("https://jitpack.io")
}

dependencies {
	// Align versions of all Kotlin components
	implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

	// Use the Kotlin JDK 8 standard library.
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	// Kotlinx JSON
	// https://github.com/Kotlin/kotlinx.serialization/
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.3")

	// Kaml - YAML Support for kotlinx.serialization
	// https://github.com/charleskorn/kaml
	implementation("com.charleskorn.kaml:kaml:0.45.0")

	// KAppDirs
	// Better than Harawata's AppDirs as KAppDirs doesn't import JNA
	// https://github.com/erayerdin/kappdirs
	implementation("com.github.erayerdin:kappdirs:0.3.1-alpha")

	// Use the Kotlin test library.
	testImplementation("org.jetbrains.kotlin:kotlin-test")

	// Use the Kotlin JUnit integration.
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

application {
	// Define the main class for the application.
	mainClass.set("app.shamilton.timecardkt.AppKt")
}

java {
	toolchain.languageVersion.set(JavaLanguageVersion.of(8))
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
	kotlinOptions {
		jvmTarget = "1.8"
	}
}

// Debian packaging tasks
apply(from = "./deploy/debian/debian.gradle")

//#region Windows
// Create Windows EXE build
tasks.withType<edu.sc.seis.launch4j.tasks.DefaultLaunch4jTask> {
	headerType = "console"
	outfile = "timecard-kt.exe"
	mainClassName = "app.shamilton.timecardkt.AppKt"
	productName = "timecard-kt"
}

// Ensure exe exists
tasks {
	createInstaller {
		dependsOn("createExe")
	}
}

// Create Windows installer
nsis {
	configuration.set(file("${rootProject.projectDir}/deploy/windows/nsis.nsi"))
	runIn.set(file(rootProject.projectDir))
}
//#endregion