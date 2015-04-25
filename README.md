# SharedPreferenceInspector
Provides a simple way to see shared preferences and edit them for test. No need to pull shared preferences. Just some simple clicks to see the values stored. Also with test mode to change the values and check the behavior of the app.

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-SharedPreferenceInspector-brightgreen.svg?style=flat)](https://android-arsenal.com/details/1/1447)


[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.prashamtrivedi/sharedpreferenceinspector/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.github.prashamtrivedi/sharedpreferenceinspector)

# Usage

## Gradle
Add this library as dependency to your project's ```build.gradle```

```groovy
debugCompile 'com.github.prashamtrivedi:sharedpreferenceinspector:{latestVersion}'
```
Where latest version can be found from above (Maven Central Badge)

## Code
### If you are using version 1.x.
Declare this activity in your debug menifest.

```xml
<activity android:name="com.ceelites.sharedpreferenceinspector.SharedPrefsBrowser"/>
```
Don't forget to **Provide appropriate theme** *(Any Child of `Theme.AppCompat` is recommended)* to that activity. It's needed to inflate menus.

### If you are using version 2.0 and above

Just import the library. Library will take care of adding activity.

To launch the SharedPrefsBrowser activity you have two ways.

1. From the menu.
2. Launch the activity directly 
 
### To Launch the activity from menu
Initiate `SharedPreferenceUtils` with `SharedPreferenceUtils.initWith()` call.

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

### To Launch the activity directly
Just Call.
```java
if (BuildConfig.DEBUG) {
	SharedPreferenceUtils.startActivity(this);
}
```

# Test Mode
SharedPreferenceInspector has a unique feature called test mode, where you can change your already stored shared preference value to any supported value and check how your app behaves with this change. While you are in the **test mode** your original value is kept safe and when you exit it, the original value will be restored. There are two ways to enter the test mode.

1. From menu. Where you can enter the tests mode
2. If you are not in test mode and try to click on the row, it will ask you to enter test mode to change the value.

The test mode values are stored in shared preference keys **until you exit test mode**.

# Support
This library gives support from api level 10. And It has been tested in a GingerBread device.

# Proguard Configuration
If you are using proguard, you should add this line in your proguard file. In case of version 2.0 this line should be added automatically.
`-keep class android.support.v7.widget.SearchView { *; }`

# ChangeLog
## 2.1
- Code Cleanup (Credit [@ggajews](https://github.com/ggajews) )
- Added Support for new AppComat library 22.1

## 2.0 
- Added Activity from library manifest, You should not declare this activity in your app manifest.
- If you are migrating from version 1.x you should simply remove the activity in your app manifest. If you want to keep some advanced attributes, you can use features of [manifest merger](http://tools.android.com/tech-docs/new-build-system/user-guide/manifest-merger) the way you please
- Added (primary) support for proguard. Feel free to open an issue if you find something missing in case of proguard.

## 1.2
- Added Clear Key

## 1.1
- Added Search Menu to search by key and value

## 1.0.1
- Some bugfixes and moved to ActionBar Activity

## 1.0.0
- Initial Version.

# Credits
Credits to [Android DbInspector Library](https://github.com/infinum/android_dbinspector) for all the inspiration behind this library.

# Known Issues And TODOs

- [ ] Not implemented handling String Set.
- [x] Does not have UI to clear the key.
- [x] Upload in maven.
- [x] Change example to reflect read me instructions

# License
	   Copyright 2015 Prasham Trivedi
	   Licensed under the Apache License, Version 2.0 (the "License");
	   you may not use this file except in compliance with the License.
	   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

	   Unless required by applicable law or agreed to in writing, software
	   distributed under the License is distributed on an "AS IS" BASIS,
	   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	   See the License for the specific language governing permissions and
	   limitations under the License.



