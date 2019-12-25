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

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.ConcurrentLinkedQueue;

public class HolderActivity extends FragmentActivity {
    private volatile static ConcurrentLinkedQueue<Request> sPrepareQueue = new ConcurrentLinkedQueue<>();

    private Request request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) return;

        request = sPrepareQueue.poll();
        if (request == null) {
            finish();
            return;
        }

        try {
            startActivityForResult(request.getIntent(), request.getRequestCode());
        } catch (ActivityNotFoundException e) {
            throw e;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        IResult onResult = request.getResult();
        if (onResult != null) {
            onResult.response(resultCode, data);
        }

        finish();
    }


    public static void addRequest(Request request) {
        sPrepareQueue.offer(request);
    }
}
