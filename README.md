# Tic Tac Toe (Java Swing GUI)

A simple, interactive Tic Tac Toe game built with Java Swing. This project features a graphical user interface, score tracking, and a play-again option for endless fun!

## Features

- **Intuitive GUI:** Play using clickable buttons in a 3x3 grid.
- **Two-player mode:** Alternate turns between Player 1 (X) and Player 2 (O).
- **Play with Computer mode:** Alternate turns between Player 1 (X) and AI (O).
- **Score tracking:** Keeps track of each player’s wins.
- **Win highlighting:** Highlights the winning combination.
- **Draw detection:** Detects and announces draws.
- **Play again:** Option to restart the game or exit after each round.


## How to Run

### Prerequisites

- Java JDK 8 or higher installed on your system.

### Steps

1. **Clone the repository:**
    ```
    git clone git@github.com:joelbijo/Tic-Tac-Toe.git
    cd Tic-Tac-Toe
    ```

2. **Compile the code:**
    ```
    javac Tics.java
    ```

3. **Run the game:**
    ```
    java Tics
    ```

## Code Overview

- **Tics.java:** Main class containing all logic and GUI code.
    - Uses `JFrame`, `JButton`, `JLabel`, and `JPanel` for the interface.
    - Handles player turns, win/draw logic, and score updates.
    - Provides dialog boxes for invalid moves and game-over scenarios.

## Customization

- Change colors, fonts, or add new features (like AI) by editing `Tics.java`.
- To ignore compiled `.class` files in your repository, ensure your `.gitignore` contains:
    ```
    *.class
    ```

## Contributing

Pull requests are welcome! For major changes, please open an issue first to discuss what you would like to change.
