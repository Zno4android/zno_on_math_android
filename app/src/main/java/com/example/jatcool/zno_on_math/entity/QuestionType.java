package com.example.jatcool.zno_on_math.entity;

public enum QuestionType {
    CHOOSE_ANSWER("Виберіть правельну(ні) відповідь(ді)"), CONFORMITY("Відповідність"), WRITE_ANSWER("Вести відповідь");

    private String name;

    QuestionType(String name) {
        this.name = name;
    }
//    public static Status getStatus(User user) {
//        int statusId = user.getStatus();
//        return Status.values()[statusId];
//    }

    public static String[] getTypes() {
        String[] types = new String[QuestionType.values().length];
        int i = 0;
        for (QuestionType questionType : QuestionType.values()) {
            types[i++] = questionType.getName();
        }

        return types;
    }

    public String getName() {
        return name().toLowerCase();
    }
}
