# Dog Outside

A simple app to try some basic Android app development.

## Features

* Set the name of your dog
* Set whether your dog is "Outside", "Inside", or "In Bed"
* Works on Android 7.1.1 and up

## How it works 

All data is stored in preferences because that was the simplest.

Setting that your dog to outside saves the time that set the dog outside. I then starts a background process that will update the notification that your dog is outside. You can't dismiss the notification, you have to set that your dog is back inside.

Tapping the notification will take you back to the main activity to set if your dog is back inside.


## Sources

* https://github.com/udacity/ud851-Sunshine
* https://github.com/udacity/ud851-Exercises
* https://proandroiddev.com/deep-dive-into-android-services-4830b8c9a09
