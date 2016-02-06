#### xyzreader
#Udacity Material Design Project

This application show a list of articles fetched from https://dl.dropboxusercontent.com/u/231329/xyzreader_data. All titles, cover images, and information come from this site.

This project has been modified to implement material design as per the project requirement of Project 5 "Make Your App Material" under Udacity Android Nanodegree program.

Main focus was on usage of CoordinatorLayout, AppBarLayout and associated toolbar, FloatingActionBar, Snackbar. Enter/Exit transitions were also implemented.

Followings changes have been made:

- Coordinatorlayout / Appbarlayout with associated toolbar, FloatingActionBar added
- Color theme added
- Dimension modified for different sized devices
- Snackbar added when no internet connection
- Implemented swiperefreshcode listener
- Stickybroadcast converted to localbroadcastmanager
- About activity added as menu
- ImageLoaderHelper class modified to fix image height and widht
- Enter/exit transition to detail activity
