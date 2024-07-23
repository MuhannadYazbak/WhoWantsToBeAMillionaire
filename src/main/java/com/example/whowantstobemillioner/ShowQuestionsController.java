package com.example.whowantstobemillioner;

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

        String query = "SELECT * FROM questions";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String questionText = resultSet.getString("question_text");
                HashMap<String, Boolean> answers = new HashMap<>();
                answers.put(resultSet.getString("answer1"), resultSet.getBoolean("is_correct1"));
                answers.put(resultSet.getString("answer2"), resultSet.getBoolean("is_correct2"));
                answers.put(resultSet.getString("answer3"), resultSet.getBoolean("is_correct3"));
                answers.put(resultSet.getString("answer4"), resultSet.getBoolean("is_correct4"));

                Question question = new Question(id, questionText, answers);
                questionsList.add(question);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionsList;
    }
}
