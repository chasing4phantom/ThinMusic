package com.example.zhang.thinmusic.utils;

import android.annotation.TargetApi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhang on 2018/3/20.
 */
@Target(ElementType.FIELD)//Annotation所修饰描述域
@Retention(RetentionPolicy.RUNTIME)//上行注解仅在运行时有效

public @interface Bind {
    int value();
}
