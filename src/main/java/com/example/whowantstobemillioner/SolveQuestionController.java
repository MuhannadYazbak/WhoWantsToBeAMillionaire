package com.example.whowantstobemillioner;

import com.example.whowantstobemillioner.model.Question;
import javafx.application.Application;
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

        try (Connection conn = DriverManager.getConnection(url, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM questions")) {

            while (rs.next()) {
                int id = rs.getInt("id");
                String questionText = rs.getString("question_text");
                HashMap<String, Boolean> answers = new HashMap<>();
                answers.put(rs.getString("answer1"), rs.getBoolean("is_correct1"));
                answers.put(rs.getString("answer2"), rs.getBoolean("is_correct2"));
                answers.put(rs.getString("answer3"), rs.getBoolean("is_correct3"));
                answers.put(rs.getString("answer4"), rs.getBoolean("is_correct4"));

                questions.add(new Question(id, questionText, answers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayQuestion(Question question) {
        questionText.setText(question.getQuestionText());

        List<String> answerTexts = new ArrayList<>(question.getAnswers().keySet());
        answer1.setText(answerTexts.get(0));
        answer2.setText(answerTexts.get(1));
        answer3.setText(answerTexts.get(2));
        answer4.setText(answerTexts.get(3));
    }

    @FXML
    private void answerClicked(MouseEvent event) {
        Button clickedButton = (Button) event.getSource();
        String selectedAnswer = clickedButton.getText();
        Question currentQuestion = questions.get(currentQuestionIndex);

        boolean isCorrect = currentQuestion.getAnswers().get(selectedAnswer);
        if (isCorrect) {
            System.out.println("Correct!");
        } else {
            System.out.println("Incorrect!");
        }

        // Move to the next question or handle end of quiz
        currentQuestionIndex++;
        if (currentQuestionIndex < questions.size()) {
            displayQuestion(questions.get(currentQuestionIndex));
        } else {
            showQuizFinishedStage();

        }
    }
    private void showQuizFinishedStage() {
        try {
            // Load the "Home" FXML again
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Home.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.setTitle("Who Wants To Be A Millionaire");
            stage.setScene(new Scene(root));
            stage.show();

            // Close the current stage
            Stage currentStage = (Stage) questionText.getScene().getWindow();
            currentStage.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
