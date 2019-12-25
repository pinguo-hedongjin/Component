/*
 * Copyright 2016 Copyright 2016 Víctor Albertos
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kubi.router.ui;

import android.content.Intent;

import androidx.annotation.Nullable;

import java.lang.ref.WeakReference;

public class Request {
    private volatile static int sRequestCode = 0;
    private final int requestCode = generateRequestCode();
    private final Intent intent;
    private final WeakReference<IResult> onResult;

    public Request(Intent intent, IResult onResult) {
        this.intent = intent;
        this.onResult = new WeakReference<>(onResult);
    }

    public int getRequestCode() {
        return requestCode;
    }

    public IResult getResult() {
        return onResult.get();
    }

    @Nullable
    public Intent getIntent() {
        return intent;
    }

    public static synchronized int generateRequestCode() {
        if ((sRequestCode & 0xffff0000) == 0) {
            return sRequestCode++;
        } else {
            sRequestCode = 0;
            return sRequestCode;
        }
    }
}
