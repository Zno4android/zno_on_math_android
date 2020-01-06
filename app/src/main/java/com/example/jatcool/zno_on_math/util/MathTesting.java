package com.example.jatcool.zno_on_math.util;

import com.example.jatcool.zno_on_math.entity.Question;
import com.example.jatcool.zno_on_math.exception.MathTestingException;

import java.util.ArrayList;

public class MathTesting implements Testing {
    private ArrayList<Question> questions;
    private ArrayList<Answer> answers;
    private int currentQuestion;
    private ArrayList<Integer> skipQuestions;
    private boolean passAllQuestions;
    private boolean testPass;
    private int countCorrect;
    private int countIncorrect;

    public MathTesting(ArrayList<Question> questions) {
        this.questions = questions;
        answers = new ArrayList<>(questions.size());
        this.currentQuestion = 0;
        skipQuestions = new ArrayList<>();
        passAllQuestions = false;
        testPass = false;
        this.countCorrect = 0;
        this.countIncorrect = 0;
    }

    /**
     * skipQuestion skip current question
     *
     * @throws MathTestingException if you have skipped this question yet or test is completed
     */
    @Override
    public void skipQuestion() {
        if (passAllQuestions ||testPass) {
            throw new MathTestingException();
        }

        answers.add(currentQuestion, null);
        skipQuestions.add(currentQuestion++);
        checkCountQuestion();
    }

    /**
     * checkCountQuestion change passAllQuestions value if you pass or skip all question
     */
    private void checkCountQuestion() {
        passAllQuestions = currentQuestion == questions.size();
    }

    /**
     * nextQuestion make answer on current question
     * if passAllQuestions is true you answer on skipped question
     *
     * @param textAnswer answer on current question
     * @throws MathTestingException if test is complete
     */
    @Override
    public void nextQuestion(String textAnswer) {
        if (testPass) {
            throw new MathTestingException();
        }

        if (passAllQuestions) {
            currentQuestion = skipQuestions.get(0);
            skipQuestions.remove(0);
            Answer answer = new Answer(textAnswer, questions.get(currentQuestion).getCorrect().equals(textAnswer));
            answers.set(currentQuestion, answer);
        } else {
            Answer answer = new Answer(textAnswer, questions.get(currentQuestion).getCorrect().equals(textAnswer));
            answers.add(currentQuestion, answer);
            currentQuestion++;
            checkCountQuestion();
        }

        checkEndTest();
    }

    /**
     * checkEndTest call endTest method if you pass all question
     */
    private void checkEndTest() {
        if (passAllQuestions && skipQuestions.isEmpty()) {
            endTest();
        }
    }

    /**
     * endTest calculate correct and incorrect answer and close access on nextQuestion and skipQuestion methods
     */
    @Override
    public void endTest() {
        if (!testPass) {
            testPass = true;
            for (Answer answer : answers) {
                if (answer.isCorrect()) {
                    countCorrect++;
                } else {
                    countIncorrect++;
                }
            }
        }
    }

    public ArrayList<Answer> getAnswers() {
        return answers;
    }

    public boolean isTestPass() {
        return testPass;
    }

    public int getCurrentQuestion() {
        return currentQuestion;
    }

    public int getCountCorrect() {
        return countCorrect;
    }

    public int getCountIncorrect() {
        return countIncorrect;
    }
}
