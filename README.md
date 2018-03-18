# VanHackathon Mobile

[![CircleCI](https://circleci.com/gh/omainegra/vanhackathon-mobile.svg?style=svg)](https://circleci.com/gh/omainegra/vanhackathon-mobile)

### Prerequisites

* [JDK 8](http://www.oracle.com/technetwork/java/javase/downloads/)
* [Gradle](https://gradle.org/install/)
* [XCode 9.2](https://developer.apple.com/xcode/)
* [Robovm](https://github.com/MobiVM/robovm)


### Android
```
$ ./gradlew :android:build
$ ./gradlew :android:installDebug
```

### iOS
```
$ ./gradlew :ios:build
$ ./gradlew :ios:launchIPhoneSimulator
```