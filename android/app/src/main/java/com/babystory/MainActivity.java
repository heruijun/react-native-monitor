package com.babystory;

import com.facebook.react.ReactActivity;
import com.babystory.components.AudioWife;

public class MainActivity extends ReactActivity {

    /**
     * Returns the name of the main component registered from JavaScript.
     * This is used to schedule rendering of the component.
     */
    @Override
    protected String getMainComponentName() {
        return "BabyStory";
    }

    @Override
    public void invokeDefaultOnBackPressed() {
        super.invokeDefaultOnBackPressed();
        AudioWife.getInstance().release();
    }
}