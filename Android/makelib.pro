-injars       AFEngine.jar
-outjars      AFEngineLib.jar
-libraryjars  E:\Tech\iptv\Tool\PackJarTool\tools\lib\android\android.jar
-printmapping AFEngine.map

-dontpreverify
-dontoptimize
-dontusemixedcaseclassnames
-repackageclasses ''
-optimizationpasses 7
-dontskipnonpubliclibraryclasses
-dontskipnonpubliclibraryclassmembers

-keepparameternames
-renamesourcefileattribute SourceFile
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,
                SourceFile,LineNumberTable,*Annotation*,EnclosingMethod

-keep public class * {
    public protected *;
}

-keepclassmembernames class * {
    java.lang.Class class$(java.lang.String);
    java.lang.Class class$(java.lang.String, boolean);
}

-keepclasseswithmembernames class * {
    native <methods>;
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

