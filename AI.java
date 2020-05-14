import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Stack;

//Justin Neff
//TCSS 435
//Didn't get AI finished so used random to test
public class AI {

	private static final int PRE_CALC_MOVES = 4;
	private static final boolean PRINT_NODES = false;
	private Random r = new Random();
	private GameBoard gb;
	private char aiToken;
	private char playerToken;
	public String lastMove;
	public boolean isMax = true;
	private int play[];

	AI() {

	}

	AI(char t, char p) {
		aiToken = t;
		playerToken = p;
	}

	public void giveBoard(GameBoard gb) {
		this.gb = gb;
	}

	public void insertToken(int board, int pos) {
		gb.insertToken(board, pos, aiToken);
	}

	public void setToken(char ai, char pl) {
		this.aiToken = ai;
		this.playerToken = pl;
	}

	public void randomMove() {
		while (!gb.insertToken(r.nextInt(4), r.nextInt(9), aiToken))
			;
		if (r.nextInt(2) == 0)
			gb.rotateLeft(r.nextInt(4));
		else
			gb.rotateRight(r.nextInt(4));
	}

	public void rotateBlock() {
		if (play[3] == 'R')
			gb.rotateRight(play[2]);
		else
			gb.rotateLeft(play[2]);
	}

	public void aiMove() {
		play = buildMiniMax(PRE_CALC_MOVES);
		insertToken(play[0], play[1]);
	}

	private int[] buildMiniMax(int maxHeight) {
		// root node ie current state
		Node parentNode = new Node(gb.getBoard(), aiToken, true);
		List<Node> children;
		List<int[]> moves;
		Stack<Node> stack = new Stack<>();
		stack.push(parentNode);
		while (!stack.isEmpty()) {
			moves = stack.peek().board.getAvailableMoves();
			children = stack.peek().children;
			if (moves.size() == children.size() || stack.size() == maxHeight || parentNode.startPrune) {
				parentNode = stack.pop();
				isMax = !isMax;
				if (parentNode.children.size() > 0) {
					parentNode.winner = parentNode.isMax ? Collections.max(parentNode.children)
							: Collections.min(parentNode.children);
					parentNode.score = parentNode.winner.score;
				}
			} else {
				char token = (isMax ? aiToken : playerToken);
				int block = moves.get(children.size())[0];
				int pos = moves.get(children.size())[1];
				int rblock = moves.get(children.size())[2];
				int rDirection = moves.get(children.size())[3];
				Node child = new Node(parentNode.board.getBoard(), token, isMax, parentNode);
				child.setMove(block, pos, token, rblock, rDirection);
				child.getWinner();
				child.tryPrune();
				children.add(child);
				parentNode = child;
				if (stack.size() != maxHeight) {
					stack.push(parentNode);
					isMax = !isMax;
				}
			}
		}
		if (PRINT_NODES) {
			System.out.println("Level 1: " + parentNode.children.size());
			int tot= 0;
			for (int i = 0; i < parentNode.children.size(); i++) {
				tot += parentNode.children.get(i).children.size();
			}
			System.out.println("Level 2: " + tot);
		}
		return parentNode.winner.board.lastMove;
	}
}
