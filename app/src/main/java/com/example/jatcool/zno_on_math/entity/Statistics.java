package com.example.jatcool.zno_on_math.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Statistics {
    @SerializedName("user")
    @Expose
    private User user;
    @SerializedName("test")
    @Expose
    private Test test;
    @SerializedName("result")
    @Expose
    private double result;
    @SerializedName("date")
    @Expose
    private Date date;

    public Statistics(User user, Test test, double result, Date date) {
        this.user = user;
        this.test = test;
        this.result = result;
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public Test getTest() {
        return test;
    }

    public double getResult() {
        return result;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "{" +
                "user:" + user +
                ", test:" + test +
                ", result:" + result +
                ", date:" + date +
                '}';
    }

    public static void main(String[] args) {
        User user = new User("email", "12345678Aw", "P-62", "Bohdan", "Kasumov", "Ruslanovich");
        List<Question> questions = new ArrayList<>();
        ArrayList<String> variants = new ArrayList<>();
        variants.add("так");
        variants.add("ні");
        Question question1 = new Question(new Theme("Тотожності"), "Чи вірна тоттожність\n5=7 ?", variants, "ні");
        Question question2 = new Question(new Theme("Тотожності"), "Чи вірна тоттожність\ntg a=sin a/cos a ?", variants, "так");
        Question question3 = new Question(new Theme("Подобность треугольников часть 1"), "Чи вірне твердження\nЯкщо у трикутників однакові усі кути, вони подібні?", variants, "так");
        Question question4 = new Question(new Theme("Подобность треугольников часть 2"), "Чи вірне твердження\nЯкщо дві сторони одного трикутника рівні двом сторонам другого трикутника, вони подібні?", variants, "ні");

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);

        Test test = new Test(1l, "wqeqwe", new Theme("123"), questions);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        String date = simpleDateFormat.format(new Date());

        Statistics statistics = new Statistics(user, test, 15.00, new Date());

        System.out.println(statistics);
    }
}
