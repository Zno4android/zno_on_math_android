package com.example.jatcool.zno_on_math.dao;

import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.util.MailCheck;

import java.util.List;

public interface UserDAO {
    User getUserByToken(String token);

    String getTokenByEmailAndPassword(String email, String password);

    boolean changeUser(final String token, final User user);

    List<String> getGroup();

    MailCheck checkEmail(final String email);

    boolean createUser(final User user);
}
