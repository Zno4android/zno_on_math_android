package com.example.jatcool.zno_on_math.service;

import com.example.jatcool.zno_on_math.dao.UserDAO;
import com.example.jatcool.zno_on_math.entity.User;
import com.example.jatcool.zno_on_math.util.MailCheck;

import java.util.List;

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
    public String getTokenByEmailAndPassword(String email, String password) {
        return userDAO.getTokenByEmailAndPassword(email, password);
    }

    @Override
    public boolean changeUser(String token, User user) {
        return userDAO.changeUser(token, user);
    }

    @Override
    public List<String> getGroup() {
        return userDAO.getGroup();
    }

    @Override
    public MailCheck checkEmail(String email) {
        return userDAO.checkEmail(email);
    }

    @Override
    public boolean createUser(User user) {
        return userDAO.createUser(user);
    }
}
