# PPF - Pin Pattern Fingerprint



Android Pin Pattern Fingerprint Library

## Prerequisites

#### Old
If you're using old gradle versions then follow this.
Add this in your root `build.gradle` :

```gradle
allprojects {
	repositories {
		...
		maven { url "https://jitpack.io" }
	}
}
```

#### New
If you're using new gradle versions then follow this.
Add this in your `settings.gradle` file:

```gradle
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
       ...
        maven { url 'https://jitpack.io' }

    }
}
```

## Theme
For using <b>CuteDialog</b> in your project, you must use <b>Material Theme</B> in your project. You can use <b>CuteDialog</b> in both <b>Material</b>  <b>Light</b> and <b>Dark</b> theme.

For example:
    
 ```xml

    <style name="AppTheme" parent="Theme.MaterialComponents.Light.DarkActionBar">
	    
        <!-- Customize your theme here. -->
	    
    </style>
```
Or

```xml

    <style name="AppTheme" parent="Theme.MaterialComponents.DayNight.DarkActionBar">
	    
        <!-- Customize your theme here. -->
	    
    </style>

```


## Dependencies
Add this to your app level `build.gradle`:

(Always use the latest version. Current Latest version is <a href="https://jitpack.io/#ahmmedrejowan/PPF"><img src="https://jitpack.io/v/ahmmedrejowan/PPF.svg" alt="JitPack"></a> )

```gradle
dependencies {
	...
	     
	        implementation 'com.github.ahmmedrejowan:PPF:0.1'


}
```
