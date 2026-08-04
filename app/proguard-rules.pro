########################################
# Generic ProGuard Configuration for Android
# Works with any package name
########################################

# ----------------------------------
# Gson Specific Rules
# ----------------------------------
-keep class com.google.gson.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class * extends com.google.gson.reflect.TypeToken { *; }
-dontwarn com.google.gson.**
-keepattributes Signature, InnerClasses, EnclosingMethod, *Annotation*

# Keep model classes (adjust pattern based on your structure)
-keep class **.model.** { *; }
-keep class **.models.** { *; }

# Keep serialized fields
-keepclassmembers class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

# ----------------------------------
# Room Database Rules
# ----------------------------------
-keep class * extends androidx.room.RoomDatabase
-keep class * extends androidx.room.Entity
-keep class * extends androidx.room.TypeConverter

# Keep database implementations
-keep class * extends androidx.room.RoomDatabase {
    public *;
    protected *;
}

# Keep entities and their fields
-keep @androidx.room.Entity class * {
    @androidx.room.* *;
}

# Keep DAOs
-keep interface * extends androidx.room.Dao {
    *;
}

# Keep TypeConverters
-keepclassmembers class * {
    @androidx.room.TypeConverter *;
}

# Keep database version info
-keepclassmembers class * extends androidx.room.RoomDatabase {
    public static *;
}

# Keep database-related packages (adjust patterns as needed)
-keep class **.database.** { *; }
-keep class **.dao.** { *; }
-keep class **.entities.** { *; }
-keep class **.converters.** { *; }

# Keep Room's generated implementation classes
-keep class **.*_Impl { *; }

# ----------------------------------
# Retrofit Rules
# ----------------------------------
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**
-keepattributes Signature, Exceptions, InnerClasses, EnclosingMethod

# Keep Retrofit service interfaces
-keep interface * extends retrofit2.http.* { *; }

# Keep all methods in Retrofit services
-keepclassmembers interface * {
    @retrofit2.http.* <methods>;
}

# Keep web service related packages
-keep class **.web.** { *; }
-keep class **.api.** { *; }
-keep class **.network.** { *; }
-keep class **.service.** { *; }

# ----------------------------------
# AndroidX and Support Library Rules
# ----------------------------------
-keep class androidx.** { *; }
-keep class android.support.** { *; }
-dontwarn androidx.**
-dontwarn android.support.**

# ----------------------------------
# Additional Library Rules
# ----------------------------------
-dontwarn javax.annotation.**
-dontwarn okhttp3.**
-dontwarn org.bouncycastle.**
-dontwarn org.conscrypt.**

-keep class net.sqlcipher.** { *; }
-keep class com.scottyab.rootbeer.** { *; }

# Keep enum methods
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

# SSL/TLS library warnings
-dontwarn org.bouncycastle.jsse.**
-dontwarn org.conscrypt.**
-dontwarn org.openjsse.**

# ----------------------------------
# Generic Application Rules
# ----------------------------------
# Keep all activities, services, broadcast receivers, and content providers
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.Application

-dontwarn org.joda.convert.FromString
-dontwarn org.joda.convert.ToString

# Keep parcelable implementations
-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

# ----------------------------------
# SSL Pinning & Security Rules
# (Dynamic - No package name needed)
# ----------------------------------

# Keep ALL X509TrustManager implementations (covers PinnedTrustManager
# in any project/package) — Android calls 3-arg checkServerTrusted
# via reflection in release builds
-keep class * implements javax.net.ssl.X509TrustManager {
    public java.util.List checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String, java.lang.String);
    public void checkServerTrusted(java.security.cert.X509Certificate[], java.lang.String);
    public void checkClientTrusted(java.security.cert.X509Certificate[], java.lang.String);
    public java.security.cert.X509Certificate[] getAcceptedIssuers();
}

# Keep ALL X509ExtendedTrustManager implementations (Android 24+
# uses this subclass internally)
-keep class * extends javax.net.ssl.X509ExtendedTrustManager {
    *;
}

# Keep javax.net.ssl classes — SSLContext, SSLSession, etc.
-keep class javax.net.ssl.** { *; }

# Keep java.security.cert classes — X509Certificate, CertificateFactory
-keep class java.security.cert.** { *; }

# Keep OkHttp SSL classes
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-keep class okhttp3.CertificatePinner { *; }
-keep class okhttp3.CertificatePinner$Builder { *; }

# Keep security-related package patterns
# (matches your naming conventions like webcall, security, crypto)
-keep class **.security.** { *; }
-keep class **.webcall.** { *; }
-keep class **.crypto.** { *; }
-keep class **.ssl.** { *; }