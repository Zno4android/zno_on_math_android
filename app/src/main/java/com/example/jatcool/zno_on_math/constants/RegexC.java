package com.example.jatcool.zno_on_math.constants;

public final class RegexC {
    public static final String VALID_EMAIL="\\b[A-Za-z]+[\\w.,]*@(gmail|mail|ukr|yandex|i|meta|rambler).(net|com|ru|ua)\\b";

    public static final String VALID_NAME="^[\\w\\.\\?\\$\\-@,!\\p{InCyrillic}]{4,}$";

    public static final String VALID_PASSWORD="[A-Za-z1-9\\.\\-_$,!?@]{8,}";
}
