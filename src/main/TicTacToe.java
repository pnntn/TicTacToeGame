// @author: pnntn

package main;

import java.awt.*;
import java.awt.event.*;
import java.io.Serializable;
import java.util.Random;

import javax.swing.*;

public class TicTacToe extends JFrame implements ActionListener, Serializable {
	private static final long serialVersionUID = 1L;
	private JButton[][] buttons;
	private char currentPlayer;
	private JLabel statusLabel;
	private boolean gameEnded;

	public TicTacToe() {
		setTitle("Tic Tac Toe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300, 300);
		setResizable(false);

		JPanel gamePanel = new JPanel();
		gamePanel.setLayout(new GridLayout(3, 3));
		add(gamePanel);

		buttons = new JButton[3][3];
		currentPlayer = 'X';
		gameEnded = false;

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j] = new JButton("");
				buttons[i][j].setFont(new Font("Arial", Font.PLAIN, 40));
				buttons[i][j].addActionListener(this);
				gamePanel.add(buttons[i][j]);
			}
		}

		statusLabel = new JLabel("");
		add(statusLabel, BorderLayout.SOUTH);

		setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		JButton clickedButton = (JButton) e.getSource();

		if (gameEnded || !clickedButton.getText().equals("")) {
			return; // If the game is ended or button is already clicked, do nothing
		}

		clickedButton.setText(Character.toString(currentPlayer));

		if (checkForWin()) {
			endGame("Player " + currentPlayer + " wins!");
		} else if (checkForDraw()) {
			endGame("It's a draw!");
		} else {
			currentPlayer = 'O'; // CPU's turn

			if (!cpuMove()) {
				endGame("It's a draw!");
				return;
			}

			if (checkForWin()) {
				endGame("Player " + currentPlayer + " wins!");
			} else if (checkForDraw()) {
				endGame("It's a draw!");
			} else {
				currentPlayer = 'X'; // Player's turn again
			}
		}
	}

	private void endGame(String message) {
		JOptionPane.showMessageDialog(this, message);
		resetBoard();
	}

	private boolean cpuMove() {
		// Look for a winning move
		if (findWinningMove()) {
			return true;
		}
		// Look for a move to block the player
		if (blockPlayer()) {
			return true;
		}
		// Otherwise, make a random move
		Random rand = new Random();
		int emptyCells = 0;
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].getText().equals("")) {
					emptyCells++;
				}
			}
		}
		if (emptyCells == 0) {
			return false; // No more empty cells, game is draw
		}
		while (true) {
			int row = rand.nextInt(3);
			int col = rand.nextInt(3);
			if (buttons[row][col].getText().equals("")) {
				buttons[row][col].setText(Character.toString(currentPlayer));
				return true;
			}
		}
	}

	private boolean findWinningMove() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].getText().equals("")) {
					buttons[i][j].setText(Character.toString(currentPlayer));
					if (checkForWin()) {
						return true;
					} else {
						buttons[i][j].setText("");
					}
				}
			}
		}
		return false;
	}

	private boolean blockPlayer() {
		char opponent = (currentPlayer == 'X') ? 'O' : 'X';
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].getText().equals("")) {
					buttons[i][j].setText(Character.toString(opponent));
					if (checkForWin()) {
						buttons[i][j].setText(Character.toString(currentPlayer));
						return true;
					} else {
						buttons[i][j].setText("");
					}
				}
			}
		}
		return false;
	}

	private boolean checkForWin() {
		for (int i = 0; i < 3; i++) {
			if (buttons[i][0].getText().equals(buttons[i][1].getText()) &&
					buttons[i][0].getText().equals(buttons[i][2].getText()) &&
					!buttons[i][0].getText().equals("")) {
				return true; // Row win
			}
			if (buttons[0][i].getText().equals(buttons[1][i].getText()) &&
					buttons[0][i].getText().equals(buttons[2][i].getText()) &&
					!buttons[0][i].getText().equals("")) {
				return true; // Column win
			}
		}
		if (buttons[0][0].getText().equals(buttons[1][1].getText()) &&
				buttons[0][0].getText().equals(buttons[2][2].getText()) &&
				!buttons[0][0].getText().equals("")) {
			return true; // Diagonal win (top-left to bottom-right)
		}
		if (buttons[0][2].getText().equals(buttons[1][1].getText()) &&
				buttons[0][2].getText().equals(buttons[2][0].getText()) &&
				!buttons[0][2].getText().equals("")) {
			return true; // Diagonal win (top-right to bottom-left)
		}
		return false;
	}

	private boolean checkForDraw() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				if (buttons[i][j].getText().equals("")) {
					return false; // If any button is empty, game is not draw yet
				}
			}
		}
		return true; // All buttons are filled, game is draw
	}

	private void resetBoard() {
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				buttons[i][j].setText("");
			}
		}
		currentPlayer = 'X';
		gameEnded = false; // Reset gameEnded flag
	}

	public static void main(String[] args) {
		new TicTacToe();
	}
}