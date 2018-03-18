# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Kotlin
-dontwarn kotlin.**
-keepclassmembers class **$WhenMappings {
    <fields>;
}
# If you want to get rid of null checks at runtime you may use the following rule:
#-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
#    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
#}

# SLF4J
-dontwarn org.slf4j.**

# Arrow
-dontwarn arrow.**
-keep class arrow.core.** { *; }
-keep class arrow.typeclasses.** { *; }

# Retrofit2
-dontwarn retrofit2.**
-keepattributes Signature
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn okhttp3.**

# Gson
-keep class com.google.gson.** { *; }
-keep class sun.misc.Unsafe
-keepattributes Signature

# Keep your model classes
-keep class com.omainegra.marvela.** { *; }