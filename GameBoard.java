
import java.util.ArrayList;
import java.util.List;

//Justin Neff
//TCSS 435A
public class GameBoard {

	private char board[][] = new char[4][9];
	private List<Character> winners = new ArrayList<>();
	public int lastMove[] = { 0, 0, 0, 0 };

	public GameBoard(String stringBoard) {
		char tempBoard[] = stringBoard.toCharArray();
		for (int i = 0; i < 36; i++) {
			int block = (i / 3) % 2 + (i / 18) * 2;
			int token = (i % 3) + (i / 6) * 3 - (i / 18) * 9;
			board[block][token] = tempBoard[i];
		}
	}

	public String getBoard() {
		String newBoard = "";
		for (int i = 0; i < 36; i++) {
			int block = (i / 3) % 2 + (i / 18) * 2;
			int token = (i % 3) + (i / 6) * 3 - (i / 18) * 9;
			newBoard += board[block][token];
		}
		return newBoard;
	}

	public List<int[]> getAvailableMoves() {
		List<int[]> moves = new ArrayList<>();
		for (int i = 0; i < 36; i++) {
			int block = (i / 3) % 2 + (i / 18) * 2;
			int token = (i % 3) + (i / 6) * 3 - (i / 18) * 9;
			if (board[block][token] == '.') {
				for (int j = 0; j < 4; j++) {
					moves.add(new int[] { block, token, j, 'L' });
					moves.add(new int[] { block, token, j, 'R' });
				}
			}
		}
		return moves;
	}

	public void rotateRight(int block) {
		lastMove[2] = block;
		lastMove[3] = (int) 'R';
		char temp[] = board[block].clone();
		for (int i = 0; i < 9; i++) {
			board[block][3 * (i % 3) + (2 - i / 3)] = temp[i];
		}
	}

	public void rotateLeft(int block) {
		lastMove[2] = block;
		lastMove[3] = (int) 'L';
		char temp[] = board[block].clone();
		for (int i = 0; i < 9; i++) {
			board[block][3 * (2 - i % 3) + (i / 3)] = temp[i];
		}
	}

	public String getLastMove() {
		return lastMove[0] + "/" + lastMove[1] + " " + lastMove[2] + "" + (char) lastMove[3];
	}

	public boolean isBoardFull() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 9; j++) {
				if (board[i][j] == '.')
					return false;
			}
		}
		return true;
	}

	public boolean insertToken(int block, int pos, char token) {
		char current = board[block][pos];
		if (current == 'b' || current == 'w') {
			return false;
		} else {
			board[block][pos] = token;
			lastMove[0] = block;
			lastMove[1] = pos;
			return true;
		}
	}

	public boolean isWin() {
		boolean won = false;
		for (int player = 0; player < 2; player++) {
			for (int i = 0; i < 6 && !won; i++) {
				if (winRow(i)) {
					won = true;
				}
				if (winColumn(i)) {
					won = true;
				}
				if (i < 3 && winDiagR(i)) {
					won = true;
				}
				if (i < 3 && winDiagL(i)) {
					won = true;
				}
			}
		}
		return won;
	}

	private boolean winRow(int row) {
		boolean win = false;
		int block = (row / 3) * 2;
		char token = board[block][(row * 3) % 9];
		int count = 1;
		for (int i = 0; i < 5; i++) {
			int rowBlock = block + (i + 1) / 3;
			int column = (i + 1) % 3 + (row * 3) % 9;
			char nextToken = board[rowBlock][column];
			if (token == nextToken && token != '.' && !winners.contains(token)) {
				count++;
				if (count == 5) {
					winners.add(token);
					win = true;
					break;
				}
			} else {
				count = 1;
			}
			token = nextToken;
		}
		return win;
	}

	private boolean winColumn(int column) {
		boolean win = false;
		int block = column / 3;
		char token = board[block][column % 3];
		int count = 0;
		for (int i = 0; i < 6; i++) {
			int colBlock = block + (i / 3) * 2;
			int row = (i * 3 + (column % 3)) % 9;
			char nextToken = board[colBlock][row];
			if (token == nextToken && token != '.' && !winners.contains(token)) {
				count++;
				if (count == 5) {
					winners.add(token);
					win = true;
					break;
				}
			} else {
				count = 0;
			}
			token = nextToken;
		}
		return win;
	}

	private boolean winDiagR(int j) {
		boolean win = false;
		char token = board[0][j / 2 + j];
		int count = 0;
		for (int i = 0; (j == 0 && i < 6) || i < 5; i++) {
			int block = 0;
			int index = 0;
			if (i == 2) {
				block = j;
				index = 8 - (j / 2 + j) * i;
			} else {
				block = (i / 3) * 3;
				index = (j / 2 + j) + 4 * (i % 3);
			}
			char nextToken = board[block][index];
			if (token == nextToken && token != '.' && !winners.contains(token)) {
				count++;
				if (count == 5) {
					winners.add(token);
					win = true;
					break;
				}
			} else {
				count = 1;
			}
			token = nextToken;
		}
		return win;
	}

	private boolean winDiagL(int j) {
		boolean win = false;
		char token = board[1][j / 2 + j];
		int count = 0;
		for (int i = 0; (j == 1 && i < 6) || i < 5; i++) {
			int block = 0;
			int index = 0;
			if (i == 2) {
				block = j / 2 + j;
				index = 8 - j * j * i;
			} else {
				block = i / 3 + 1;
				index = j * j + 1 + (i % 3) * 2;
			}
			char nextToken = board[block][index];
			if (token == nextToken && token != '.' && !winners.contains(token)) {
				count++;
				if (count == 5) {
					winners.add(token);
					win = true;
					break;
				}
			} else {
				count = 1;
			}
			token = nextToken;
		}
		return win;
	}

	public List<Character> getWinners() {
		return winners;
	}

	@Override
	public String toString() {
		String state = "+-------+-------+\n";
		for (int j = 0, i = 0; j < 6; j++) {
			state += "| ";
			for (; i < (j * 3 + 3); i++) {
				state += board[j / 3 + j / 3][i % 9];
				state += " ";
			}
			state += "| ";
			i -= 3;
			for (; i < (j * 3 + 3); i++) {
				state += board[j / 3 + j / 3 + 1][i % 9];
				state += " ";
			}
			state += "|\n";
			if (j % 3 == 2) {
				state += "+-------+-------+\n";
			}
		}
		return state;
	}
}
