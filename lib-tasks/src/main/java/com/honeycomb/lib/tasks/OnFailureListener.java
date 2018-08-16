package com.honeycomb.lib.tasks;

import android.support.annotation.Nullable;

public interface OnFailureListener {

    void onFailure(@Nullable Exception exception);
}
