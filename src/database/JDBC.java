package database;

import java.sql.*;
import java.util.ArrayList;

public class JDBC {
    // mySQL Configuration
    private static final String OB_URL = "jdbc:mysql://127.0.0.1:3306/quiz_qui_db";
    private static final  String OB_USERNAME = "root";
    private static final  String OB_PASSWORD = "peace@966721";

    /*
        question - the question to be inserted
        category - the category of the question to be inserted if not already in the database
        answers -  the answer to be inserted
        correctIndex - determines which of the answer is the correct answer
     */

    public  static boolean saveQuestionCategoryAndAnswerCategoryToDatabase(String question,
                                                                           String category, String[] answers, int correctIndex){

      try {
          // establish a database connection
          Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
          );

          // insert category if it's new, otherwise retrieve it from the database
          Category categoryObj = getCategory(category);
          if(categoryObj == null){
             // insert new category to database
             categoryObj = insertCategory(category);
          }

         // insert question to database
          Question questionObj = insertQuestion(categoryObj, question);

          // insert answers to database
          return insertAnswers(questionObj, answers, correctIndex);

      }catch (Exception e){
        e.printStackTrace();

      }
      return false;
    }

    // question methods
    public static  ArrayList<Question> getQuestion(Category category){
        ArrayList<Question> questions = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            // query that retrieves all the questions of a category in random order
            PreparedStatement getQuestionQuery = connection.prepareStatement(
                    "SELECT * FROM QUESTION JOIN CATEGORY " +
                            "ON QUESTION.CATEGORY_ID = CATEGORY.CATEGORY_ID " +
                            "WHERE CATEGORY.CATEGORY_NAME = ? ORDER BY RAND()"
            );
            getQuestionQuery.setString(1, category.getCategoryName());

            ResultSet resultSet = getQuestionQuery.executeQuery();
            while (resultSet.next()){
                int questionId = resultSet.getInt("question_id");
                int categoryId = resultSet.getInt("category_id");
                String question = resultSet.getString("question_text");
                questions.add(new Question(questionId, categoryId, question));
            }

            return questions;
        }catch (Exception e){
            e.printStackTrace();
        }

        // return null if it could not find the questions in the database
        return null;
    }

    private static  Question insertQuestion(Category category, String questionText){
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            PreparedStatement insertQuestionQuery = connection.prepareStatement(
                    "INSERT INTO QUESTION(CATEGORY_ID, QUESTION_TEXT) " +
                            "VALUES(?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            insertQuestionQuery.setInt(1, category.getCategoryId());
            insertQuestionQuery.setString(2,questionText);
            insertQuestionQuery.executeUpdate();

            // check for the question id
            ResultSet resultSet = insertQuestionQuery.getGeneratedKeys();
            if (resultSet.next()){
                int questionId = resultSet.getInt(1);
                return  new Question(questionId,category.getCategoryId(), questionText);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        // return null if it there was an error inserting the question to the database
        return null;

    }

    public static Category getCategory(String category){
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            PreparedStatement getCategoryQuery = connection.prepareStatement("SELECT * FROM CATEGORY WHERE CATEGORY_NAME = ? ");
            getCategoryQuery.setString(1, category);
            //execute query and store results
            ResultSet resultSet = getCategoryQuery.executeQuery();
            if (resultSet.next()){
                // found the category
                int categoryId = resultSet.getInt("category_id");
                return  new Category(categoryId, category);
            }


        }catch (Exception e){
            e.printStackTrace();
        }

        // return null if it could not find the category in the database
        return null;
    }
    public static ArrayList<String> getCategories(){
        ArrayList<String> categoryList = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            Statement getCategoryQuery = connection.createStatement();
            ResultSet resultSet = getCategoryQuery.executeQuery("SELECT * FROM CATEGORY");

            while (resultSet.next()){
                String categoryName = resultSet.getString("category_name");
                categoryList.add(categoryName);
            }
            return categoryList;
        }catch (Exception e){
            e.printStackTrace();
        }

        // return null if it could not find the categories in the database
        return null;
    }
    private static  Category insertCategory(String category){
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            PreparedStatement insertCategoryQuery = connection.prepareStatement(
                    "INSERT INTO CATEGORY(CATEGORY_NAME) " +
                    "VALUES(?)",
                    Statement.RETURN_GENERATED_KEYS
                    );
            insertCategoryQuery.setString(1, category);
            insertCategoryQuery.executeUpdate();

            // get the category id that gets automatically incremented for each new insert in the category table
            ResultSet resultSet = insertCategoryQuery.getGeneratedKeys();
            if (resultSet.next()){
                int categoryId = resultSet.getInt(1);
                return new Category(categoryId, category);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        // return null if it could not find the category in the database
        return null;
    }


    // answer methods
    public  static  ArrayList<Answer> getAnswers(Question question){
        ArrayList<Answer> answers = new ArrayList<>();
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            // query that retrieves all the answers of a category in random order
            PreparedStatement getAnswerQuery = connection.prepareStatement(
                    "SELECT * FROM QUESTION JOIN ANSWER " +
                            "ON QUESTION.QUESTION_ID = ANSWER.QUESTION_ID " +
                            "WHERE QUESTION.QUESTION_ID = ? ORDER BY RAND()"
            );
            getAnswerQuery.setInt(1, question.getQuestionId());

            ResultSet resultSet = getAnswerQuery.executeQuery();
            while (resultSet.next()){
                int answerId = resultSet.getInt("idanswer");
                String answerText = resultSet.getString("answer_text");
                boolean isCorrect = resultSet.getBoolean("is_correct");
                Answer answer = new Answer(answerId, question.getQuestionId(), answerText, isCorrect);
                answers.add(answer);
            }

            return answers;
        }catch (Exception e){
            e.printStackTrace();
        }

        // return null if it could not find the answers in the database
        return null;
    }
    // true- successfully inserted answers
    // false - failed to insert answers
    private static boolean insertAnswers(Question question, String[] answers, int correctionIndex){
        try {
            Connection connection = DriverManager.getConnection(OB_URL,OB_USERNAME,OB_PASSWORD
            );

            PreparedStatement insertAnswerQuery = connection.prepareStatement(
                    "INSERT INTO ANSWER(QUESTION_ID, ANSWER_TEXT, IS_CORRECT) " +
                            "VALUES(?, ?, ?)"
            );
            insertAnswerQuery.setInt(1, question.getQuestionId());
            for (int i = 0; i < answers.length; i++){
                insertAnswerQuery.setString(2, answers[i]);

                if (i == correctionIndex){
                    insertAnswerQuery.setBoolean(3, true);
                }else {
                    insertAnswerQuery.setBoolean(3, false);
                }

                insertAnswerQuery.executeUpdate();
            }
              return true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
        }
    }

