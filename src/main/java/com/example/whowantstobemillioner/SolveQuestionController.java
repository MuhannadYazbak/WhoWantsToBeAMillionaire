package com.example.whowantstobemillioner;

import com.example.whowantstobemillioner.model.Answer;
import com.example.whowantstobemillioner.model.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.whowantstobemillioner.ConfigReader.readConfig;

public class SolveQuestionController {

    @FXML
    private Label questionText;
    @FXML
    private Button answer1;
    @FXML
    private Button answer2;
    @FXML
    private Button answer3;
    @FXML
    private Button answer4;

    private List<Question> questions = new ArrayList<>();
    private int currentQuestionIndex = 0;

    @FXML
    public void initialize() throws IOException {
        // Load questions from the database
        loadQuestionsFromDatabase();

        // Display the first question
        if (!questions.isEmpty()) {
            displayQuestion(questions.get(currentQuestionIndex));
        }
    }

    private void loadQuestionsFromDatabase() throws IOException {
        DBConfig config = readConfig("config.json");
        String url = config.getDbUrl();
        String user = config.getUsername();
        String password = config.getPassword();

        String query = "SELECT q.id AS question_id, q.question_text, a.id AS answer_id, a.answer_text, a.is_correct " +
                "FROM questions q " +
                "JOIN answers a ON q.id = a.question_id " +
                "ORDER BY q.id, a.id";

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            Question currentQuestion = null;
            int currentQuestionId = -1;

            while (rs.next()) {
                int questionId = rs.getInt("question_id");
                String questionText = rs.getString("question_text");
                int answerId = rs.getInt("answer_id");
                String answerText = rs.getString("answer_text");
                boolean isCorrect = rs.getBoolean("is_correct");

                if (currentQuestionId != questionId) {
                    if (currentQuestion != null) {
                        questions.add(currentQuestion);
                    }
                    currentQuestionId = questionId;
                    currentQuestion = new Question();
                    currentQuestion.setId(questionId);
                    currentQuestion.setQuestionText(questionText);
                    currentQuestion.setAnswers(new ArrayList<>());
                }

                Answer answer = new Answer(answerId, answerText, isCorrect);
                currentQuestion.getAnswers().add(answer);
            }

            if (currentQuestion != null) {
                questions.add(currentQuestion);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(Question question) {
        questionText.setText(question.getQuestionText());

        List<Answer> answers = question.getAnswers();
        answer1.setText(answers.get(0).getAnswerText());
        answer2.setText(answers.get(1).getAnswerText());
        answer3.setText(answers.get(2).getAnswerText());
        answer4.setText(answers.get(3).getAnswerText());
    }

    @FXML
    private void answerClicked(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        String selectedAnswer = clickedButton.getText();
        Question currentQuestion = questions.get(currentQuestionIndex);

        boolean isCorrect = false;
        for (Answer answer : currentQuestion.getAnswers()) {
            if (answer.getAnswerText().equals(selectedAnswer)) {
                isCorrect = answer.isCorrect();
                break;
            }
        }

        if (isCorrect) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect!");
        }

        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion(questions.get(currentQuestionIndex));
        } else {
            showQuizFinishedStage();
        }
    }

    private void showQuizFinishedStage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setTitle("Who Wants To Be A Millionaire");
            stage.setScene(new Scene(root));
            stage.show();

            Stage currentStage = (Stage) questionText.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
