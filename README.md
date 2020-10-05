# ExpenseTrackerDemo

**ExpenseTrackerDemo** is a demo application for extracting all transaction messages from user's Inbox and plot them onto the screen.

I choose kotlin as primary language for this demo because Kotlin have a lot more features than Java like null safety, extension functions, named parameters etc which enforces code readability and reduced the number of LOC.

# Features

Below are some of the features of this demo application.
- Ability to fetch all SMS messages from User's device. 
- Filter out debited and credited messages and plot a chart and list of messages on screen.

# Work Flow

* First this application checks if user have sufficient permissions or not (SMS permissions). If not than the applications will ask user to grant all permissions otherwise it will load 
all messages from user's device.
* Than those loaded messages are filtered out for finding only transactional messages out of them.
* Than we find the amounts from those messages using regular expressions after that those messages are added to different lists based on the condition weather 
they were credited message or debited message.
* After filtering them we pass those lists and total of the debited and credited amount to our Activity so we can plot a Pie chart and list based on the data received.

# Setup

This project was built using Android studio v4.0.1 so if any gradle issue comes up than the code is required to be opened with latest version of Android studio i.e 4.0.1 or else 
the gradle version of project requires to be downgraded first to use on any older version of Android Studio.

## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.

## Built With
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes. 
  - [ViewBinding](https://developer.android.com/topic/libraries/view-binding) - Generates a binding class for each XML layout file present in that module and allows you to more easily write code that interacts with views.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [MPAndroidChart](https://github.com/PhilJay/MPAndroidChart) - Library used to plot charts in an Application.
