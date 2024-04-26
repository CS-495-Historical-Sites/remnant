# Questionnaire

#### Usage

This screen allows the app to gather preferences on topics relating to
historical sites. It displays a question and then displays chips that you can
select to choose your answers. Clicking on "Next" brings you to the next
question until all questions are answered.

#### Development Modification

**Functions Overview:**
 - This section provides a detailed breakdown of the key functions used within the questionnaire activity.

#### QuestionnaireMenu

  - **Purpose**: Stores a list of questions that will be displayed to the user.
  - **Parameters**:
      -  When the questionnaire is completed, pass all answers to the callback `onQuestionnaireCompleted`
  - **Questions**:
      - Questions are stored in a list of lists. The outer lists are the questions and the inner lists are the answers. Can be modified to contain more or less questions and answers.
  - **Behavior**:
      - Stores questionnaire answers as the user clicks `Next`.
      - Once the current question index is greater than or equals the question list size, it calls `onQuestionnaireCompleted` to complete the activity.

#### QuestionnaireCard

  - **Purpose**: Used for displaying the UI for the questionnaire page
  - **Parameters**:
      -  `currentQuestionIndex` keeps track of what question the user is answering.
      -  `question` String that displays the current question
      -  `options` keeps track of what chips/answers the user is choosing.
      -  `selectedOptions` keeps track of all the chips the user is choosing.
      -  `onAnswer` stores the chips with their respective question.
      -  `onNext` Controls the button functionality.
      
  - **Behavior**:
      - Cycles through each question and displays the answers as chips.
      - When the `Next` button is clicked it displays the next question and answers.
      - When the current question index reaches the list size it changes the button to `Finish`. Clicking on `Finish` will send the user to the main activity.

##