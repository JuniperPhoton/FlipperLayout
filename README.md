# FlipperView
Android version of [FlipperControl for UWP](https://github.com/JuniperPhoton/FlipperControl)

A control that uses flip transition to change different states.
Works on API 19, but with more tests I think it can work well in API 16.

![](https://github.com/JuniperPhoton/FlipperControl/blob/master/demo.gif)

##How to use

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
        app:flipAxis="0"
        >

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

##defaultIndex:int
Default display index of view. Note that the value of zero points to the first view you declar in XML.

##flipDirection:int
FlipDirection. 

backToFront or frontToBack

##flipAxis:int
`X` or `Y`

##duration:int
Animation duration in millis. The default value is 200 which I think it's fast enough.

##tapToFlip:boolean
Enable tap to flip or not. Default value is false.

Current there are 3 ways to switch views:

- next();
- next(int index);
- previous();

Please be aware of *IndexOutOfBoundsException*.
