package com.example.jatcool.zno_on_math.util;

import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.entity.Theme;
import com.example.jatcool.zno_on_math.exception.MathTestingException;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class MathTestingTests {
    MathTesting mathTesting;
    ArrayList<Question> questions;

    @Before
    public void createTest() {
        questions = new ArrayList<>();
        ArrayList<String> variants = new ArrayList<>();
        variants.add("так");
        variants.add("ні");
        Question question1 = new Question("Тотожності", "Чи вірна тоттожність\n5=7 ?", variants, "ні");
        Question question2 = new Question("Тотожності", "Чи вірна тоттожність\ntg a=sin a/cos a ?", variants, "так");
        Question question3 = new Question("Подобность треугольников часть 1", "Чи вірне твердження\nЯкщо у трикутників однакові усі кути, вони подібні?", variants, "так");
        Question question4 = new Question("Подобность треугольников часть 2", "Чи вірне твердження\nЯкщо дві сторони одного трикутника рівні двом сторонам другого трикутника, вони подібні?", variants, "ні");

        questions.add(question1);
        questions.add(question2);
        questions.add(question3);
        questions.add(question4);
        mathTesting = new MathTesting(questions);
    }

    @Test
    public void skipQuestion() {
        mathTesting.skipQuestion();
        int actualResult = mathTesting.getCurrentQuestion();

        assertEquals(1, actualResult);
    }

    @Test(expected = MathTestingException.class)
    public void skipQuestionWithException() {
        mathTesting.skipQuestion();
        mathTesting.skipQuestion();
        mathTesting.skipQuestion();
        mathTesting.skipQuestion();
        mathTesting.skipQuestion();
    }

    @Test
    public void nextQuestion() {
        mathTesting.nextQuestion("так");
        int actualResult = mathTesting.getCurrentQuestion();

        assertEquals(1, actualResult);
    }

    @Test
    public void nextQuestionAfterSkip() {
        mathTesting.nextQuestion("так");
        mathTesting.nextQuestion("так");
        mathTesting.skipQuestion();
        mathTesting.nextQuestion("так");
        mathTesting.nextQuestion("так");

        mathTesting.endTest();

        assertEquals(2, mathTesting.getCountCorrect());
        assertEquals(2, mathTesting.getCountIncorrect());
    }

    @Test
    public void endTest() {
        mathTesting.nextQuestion("так");
        mathTesting.nextQuestion("так");
        mathTesting.endTest();

        assertEquals(1, mathTesting.getCountCorrect());
        assertEquals(1, mathTesting.getCountIncorrect());
        assertTrue(mathTesting.isTestPass());
    }

    @Test
    public void endTestWithSomeSkips() {
        mathTesting.nextQuestion("так");
        mathTesting.nextQuestion("так");
        mathTesting.skipQuestion();
        mathTesting.skipQuestion();
        mathTesting.nextQuestion("так");
        mathTesting.nextQuestion("так");

        assertEquals(2, mathTesting.getCountCorrect());
        assertEquals(2, mathTesting.getCountIncorrect());
        assertTrue(mathTesting.isTestPass());
    }
}