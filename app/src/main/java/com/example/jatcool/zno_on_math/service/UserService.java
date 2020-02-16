package com.example.jatcool.zno_on_math.service;

import com.example.jatcool.zno_on_math.entity.User;

public interface UserService {
    User getUserByToken(String token);

    String getUserByEmailAndPassword(String email, String password);
}
