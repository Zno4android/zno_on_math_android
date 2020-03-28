package com.example.jatcool.zno_on_math.util;

import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.exception.MathTestingException;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class MathTestingTests {
    MathTesting mathTesting;
    List<Question> questions;
    List<String> answer1;
    List<String> answer2;
    List<String> answer3;

    @Before
    public void createTest() {
        questions = new ArrayList<>();
        ArrayList<String> variants = new ArrayList<>();
        variants.add("так");
        variants.add("ні");
        answer1 = new ArrayList<>();
        answer1.add("ні");
        answer2 = new ArrayList<>();
        answer2.add("так");

        Question question1 = new Question("Чи вірна тоттожність\n5=7 ?", variants, answer1);
        Question question2 = new Question("Чи вірна тоттожність\ntg a=sin a/cos a ?", variants, answer2);
        Question question3 = new Question("Чи вірне твердження\nЯкщо у трикутників однакові усі кути, вони подібні?", variants, answer2);
        Question question4 = new Question("Чи вірне твердження\nЯкщо дві сторони одного трикутника рівні двом сторонам другого трикутника, вони подібні?", variants, answer1);

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
        mathTesting.nextQuestion(answer2);
        int actualResult = mathTesting.getCurrentQuestion();

        assertEquals(1, actualResult);
    }

    @Test
    public void nextQuestionConformity() {
        answer3 = new ArrayList<>();
        answer3.add("ні");
        answer3.add("так");

        Question question1 = new Question("Чи вірна тоттожність\n5=7 ?", null, answer3);
        questions = new ArrayList<>();
        questions.add(question1);
        mathTesting = new MathTesting(questions);

        List<String> answer4 = new ArrayList<>();
        answer4.add("так");
        answer4.add("ні");

        mathTesting.nextQuestion(answer4);
        int actualResult = mathTesting.getCountCorrect();

        assertEquals(1, actualResult);
    }

    @Test
    public void nextQuestionAfterSkip() {
        mathTesting.nextQuestion(answer2);
        mathTesting.nextQuestion(answer2);
        mathTesting.skipQuestion();
        mathTesting.nextQuestion(answer2);
        mathTesting.nextQuestion(answer2);

        mathTesting.endTest();

        assertEquals(2, mathTesting.getCountCorrect());
        assertEquals(2, mathTesting.getCountIncorrect());
    }

    @Test
    public void endTest() {
        mathTesting.nextQuestion(answer2);
        mathTesting.nextQuestion(answer2);
        mathTesting.endTest();

        assertEquals(1, mathTesting.getCountCorrect());
        assertEquals(1, mathTesting.getCountIncorrect());
        assertTrue(mathTesting.isTestPass());
    }

    @Test
    public void endTestWithSomeSkips() {
        mathTesting.nextQuestion(answer2);
        mathTesting.nextQuestion(answer2);
        mathTesting.skipQuestion();
        mathTesting.skipQuestion();
        mathTesting.nextQuestion(answer2);
        mathTesting.nextQuestion(answer2);

        assertEquals(2, mathTesting.getCountCorrect());
        assertEquals(2, mathTesting.getCountIncorrect());
        assertTrue(mathTesting.isTestPass());
    }
}