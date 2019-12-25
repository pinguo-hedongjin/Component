package com.kubi.router.ui;

import android.content.Intent;

import androidx.annotation.Nullable;

public interface IResult {
    void response(int resultCode, @Nullable Intent data);
}
