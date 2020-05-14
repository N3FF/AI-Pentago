import java.util.ArrayList;
import java.util.List;

public class Node implements Comparable<Node>{

	GameBoard board = null;
	char aiToken;
	Node parent = null;
	List<Node> children = new ArrayList<>();
	boolean isMax;
	int score = 0;
	int alpha = 9;
	int beta = 9;
	boolean startPrune = false;
	Node winner = this;

	Node(String board, char aiToken, boolean max) {
		this.isMax = max;
		this.board = new GameBoard(board);
		this.aiToken = aiToken;
		getWinner();
	}

	Node(String board, char aiToken, boolean max, Node parent) {
		this.isMax = max;
		this.board = new GameBoard(board);
		this.aiToken = aiToken;
		this.parent = parent;
		getWinner();
	}

	
	public void setMove(int block, int pos, char token, int rBlock, int rDirection) {
		board.insertToken(block, pos, token);
		if(rDirection == 'L') {
			board.rotateLeft(rBlock);
		} else {
			board.rotateRight(rBlock);
		}
	}

	public void getWinner() {
		if (board.isWin()) {
			if (board.getWinners().size() < 2) {
				score += board.getWinners().get(0).equals(aiToken)? 1 : -1;
			}
		}
	}

	public void tryPrune() {
		if((isMax && score == 1) || (isMax && score == -1)) {
			startPrune = true;
		}
	}
	@Override
	public int compareTo(Node o) {
		return this.score - o.score;
	}
	
}
