<h1 align="center"> GameMingle </h1> <br>
<p align="center">
    <img alt="GameMingle" title="GameMingle" src="https://github.com/TURPCUN/GameMingle/blob/develop/gameminglelogo.png" width="250">
</p>

<p align="center">
  Board Game Event Planner Application.
</p>

## Table of Contents

- [Introduction](#introduction)
- [Features](#features)
- [Feedback](#feedback)
- [Contributors](#contributors)
- [Build Process](#build-process)

<!-- END doctoc generated TOC please keep comment here to allow auto update -->

## Introduction

With the advancement of technology and people's increasing reliance on online friendships, establishing meaningful in-person connections has become more challenging. Online gaming with virtual friends can't fully replace the joy of sitting down with real friends to play board games over a cup of coffee. However, as our ability to make friends face-to-face diminishes, it's becoming increasingly difficult to meet and spend time with like-minded individuals who share our interests.

The GameMingle App aims to rekindle the experience of gathering with friends as in the past and even foster new friendships. Users can add their favorite board games, both the ones they love and own, through the app. They can then connect with other users interested in playing the same board game and schedule meetings to enjoy their favorite games together over a cup of coffee..

## Features

A few of the things you can do with GameMingle:

* Create Events: Plan board game events by setting details like date, time, location, game type.
* Search Events: Discover events based on game type.
* Manage Participants: Invite participants or approve join requests for your events.
* Real-Time Chat: Communicate with participants through in-app messaging.
* Report Users: Ensure safety by reporting users in cases of inappropriate behavior.
* Receive Notifications: Stay updated with event changes, messages, and participation status.
* Profile Management: Update your profile, view past events, and upload profile pictures.
* Favorite Games: Save games you’re interested in to easily access them later.
* Feedback and Ratings: Provide feedback on events and rate other participants.

<p align="center">
  <img src = "https://github.com/TURPCUN/GameMingle/blob/develop/screens1.png" width=800>
</p>

<p align="center">
  <img src = "https://github.com/TURPCUN/GameMingle/blob/develop/screens2.png" width=800>
</p>

<p align="center">
  <img src = "https://github.com/TURPCUN/GameMingle/blob/develop/screens3.png" width=800>
</p>

## Feedback

Feel free to send me feedback: [file an issue](https://github.com/TURPCUN/GameMingle/issues/new). Feature requests are always welcome.

## Build Process

To build and run the GameMingle Application, follow these steps:

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/TURPCUN/GameMingle.git
   ```

2. **Open the Project in Android Studio:**
   - Open Android Studio.
   - Select **Open an existing project**, and navigate to the cloned folder.

3. **Sync the Project:**
   - Android Studio will automatically sync the project. If not, click on **File > Sync Project with Gradle Files**.

4. **Configure Firebase API:**
   - Go to the [Firebase Console](https://console.firebase.google.com/).
   - Create a new project or select an existing project.
   - Add an Android app to your Firebase project and download the `google-services.json` file.
   - Copy the `google-services.json` file into the `app/` folder of your Android project.

5. **Set Up Firebase Realtime Database and Storage:**
   - **Realtime Database**: In the Firebase Console, navigate to Realtime Database and create the required tables with appropriate rules and structure to match your application needs. (Table structure details will be added soon.)
   - **Firebase Storage**: Set up Firebase Storage by creating the necessary buckets and setting appropriate access rules to ensure data security and user privacy.(Table structure details will be added soon.)

6. **Build the Project:**
   - Click on **Build > Make Project**.

7. **Run the App:**
   - To run the app on an emulator or connected device, click on **Run > Run 'app'**.

8. **Dependencies:**
   - Ensure that all required dependencies are correctly installed. You can view and manage dependencies in the `build.gradle` file.
