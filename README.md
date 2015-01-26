# SharedPreferenceInspector
Provides a simple way to see shared preferences and edit them for test. No need to pull shared preferences. Just some simple clicks to see the values stored. Also with test mode to change the values and check the behavior of the app.

# Usage

## Gradle
Add this library as dependency to your project's ```build.gradle```

```groovy
debugCompile {gradle credentials coming soon}
```

## Code
Declare this activity in menifest

```xml
<activity android:name="com.ceelites.sharedpreferenceinspector.SharedPrefsBrowser"/>
```

To launch this activity you have two ways.

1. From the menu.

To do it, Initiate `SharedPreferenceUtils` with `SharedPreferenceUtils.initWith()` call.

In `onCreateOptionsMenu()` call 
```java
if (BuildConfig.DEBUG) {
			prefsUtils.inflateDebugMenu(getMenuInflater(), menu);
		}
```

In `onOptionsItemSelected()` method check value of `isDebugHandled` method 
```java
prefsUtils.isDebugHandled(this, item)) 
```
If this method returns `true` activity will open itself. Otherwise you have to handle menu generation code yourselves.

2. Launch the activity directly 

Just Call.
```java
if (BuildConfig.DEBUG) {
			SharedPreferenceUtils.startAcvitivy(this);
}
```

# Test Mode
SharedPreferenceInspector has a unique feature called test mode, where you can change your already stored shared preference value to any supported value and check how your app behaves with this change. While you are in the **test mode** your original value is kept safe and when you exit it, the original value will be restored. There are two ways to enter the test mode.

1. From menu. Where you can enter the tests mode
2. If you are not in test mode and try to click on the row, it will ask you to enter test mode to change the value.

The test mode values are stored in shared preference keys **untill you exit test mode**. 

# Support
This library gives support from api level 10. And It has been tested in a GingerBread device.

# Known Issues
1. Not implemented handling String Set. 
2. Uploading maven. 