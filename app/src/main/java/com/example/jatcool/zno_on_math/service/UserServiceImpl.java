package com.example.jatcool.zno_on_math.service;

import com.example.jatcool.zno_on_math.dao.UserDAO;
import com.example.jatcool.zno_on_math.entity.User;

public class UserServiceImpl implements UserService {
    private UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User getUserByToken(String token) {
        return userDAO.getUserByToken(token);
    }

    @Override
    public String getUserByEmailAndPassword(String email, String password, User user) {
        return userDAO.getUserByEmailAndPassword(email, password, user);
    }
}
