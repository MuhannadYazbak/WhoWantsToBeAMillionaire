package com.example.whowantstobemillioner;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {
    @FXML
    private Button startQuizBtn;
    @FXML
    private Button showQuestionsBtn;
    @FXML
    private Button addQuestionBtn;
    @FXML
    private Button exitBtn;

    @FXML
    private void handleExit() {
        // Close the current stage
        Stage stage = (Stage) exitBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void showQuestions() throws IOException {
        Stage stage = (Stage) showQuestionsBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/whowantstobemillioner/ShowQuestions.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    private void startQuiz() throws IOException {
        Stage stage = (Stage) startQuizBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/whowantstobemillioner/SolveQuestion.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }
    @FXML
    private void addQuestion() throws IOException {
        Stage stage = (Stage) addQuestionBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/whowantstobemillioner/AddQuestion.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

}
