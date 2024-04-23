import database.Category;
import screens.CreateQuestionScreenGui;
import screens.QuizScreenGui;
import screens.TitleScreenGui;

import javax.swing.*;

public class App {


    public static void main(String[] args) {



        // ensures swing GUI tasks are executed on the event dispatch thread (EDT)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
               // create and display the title screen gui windows
               new TitleScreenGui().setVisible(true);
               //new CreateQuestionScreenGui().setVisible(true);

                //new QuizScreenGui(new Category(1, "Java"), 10).setVisible(true);
            }
        });
    }
}
