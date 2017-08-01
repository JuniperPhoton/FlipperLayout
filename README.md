# FlipperView
Android version of [FlipperControl for UWP](https://github.com/JuniperPhoton/FlipperControl) . Now it's re-written in Kotlin.

A control that uses flip transition to change different states.
Works on API 19, but with more tests I think it works well on API 16.

![](https://github.com/JuniperPhoton/FlipperControl/blob/master/demo.gif)

# Using FlipperView in your application
If you are building with Gradle, simply add the following line to the dependencies section of your app .gradle file:

```
compile 'com.juniperphoton:flipperlayout:1.1.1'
```

# Usage
FlipperView extends from `FrameLayout` so you can use it as a normal`FrameLayout`. However it only displays one child at a time and you can call `next()` or `previous()` to segue to another child. Please don't put views that do **heavy** work.

    <com.juniperphoton.flipperview.FlipperView
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
    </com.juniperphoton.flipperview.FlipperView>

It has a sample project to demonstrate how to use it.

There are a few properties that you can configure.

## defaultIndex: Int
Default display index of view. Note that the value of zero points to the first view you declare in XML.

## flipDirection: Int
Flip direction. 

XML : `backToFront` or `frontToBack`

CODE: `FLIP_DIRECTION_BACK_TO_FRONT` or `FLIP_DIRECTION_FRONT_TO_BACK`

## flipAxis: Int

XML : `X` or `Y`

CODE: `AXIS_X` or `AXIS_Y`

## duration: Int
Animation duration in millis. The default value is 200 which I think it's fast enough.

##tapToFlip:boolean
Enable tap to flip or not. Default value is false.

## Way to segue views

Current there are **3** ways to segue views:

- next();
- next(index:Int, animate: Boolean);
- previous();

Please be aware of `IndexOutOfBoundsException` while calling `next(index:Int, animate: Boolean)`.
