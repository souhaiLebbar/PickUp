# PickUp
Pickup is an android application to help those in the same area meet and play basketball. The source code of this app can be modified to meet any other sport or activity because of its modular design.

## Getting Started
##### Prerequisites
- [Android Studio](https://developer.android.com/studio/index.html)
- Capabilities to run an Android Emulator or an Android Device capable of meeting [System Requirements](#System Requirements)

###### System Requirements
- 8+ MB of space
- Android device running Android SDK 24+

##### Installation
- Follow Android Studio Installation
- Clone repo in directory of your choice
- Open the PickUp project within Android Studio
- [See Installation Guide](https://docs.google.com/document/d/1mZIMAq4nOLC-hFaJL55Z0Lphmc27T0TDkhn8_lG0c3k/edit)

##### Running Tests
Under the [testing folder](https://uocis.assembla.com/spaces/cis422w18-team3/git/source/master/PickUp/app/src/androidTest/java/com/example/rustybucket/pickup) you will find instrumented tests. Instrumented tests are our integrated tests that will only work if an emulator or an Android device is running to provide Android Services that those tests rely on.

##### Running the application
[See User Guide](https://docs.google.com/document/d/1OgtAOpHV885ReNMs61MejThJNMSUp2zaPLjJEa2voXU/edit)

##### Important code
- Where the [Java files](https://uocis.assembla.com/spaces/cis422w18-team3/git/source/master/PickUp/app/src/main/java/com/example/rustybucket/pickup) are located
  - [DataManager](https://uocis.assembla.com/spaces/cis422w18-team3/git/source/master/PickUp/app/src/main/java/com/example/rustybucket/pickup/DataManager.java) - Handled all interactions with database and model classes
  - [LocManager](https://uocis.assembla.com/spaces/cis422w18-team3/git/source/master/PickUp/app/src/main/java/com/example/rustybucket/pickup/LocManager.java) - Handled all location related interactions (Permissions/Geocoder API/Longitude & Latitude)
- Where the [XML layout files](https://uocis.assembla.com/spaces/cis422w18-team3/git/source/master/PickUp/app/src/main/res)  are located

##### Services Used
- Google Play Services - Handling Authentication and Google Sign in
- Android device location system
- [Firebase](https://firebase.google.com/) - Real-time Database and Notification Management

## Current Features
- Send notifications to those in your same postal code
- View notifications in your same postal code
- Create/Edit custom Profile

## Features planned
- Team Gatherings
- Search for people in n-mile radius away from you instead of being restricted by zip code
- View Profiles
- In-App messaging
- Google Maps integration

## Contact Information
- Brandon Sov - Team Manager/API Integrator - bhs@uoregon.edu
- Shohei Etzel - Requirements Analyst - sse@uoregon.edu
- Adam Chen - Software Architect/Quality Assurance - achen@uoregon.edu
- Erik Lopez - Configurations/Backend Developer - elopezr2@uoregon.edu
- Jeffrey Knees - Full Stack Developer - jknees@uoregon.edu

## Note
- Please see more details from this link: https://uocis.assembla.com/spaces/cis422w18-team3/wiki, including system architecture, Quality Assurance plan
