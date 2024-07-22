package com.example.whowantstobemillioner.model;

import java.util.HashMap;

public class Question {
    private int id;
    private String questionText;
    private HashMap<String, Boolean> answers;

    public Question(int id, String questionText, HashMap<String, Boolean> answers) {
        this.id = id;
        this.questionText = questionText;
        this.answers = answers;
    }

    public Question(String questionText, HashMap<String, Boolean> answers) {
        this.questionText = questionText;
        this.answers = answers;
    }

    public int getId() {
        return id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public HashMap<String, Boolean> getAnswers() {
        return answers;
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + id +
                ", questionText='" + questionText + '\'' +
                ", answers=" + answers +
                '}';
    }

}


//package com.example.whowantstobemillioner.model;
//
//import java.util.HashMap;
//import java.util.Objects;
//
//public class Question {
//    private static int ID = 1;
//    private int id;
//    private String questionText;
//    private HashMap<String, Boolean> answers;
//
//    public Question(String questionText) {
//        this.questionText = questionText;
//        this.id = Question.ID++;
//        this.answers = new HashMap<>();
//    }
//
//    public Question(int id, String questionText, HashMap<String, Boolean> answers) {
//        this.id = id;
//        this.questionText = questionText;
//        this.answers = answers;
//    }
//
//    public int getId() {
//        return id;
//    }
//
//    public void setId(int id) {
//        this.id = id;
//    }
//
//    public String getQuestionText() {
//        return questionText;
//    }
//
//    public void setQuestionText(String questionText) {
//        this.questionText = questionText;
//    }
//
//    public HashMap<String, Boolean> getAnswers() {
//        return answers;
//    }
//
//    public void setAnswers(HashMap<String, Boolean> answers) {
//        this.answers = answers;
//    }
//    public boolean addAnswer(String answerText, boolean isRight){
//        if(answers.size() < 4){
//            if(answers.containsValue(true) && isRight){
//                //answers.put(answerText, isRight);
//                return false;
//            }
//            answers.put(answerText, isRight);
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Question question = (Question) o;
//        return id == question.id && Objects.equals(questionText, question.questionText) && Objects.equals(answers, question.answers);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, questionText, answers);
//    }
//
//    @Override
//    public String toString() {
//        return "Question{" +
//                "id=" + id +
//                ", questionText='" + questionText + '\'' +
//                ", answers=" + answers +
//                '}';
//    }
//}
