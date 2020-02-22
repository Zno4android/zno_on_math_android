package com.example.jatcool.zno_on_math.dao;

import com.example.jatcool.zno_on_math.entity.User;

public interface UserDAO {
    User getUserByToken(String token);

    String getUserByEmailAndPassword(String email, String password, User user);
}
