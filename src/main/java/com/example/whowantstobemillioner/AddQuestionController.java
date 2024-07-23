package com.example.whowantstobemillioner;

import com.example.whowantstobemillioner.model.Question;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.whowantstobemillioner.ConfigReader.readConfig;

public class AddQuestionController {
    @FXML
    private Button homeBtn, addQuestionBtn;
    @FXML
    private TextField questionTF;
    @FXML
    private TextField answer1TF, answer2TF, answer3TF, answer4TF;
    @FXML
    private ComboBox<Integer> rightAnswerBox;
    int rightAnswer = 0;

    @FXML
    public void initialize() {
        for (int i = 1; i <= 4; i++) {
            rightAnswerBox.getItems().add(i);
        }
        rightAnswerBox.setOnAction(event -> {
            rightAnswer = rightAnswerBox.getValue();
            System.out.println("Selected right answer is: "+ rightAnswer);
        });
    }


    String questionText, answer1, answer2, answer3, answer4;
    List<Question> questions = new ArrayList<>();
    @FXML
    private void goHome() throws IOException {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/whowantstobemillioner/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    private boolean validQuestion() throws IOException {
        questionText = questionTF.getText();
        answer1 = answer1TF.getText();
        answer2 = answer2TF.getText();
        answer3 = answer3TF.getText();
        answer4 = answer4TF.getText();
        List<String> answersList = new ArrayList<>();
        answersList.add(answer1);
        answersList.add(answer2);
        answersList.add(answer3);
        answersList.add(answer4);
        rightAnswer = rightAnswerBox.getValue();
        HashMap<String, Boolean> answers = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            answers.put(answersList.get(i), i == (rightAnswer - 1));
        }
        if (questionText.isBlank() || answer1.isBlank() || answer2.isBlank() || answer3.isBlank() || answer4.isBlank() || rightAnswer == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please fill all fields before adding the question");
            alert.setTitle("Add Question Error");
            alert.showAndWait();
            return false;
        }
        Question question = new Question(questionText, answers);
        insertQuestion(question);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Added new question");
        alert.setHeaderText("New Question Added Successfully");
        alert.setContentText(question.toString());
        alert.showAndWait();
        return true;

    }
    private static void insertQuestion(Question question) throws IOException {
        DBConfig config = readConfig("config.json");
        String url = config.getDbUrl();
        String user = config.getUsername();
        String password = config.getPassword();

        String query = "INSERT INTO questions (question_text, answer1, is_correct1, answer2, is_correct2, answer3, is_correct3, answer4, is_correct4) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, question.getQuestionText());

            int index = 2;
            for (String answer : question.getAnswers().keySet()) {
                preparedStatement.setString(index, answer);
                preparedStatement.setBoolean(index + 1, question.getAnswers().get(answer));
                index += 2;
            }

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void addQuestion() throws IOException {
        validQuestion();
    }
}
