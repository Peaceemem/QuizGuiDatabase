package screens;
import constants.CommonConstants;
import database.Category;
import database.JDBC;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class TitleScreenGui extends JFrame {
    private  JComboBox categoriesMenu;
    private JTextField numOfQuestionTextField;
    public TitleScreenGui() {
        // call the constructor of the superclass with the title of "Title Screen"
        super("Title Screen");

        // set the size of the JFrame to 400 pixels wide and 565 pixels wide
        setSize(400, 565);

        // set the layout manager of the frame to null, allowing manual positioning of the components
        setLayout(null);

        // set the frame to be centered on the screen when displayed
        setLocationRelativeTo(null);

        //  disable resizing of the frame by the user
        setResizable(false);

        // set default close operation of the frame to exist after the application as been closed
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // change the background color
        getContentPane().setBackground(CommonConstants.DARK_PURPLE);
        //ImageIcon imageIcon = new ImageIcon("src/images.jpeg");

//        // Create a JLabel with the image
//        JLabel imageLabel = new JLabel(imageIcon);
//        // Set the size and position of the label
//        imageLabel.setBounds(100, 90, imageIcon.getIconWidth(), imageIcon.getIconHeight());
//        // Add the label to the content pane
//        getContentPane().add(imageLabel);

        addGuiComponents();
    }

    private void addGuiComponents() {
        // Title label
        JLabel titleLabel = new JLabel("Quiz Game!");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 36));
        titleLabel.setBounds(0,20,390,43);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(titleLabel);

        // choose category label
        JLabel chooseCategoryLabel = new JLabel("Choose a Category");
        chooseCategoryLabel.setFont(new Font("Arial", Font.BOLD, 16));
        chooseCategoryLabel.setBounds(0,90,400,43);
        chooseCategoryLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chooseCategoryLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(chooseCategoryLabel);

        // category drop down menu
       ArrayList<String> categoryList = JDBC.getCategories();
       categoriesMenu =  new JComboBox(categoryList.toArray());
        categoriesMenu.setBounds(20,120,337,45);
        categoriesMenu.setForeground(CommonConstants.DARK_BLUE);
        add(categoriesMenu);

        // num of Question label
        JLabel numOfQuestionLabel = new JLabel("Number of Question: ");
        numOfQuestionLabel.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionLabel.setBounds(20,190,172,20);
        numOfQuestionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        numOfQuestionLabel.setForeground(CommonConstants.BRIGHT_YELLOW);
        add(numOfQuestionLabel);

        // num of Question text input field
        numOfQuestionTextField = new JTextField("10");
        numOfQuestionTextField.setFont(new Font("Arial", Font.BOLD, 16));
        numOfQuestionTextField.setBounds(200,190,148,26);
        numOfQuestionTextField.setForeground(CommonConstants.DARK_BLUE);
        add(numOfQuestionTextField);

         //start button
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Arial", Font.BOLD, 16));
        startButton.setBounds(65,290,262,45);
        startButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        startButton.setForeground(CommonConstants.DARK_PURPLE);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (validateInput()){
                    // retrieve category
                    Category category = JDBC.getCategory(categoriesMenu.getSelectedItem().toString());

                    // invalid category
                    if (category == null)
                        return;

                    int numOfQuestions = Integer.parseInt(numOfQuestionTextField.getText());

                    // load quiz screen
                    QuizScreenGui quizScreenGui = new QuizScreenGui(category, numOfQuestions);
                    quizScreenGui.setLocationRelativeTo(TitleScreenGui.this);

                    // dispose of the screen
                    TitleScreenGui.this.dispose();

                    // display quiz screen
                    quizScreenGui.setVisible(true);
                }
            }
        });
        add(startButton);

        // exit button
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("Arial", Font.BOLD, 16));
        exitButton.setBounds(65,350,262,45);
        exitButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        exitButton.setForeground(CommonConstants.DARK_PURPLE);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // dispose of the screen
                TitleScreenGui.this.dispose();
            }
        });
        add(exitButton);

        // create a question button
        JButton createQuestionButton = new JButton("Create a Question");
        createQuestionButton.setFont(new Font("Arial", Font.BOLD, 16));
        createQuestionButton.setBounds(65,420,262,45);
        createQuestionButton.setBackground(CommonConstants.BRIGHT_YELLOW);
        createQuestionButton.setForeground(CommonConstants.DARK_PURPLE);
        createQuestionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // create question screen gui
                CreateQuestionScreenGui createQuestionScreenGui = new CreateQuestionScreenGui();
                createQuestionScreenGui.setLocationRelativeTo(TitleScreenGui.this);

                // DISPOSE of this title screen
                TitleScreenGui.this.dispose();

                // display create a question screen gui
                createQuestionScreenGui.setVisible(true);
            }
        });
        add(createQuestionButton);
    }
    // true - valid input
    // false - invalid input
    private boolean validateInput(){
        // num of question field must not be empty
        if (numOfQuestionTextField.getText().replaceAll(" ", " ").length() <= 0)
            return false;

        // no category is chosen
        if (categoriesMenu.getSelectedItem() == null)
            return false;
        return true;
    }

}
