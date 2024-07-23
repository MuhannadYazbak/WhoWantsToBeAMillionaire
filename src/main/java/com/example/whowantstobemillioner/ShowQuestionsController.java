package com.example.whowantstobemillioner;

import com.example.whowantstobemillioner.model.Answer;
import com.example.whowantstobemillioner.model.Question;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.whowantstobemillioner.ConfigReader.readConfig;

public class ShowQuestionsController {

    @FXML
    private Button homeBtn;
    @FXML
    private TableView<Question> questionsTable;
    @FXML
    private TableColumn<Question, Integer> idColumn;
    @FXML
    private TableColumn<Question, String> questionTextColumn;

    @FXML
    public void initialize() throws IOException {
        setupTableColumns();
        ObservableList<Question> data = fetchQuestionsFromDatabase();
        questionsTable.setItems(data);
    }

    @FXML
    private void goHome() throws IOException {
        Stage stage = (Stage) homeBtn.getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/whowantstobemillioner/Home.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
    }

    private void setupTableColumns() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        questionTextColumn.setCellValueFactory(new PropertyValueFactory<>("questionText"));
    }

    private ObservableList<Question> fetchQuestionsFromDatabase() throws IOException {
        ObservableList<Question> questionsList = FXCollections.observableArrayList();

        DBConfig config = readConfig("config.json");
        String url = config.getDbUrl();
        String user = config.getUsername();
        String password = config.getPassword();

        String query = "SELECT q.id AS question_id, q.question_text, a.id AS answer_id, a.answer_text, a.is_correct " +
                "FROM questions q " +
                "JOIN answers a ON q.id = a.question_id " +
                "ORDER BY q.id, a.id";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Temporary variables to hold the current question and its answers
            Question currentQuestion = null;
            int currentQuestionId = -1;

            while (resultSet.next()) {
                int questionId = resultSet.getInt("question_id");
                String questionText = resultSet.getString("question_text");
                int answerId = resultSet.getInt("answer_id");
                String answerText = resultSet.getString("answer_text");
                boolean isCorrect = resultSet.getBoolean("is_correct");

                // If we encounter a new question, save the previous one (if exists) and start a new one
                if (currentQuestionId != questionId) {
                    if (currentQuestion != null) {
                        questionsList.add(currentQuestion);
                    }
                    currentQuestionId = questionId;
                    currentQuestion = new Question();
                    currentQuestion.setId(questionId);
                    currentQuestion.setQuestionText(questionText);
                    currentQuestion.setAnswers(new ArrayList<>());
                }

                // Add the answer to the current question
                Answer answer = new Answer(answerId, answerText, isCorrect);
                currentQuestion.getAnswers().add(answer);
            }

            // Add the last question if exists
            if (currentQuestion != null) {
                questionsList.add(currentQuestion);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionsList;
    }

}
