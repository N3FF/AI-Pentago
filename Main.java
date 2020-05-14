import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

//Justin Neff
//TCSS 435A
public class Main {

	static String aiName = "SMART AI";
	static char aiColor = 'b';
	static String playerName = "RANDOM AI";
	static char playerColor = 'w';
	static boolean playerTurn = true;
	static boolean playerStart = false;

	static final boolean SMART_AI_VS_RANDOM_AI = true;
	static final boolean EMPTY_BOARD = true;
	static final boolean PRINT_TO_FILE = false;

	public static void main(String[] args) {
		startFile();
		AI ai = new AI();
		AI aiRandom;
		if (SMART_AI_VS_RANDOM_AI) {
			aiRandom = new AI(playerColor, aiColor);
		}
		playerTurn = (new java.util.Random().nextInt(2)) == 0 ? true : false;
		playerStart = playerTurn;
		int index = 0;
		String input[];
		if (!SMART_AI_VS_RANDOM_AI) {
			input = consoleInput();
			playerName = input[index++];
			playerColor = input[index++].equals("b") ? 'b' : 'w';
			aiName = input[index++];
		}
		aiColor = playerColor == 'b' ? 'w' : 'b';
		ai.setToken(aiColor, playerColor);

		String board = (SMART_AI_VS_RANDOM_AI) ? testBoard() : input[index];
		System.out.println((playerTurn ? playerName : aiName) + " won the coinflip!");
		output("\n"+(playerTurn ? playerName : aiName) + " won the coinflip!\n\n");

		GameBoard gb = new GameBoard(board);
		System.out.println(gb);
		output(gb + "\n");
		while (!gb.isWin()) {
			if (playerTurn) {
				if (SMART_AI_VS_RANDOM_AI) {
					aiRandom.giveBoard(gb);
					aiRandom.randomMove();
					System.out.println(gb);
					output(gb + "\n");
					playerTurn = !playerTurn;
				} else {
					System.out.println("[" + playerColor + "] " + playerName
							+ "'s turn. '(block#)/(index#) (block#)(Direction)' ie '4/1 4L' is input at block 4, index 1 and then rotate block 4 left");
					System.out.print("Make your move: ");
					output("[" + playerColor + "] " + playerName
							+ "'s turn. '(block#)/(index#) (block#)(Direction)' ie '4/1 4L' is input at block 4, index 1 and then rotate block 4 left\nMake your move: ");
					String play[] = inputTurn();
					String token[] = play[0].split("/");
					char[] rotate = play[1].toUpperCase().toCharArray();
					int block = Integer.parseInt(token[0]);
					int pos = Integer.parseInt(token[1]);

					if (makeMove(gb, block, pos, (int) rotate[0] - 48, rotate[1])) {
						playerTurn = !playerTurn;
					} else {
						System.out.println("Position " + play[0] + " is already taken, try again.");
					}
				}
			} else {
				ai.giveBoard(gb);
				ai.aiMove();
				if (!SMART_AI_VS_RANDOM_AI) {
					System.out.println(gb);
					output(gb + "\n");
				}
				ai.rotateBlock();
				System.out.println(gb);
				output(gb + "\n");
				playerTurn = !playerTurn;
			}
			System.out.println((!playerTurn ? playerName : aiName) + " [" + (!playerTurn ? playerColor : aiColor)
					+ "]: " + gb.getLastMove() + "\n");
			output((!playerTurn ? playerName : aiName) + " [" + (!playerTurn ? playerColor : aiColor) + "]: "
					+ gb.getLastMove() + "\n\n");
		}
		if (gb.getWinners().size() > 1 || gb.getAvailableMoves().isEmpty()) {
			System.out.println("It's a TIE!");
			output("It's a TIE!\n");
		} else {
			String winner = gb.getWinners().get(0).equals(playerColor) ? playerName : aiName;
			System.out.println("Winner: " + winner);
			output("Winner: " + winner + "\n");
		}
	}

	private static boolean makeMove(GameBoard gb, int blockInput, int index, int blockRotate, char direction) {
		if (gb.insertToken(blockInput, index, playerTurn ? playerColor : aiColor)) {
			System.out.println(gb);
			output(gb + "\n");
			if (direction == 'L')
				gb.rotateLeft(blockRotate);
			else if (direction == 'R')
				gb.rotateRight(blockRotate);
			System.out.println(gb);
			output(gb + "\n");
			return true;
		}
		return false;
	}

	private static String[] inputTurn() {
		String read = null;
		String input[] = null;
		int block = -1;
		int index = -1;
		int rBlock = -1;
		char rDir = ' ';
		boolean valid = false;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			while (!valid) {
				read = in.readLine();
				input = read.trim().split(" ");
				if (input.length == 2) {
					if (input[0].length() == 3) {
						if (isNum(input[0].charAt(0))) {
							block = Integer.parseInt(input[0].charAt(0) + "");
						}
						if (isNum(input[0].charAt(2))) {
							index = Integer.parseInt(input[0].charAt(2) + "");
						}
					}
					if (input[1].length() == 2) {
						if (isNum(input[1].charAt(0))) {
							rBlock = Integer.parseInt(input[1].charAt(0) + "");
						}
						rDir = input[1].toUpperCase().charAt(1);
					}

					valid = input[0].toCharArray()[1] == '/' && block >= 0 && block < 4 && index >= 0 && index < 9
							&& rBlock >= 0 && rBlock < 4 && (rDir == 'R' || rDir == 'L');
				}
				if (!valid) {
					System.out.print("Invalid input.\nMake your move:");
				}
			}
			output(read + "\n");
		} catch (Exception e) {

		}
		return input;
	}

	private static boolean isNum(char c) {
		return (int) c > 47 && (int) c < 58;
	}

	private static String[] consoleInput() {
		int counter = 0;
		String input[] = new String[11];
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		try {
			System.out.print("Player Name: ");
			output("Player Name: ");
			input[counter++] = in.readLine();
			output(input[counter - 1] + "\n");

			System.out.print("Player Token Color: ");
			output("Player Token Color: ");
			input[counter++] = in.readLine();
			output(input[counter - 1] + "\n");

			System.out.print("AI Name: ");
			output("AI Name: ");
			input[counter++] = in.readLine();
			output(input[counter - 1] + "\n");
			String board = "";
			for (int i = 0; i < 6; i++) {
				System.out.print("Input 6 characters for row: " + i + " and press [enter]\n");

				String temp = in.readLine();
				if (temp.length() == 6) {
					output("Input 6 characters for row: " + i + " and press [enter]\n");
					board += temp;
					output(temp + "\n");
				} else {
					System.out.print("Incorrect Number of characters.\n");
					i--;
				}
			}
			input[counter++] = board;

			return input;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static String testBoard() {
		String board = "";
		if (!EMPTY_BOARD) {
			board += "wbbwww";
			board += "wb....";
			board += "..w.b.";
			board += "b.....";
			board += "b.....";
			board += "......";
		} else {
			board += "......";
			board += "......";
			board += "......";
			board += "......";
			board += "......";
			board += "......";
		}
		return board;
	}

	private static void output(String s) {
		if (PRINT_TO_FILE) {
			try {
				BufferedWriter w = new BufferedWriter(new FileWriter("Output.txt", true));
				w.append(s);
				w.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private static void startFile() {
		if (PRINT_TO_FILE) {
			try {
				FileWriter f = new FileWriter("Output.txt", false);
				PrintWriter w = new PrintWriter(f, false);
				w.flush();
				w.close();
				f.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
