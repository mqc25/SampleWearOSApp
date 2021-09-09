# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /Users/Arjun/Library/Android/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

#-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#-optimizationpasses 16
#-allowaccessmodification


 # Keep - Library. Keep all public and protected classes, fields, and methods.
-keep public class * {
    public protected <fields>;
    public protected <methods>;
}

-keepclassmembers enum * { *; }
-dontshrink
-dontoptimize
-dontpreverify
#
#-assumenosideeffects class android.util.Log {
#    public static boolean isLoggable(java.lang.String, int);
#    public static int v(...);
#    public static int i(...);
#    public static int w(...);
#    public static int d(...);
#    public static int e(...);
#}
#

-keep class com.microsoft.azure.sdk.iot.**
-keep class com.google.**
-keep class com.amazonaws.** { *; }
-keepnames class com.amazonaws.** { *; }
-dontwarn com.amazonaws.**
-dontwarn com.fasterxml.**

-dontwarn org.apache.log4j.**
-dontwarn org.bouncycastle.**
-dontwarn org.slf4j.**

-dontwarn java.awt.**
-dontwarn javax.swing.**

-dontwarn weka.knowledgeflow.**
-dontwarn weka.gui.**
-dontwarn weka.experiment.**
-dontwarn java_cup.**
-dontwarn weka.core.pmml.**
-dontwarn weka.core.xml.**
-dontwarn org.bounce.**
-dontwarn weka.classifiers.bayes.net.**

-dontwarn weka.classifiers.functions.**
-dontwarn weka.core.json.**
-dontwarn weka.core.packageManagement.**
-dontwarn weka.filters.unsupervised.attribute.**

-dontwarn weka.classifiers.pmml.**
-dontwarn weka.attributeSelection.**
-dontwarn weka.clusterers.**
-dontwarn weka.classifiers.evaluation.**
-dontwarn weka.classifiers.misc.**
-dontwarn weka.core.Utils.**
-dontwarn weka.core.Utils.*
-dontwarn weka.core.Utils
-dontwarn weka.core.CheckGOE.**

-dontwarn weka.core.Trie.**
-dontwarn weka.core.Trie.*
-dontwarn weka.core.Trie
-dontwarn weka.estimators.*
-dontwarn weka.core.*

-dontwarn org.apache.log4j.*
-dontwarn com.microsoft.azure.storage.*
-dontwarn okhttp3.internal.cache.*
-dontwarn okhttp3.logging.*
-dontwarn org.bouncycastle.mail.*
-keepattributes *Annotation*
-keep @**annotation** class * {*;}
#-keep class weka.classifiers.meta.AttributeSelectedClassifier.**
#-keep class weka.classifiers.meta.AttributeSelectedClassifier.*
#-keep class weka.classifiers.meta.AttributeSelectedClassifier

#-keep class weka.classifiers.trees.RandomForest.**
#-keep class weka.classifiers.trees.RandomForest.*
#-keep class weka.classifiers.trees.RandomForest
-keep class weka.** { *; }
-ignorewarnings

-dontwarn org.apache.commons.compress.**
-dontwarn org.apache.tools.
-dontwarn java_cup.anttask.CUPTask.**

-dontwarn okhttp3.*
-dontwarn okio.*

#-keepnames class weka.** { *; }
#-dontwarn weka.**
