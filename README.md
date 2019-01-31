# FlipperView
Android version of [FlipperControl for UWP](https://github.com/JuniperPhoton/FlipperControl) . Now it's written in Kotlin.

A control that uses flip transition to change different states.
Works on API 19, but with more tests I think it works well on API 16.

![](https://github.com/JuniperPhoton/FlipperControl/blob/master/demo.gif)

## How to use

FlipperView extends from `FrameLayout` so you can use it as a `FrameLayout` but it only displays one child at a time and you can call `next()` to switch to another child. Please don't put views that do **heavy** work.

    <com.juniperphoton.flipperviewlib.FlipperView
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_centerInParent="true"
        android:clickable="true"
        android:clipChildren="false"
        android:clipToPadding="false"
        android:foreground="?android:attr/selectableItemBackground"
        android:padding="20dp"
        app:defaultIndex="0"
        app:flipAxis="X">
    
        <TextView
            android:layout_width="200dp"
            android:layout_height="50dp"
            android:background="@color/exampleColor1"
            android:gravity="center"
            android:text="TAP ME"
            android:textColor="@android:color/white"/>
    
        <TextView
            android:layout_width="200dp"
            android:layout_height="50dp"
            android:background="@color/exampleColor2"
            android:gravity="center"
            android:text="KEEP TAPPING"
            android:textColor="@android:color/white"/>
    
        <TextView
            android:layout_width="200dp"
            android:layout_height="50dp"
            android:background="@color/exampleColor3"
            android:gravity="center"
            android:text="TAP ME PLEASE"
            android:textColor="@android:color/white"/>
    </com.juniperphoton.flipperviewlib.FlipperView>

It has a sample proj to demonstrate how to use it.

There are a few attrs that control the behavior:

## defaultIndex:int
Default display index of view. Note that the value of zero points to the first view you declare in XML.

## flipDirection:int
FlipDirection. 

XML : `backToFront` or `frontToBack`

CODE: `FLIP_DIRECTION_BACK_TO_FRONT` or `FLIP_DIRECTION_FRONT_TO_BACK`

## flipAxis:int

XML : `X` or `Y`

CODE: `AXIS_X` or `AXIS_Y`

## duration:int
Animation duration in millis. The default value is 200 which I think it's fast enough.

## tapToFlip:boolean
Enable tap to flip or not. Default value is false.

## Switch views

Current there are **4** ways to switch views:

#### fun next()

Segue to next view. If it's the end of the views, then segue to the first one.

#### fun previous()

Segue to the previous view. If it's the head of the views, then segue to the last one.

### fun next(Int, Boolean, ViewAction, ViewAction?)

Segue to the specified one with/without animation, and custom the action that  will be applied on the current display view on **Exit animation** end and run the action after the **Enter animation**.

To understand the running time on both actions, please refer to the **advance** topic.

Note that both `ViewAction` has default value for Kotlin.

Please be aware of *IndexOutOfBoundsException*.

### fun refreshCurrent(ViewAction)

Perform the flip animation and run the custom action on exit animation end.

## Advance

The flipping animation contains two parts:

1. Exit animation: the current display view rotates from 0 to 90 degrees
2. Enter animation: the next display view rotates from -90 to 0 degrees

At the end of both animations, you can perform your actions. See `fun next(Int, Boolean, ViewAction, ViewAction?)` method for details.

## Note for Android P user

Since the elevation shadow in Android P is NOT control by animation transformation, thus it will cause some weird issues on Android P devices.

Thus the FlipperLayout will disable elevation during animation and apply it back for you at the end of animation.