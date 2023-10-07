-keepnames class androidx.navigation.fragment.NavHostFragment
-keep class androidx.navigation.fragment.NavHostFragment
-keep class com.address_package.** { *; }
-keep class * extends androidx.fragment.app.Fragment{}

-dontwarn com.github.luben.**
-dontwarn com.mongodb.crypt.**
-dontwarn io.netty.**

-dontwarn okhttp3.**
-dontwarn com.google.errorprone.annotations.Immutable
## Rules for Gson
# Gson specific classes
-keep class com.google.gson.stream.** { *; }

# Retain generic type information for use by reflection by converters and adapters.
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

-dontusemixedcaseclassnames
##########
# Maintain all attributes:
# To avoid having to add each in several different places
# below.
#
# You may need to keep Exceptions if using dynamic proxies
# (e. g. Retrofit), Signature and *Annotation* if using reflection
# (e. g. Gson's ReflectiveTypeAdapterFactory).
##########
-keepattributes Exceptions,InnerClasses,Signature,Deprecated, SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

##########
# Android:
##########
##########
# Those are no longer required as this will force ProGuard to keep
# not only real app components and views, but also stuff like
# BaseFragmentActivityApi16, BaseFragmentActivityApi14,
# SupportActivity etc from being merged or removed.
# AAPT generates rules for all classes which were mentioned in XMLs.
##########
#-keep public class * extends android.app.Activity
#-keep public class * extends android.app.Application
#-keep public class * extends android.app.Service
#-keep public class * extends android.content.BroadcastReceiver
#-keep public class * extends android.content.ContentProvider
#-keep public class * extends android.app.backup.BackupAgentHelper
#-keep public class * extends android.preference.Preference
# Data Binding
-dontwarn android.databinding.**
-dontwarn javax.annotation.**
-dontwarn sun.misc.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase
-keep class com.google.gson.examples.android.model.** { <fields>; }
-keep class com.google.gson.examples.android.model.** { *; }

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform

-keep class android.databinding.** { *; }
#This is used if you are using onClick on the XML.. you shouldn't :-)
-keepclassmembers class * extends android.content.Context {
   public void *(android.view.View);
   public void *(android.view.MenuItem);
}

-keep class **$$ViewInjector {
    public static void inject(...);
    public static void reset(...);
}

-keep class **$$ViewBinder {
    public static void bind(...);
    public static void unbind(...);
}

-if   class **$$ViewBinder
-keep class <1>

-keep class **_ViewBinding {
    <init>(<1>, android.view.View);
}

-if   class **_ViewBinding
-keep class <1>

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

##########
#Enums - For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
##########

-keepclassmembers enum * { *; }

-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
-keepclassmembers class * implements android.os.Parcelable {
    static ** CREATOR;
}

-keepclassmembers class **.R$* {
    public static <fields>;
}


# Prevent proguard from stripping interface information from TypeAdapter, TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapter
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

#############
# Serializables
#############
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Ignore null checks at runtime
-assumenosideeffects class kotlin.jvm.internal.Intrinsics {
    static void checkParameterIsNotNull(java.lang.Object, java.lang.String);
}


# Workaround for Kotlin Safe Args issue
-if public class ** implements androidx.navigation.NavArgs
-keepclassmembers public class <1> {
    public static ** Companion;
    ** fromBundle(android.os.Bundle);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


-keepclasseswithmembers class * {
    @androidx.annotation.Keep <methods>;
}
-keep @androidx.annotation.Keep class * {*;}

-keepclassmembers public class * extends android.view.View {
    void set*(***);
    *** get*();
}

-keepclasseswithmembers class * {
    @androidx.annotation.Keep <init>(...);
}



#moshi
# JSR 305 annotations are for embedding nullability information.
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}
-dontwarn org.jetbrains.annotations.**
-keep class kotlin.Metadata { *; }
-keepclassmembers class com.summer.passwordmanager.beans.** {
  <init>(...);
  <fields>;
}
-keepclassmembers class com.summer.passwordmanager.database.entities.** {
  <init>(...);
  <fields>;
}

-dontnote org.apache.http.**
-dontnote android.net.http.**
-dontnote java.lang.invoke.**

-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
-optimizationpasses 5
-allowaccessmodification

-keepclassmembers class **$WhenMappings {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
-keep class kotlin.Metadata { *; }
-keepclassmembers class kotlin.Metadata {
    public <methods>;
}


-keepclassmembers class * extends java.lang.Enum {
    <fields>;
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepclassmembernames class kotlinx.** {
    volatile <fields>;
}
-dontwarn com.onesignal.**

-keep class com.onesignal.ActivityLifecycleListenerCompat** {*;}

-keep class com.onesignal.JobIntentService$* {*;}

-keep class com.google.firebase.crashlytics.** { *; }
-dontwarn com.google.firebase.crashlytics.**


-keep class com.google.android.gms.* {  *; }
-dontwarn com.google.android.gms.**
-dontnote **ILicensingService
-dontnote com.google.android.gms.**

-dontwarn com.google.android.gms.ads.**
#############
# Android Support Lib
#############
# Firebase
#############
-dontnote com.google.firebase.**
-dontwarn com.google.firebase.crash.**


-dontnote okio.**

#############
# HttpClient Legacy (Ignore) - org.apache.http legacy
#############
-dontnote android.net.http.*
-dontnote org.apache.commons.codec.**
-dontnote org.apache.http.**
##########
# Glide
##########
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
    **[] $VALUES;
    public *;
}
-dontnote com.bumptech.glide.**
##########
# RxJava 2
##########

-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}

# Preserve the special static methods that are required in all enumeration classes.
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class * {
    public protected *;
}

-keep class net.sqlcipher.** { *; }
-keep class net.sqlcipher.database.* { *; }

-dontwarn com.google.errorprone.annotations.**

-keep public class * extends java.lang.Exception