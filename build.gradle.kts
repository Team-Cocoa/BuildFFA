plugins {
    java
    id("com.diffplug.spotless") version "8.10.3"
    id("com.gradleup.shadow") version "9.2.2"
}

group = "kr.teamcocoa"
version = "1.0-SNAPSHOT"

java {
    // JDK 17 is required: TeamCocoa-Core, Language and KitManager are Java 17 class files,
    // which a JDK 8 compiler can't read. The plugin itself is still compiled to Java 8 bytecode.
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

spotless {
    java {
        importOrder() // Enforces a uniform import structure
        removeUnusedImports()
        googleJavaFormat() // Automatically applies Google Style Guide formatting
    }
}

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://jitpack.io/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://oss.sonatype.org/content/groups/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    compileOnly(files("lib/Language.jar"))
    compileOnly(files("lib/TeamCocoa-Core.jar"))
    compileOnly(files("lib/KitAPI.jar"))

    // Provided at runtime by TeamCocoa-Core (retrofit 2.9.0 / okhttp 3.14.9 / gson 2.10.1)
    compileOnly("com.squareup.retrofit2:retrofit:2.9.0")
    compileOnly("com.squareup.retrofit2:converter-gson:2.9.0")

    // Shaded and relocated: Spigot 1.8 ships Guava 17, which is too old for Guice
    implementation("com.google.inject:guice:5.1.0")
    implementation("com.google.inject.extensions:guice-assistedinject:5.1.0")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
    options.release.set(8)
}

tasks.jar {
    archiveClassifier.set("plain")
}

tasks.shadowJar {
    archiveFileName.set("BuildFFA.jar")

    dependencies {
        exclude(dependency("com.google.code.findbugs:jsr305"))
        exclude(dependency("com.google.errorprone:error_prone_annotations"))
        exclude(dependency("com.google.j2objc:j2objc-annotations"))
        exclude(dependency("org.checkerframework:checker-qual"))
    }

    val libs = "kr.teamcocoa.buildffa.libs"
    relocate("com.google.inject", "$libs.guice")
    relocate("com.google.common", "$libs.guava")
    relocate("com.google.thirdparty", "$libs.guava.thirdparty")
    relocate("javax.inject", "$libs.javax.inject")
    relocate("org.aopalliance", "$libs.aopalliance")
}

tasks.build {
    dependsOn(tasks.shadowJar)
}
