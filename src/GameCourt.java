import java.awt.*;
import java.util.List;
import java.util.*;
import javax.swing.*;


@SuppressWarnings("serial")
public class GameCourt extends JPanel {
	public static final int COURT_WIDTH = 400;
	public static final int COURT_HEIGHT = 400;
	public abstract class Mode  {
		public abstract void action(int xCoord, int yCoord);
		public abstract Color checkWhoseTurn();
	}
	private Mode mode = new SelectMode(Color.RED);
	private List<GameObj[][]> moves = new LinkedList<GameObj[][]>();
	private List<Integer> rScores = new LinkedList<Integer>();
	private List<Integer> bScores = new LinkedList<Integer>();
	private int redUndosRemaining = 3;
	private int blueUndosRemaining = 3;

	public GameCourt(JLabel status) {
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		constructInitialPiecesArray();
		repaint();
	}

	//Copy over array elements (to avoid aliasing in moves linked list)
	private static GameObj[][] copyArray(GameObj[][] arrayToCopy) {
		GameObj[][] finalCopy = new GameObj[arrayToCopy.length][arrayToCopy[0].length];
		for (int i = 0; i <arrayToCopy.length; i++) {
			for (int j = 0; j < arrayToCopy.length; j++) {				
				if (arrayToCopy[i][j] instanceof RegularPiece) {
					if (arrayToCopy[i][j].getColor().equals(Color.RED)) {
						finalCopy[i][j] = new RegularPiece(i, j, Color.RED);						
					} else {
						finalCopy[i][j] = new RegularPiece(i, j, Color.BLUE);
					}
				} else if (arrayToCopy[i][j] instanceof KingPiece) {
					if (arrayToCopy[i][j].getColor().equals(Color.RED)) {
						finalCopy[i][j] = new KingPiece(i, j, Color.RED);
					} else {
						finalCopy[i][j] = new KingPiece(i, j, Color.BLUE);
					}
				} else {
					finalCopy[i][j] = new EmptyPiece(0, 0, Color.WHITE);
				}
			} 
		} 
		return finalCopy;
	}


	//Mode for when click on piece to be moved
	class SelectMode extends Mode {

		Color c;
		SelectMode(Color color) {
			c = color;
		}

		public Color checkWhoseTurn() {
			return c;
		}

		public void action(int xIndexInit, int yIndexInit) {
			GameObj boardSetup[][] = copyArray(((LinkedList<GameObj[][]>) moves).peekLast());
			if (boardSetup[xIndexInit][yIndexInit].getColor().equals(c)) {
				GameObj selectedPiece = boardSetup[xIndexInit][yIndexInit];
				mode = new MoveMode(c, selectedPiece, xIndexInit, yIndexInit);
			}
			repaint();
		}
	}

	//Mode for when click on place to move piece to
	class MoveMode extends Mode {
		Color c;
		Color nextColor;
		int xIndexInit;
		int yIndexInit;
		GameObj selectedPiece;

		MoveMode(Color color, GameObj selected, int x, int y) {
			c = color;
			xIndexInit = x;
			yIndexInit = y;
			selectedPiece = selected;

			if (c.equals(Color.RED)) {
				nextColor = Color.BLUE;
			} else {
				nextColor = Color.RED;
			}
		}

		public Color checkWhoseTurn() {
			return c;
		}

		public int getSelectedX() {
			return selectedPiece.getX();
		}

		public int getSelectedY() {
			return selectedPiece.getY();
		}

		public void action(int xIndexFinal, int yIndexFinal) {

			//Deselect piece by double clicking on it
			if (xIndexFinal == selectedPiece.getX() && yIndexFinal == selectedPiece.getY()) {
				mode = new SelectMode(selectedPiece.getColor());
			}

			GameObj boardSetup[][] = copyArray(((LinkedList<GameObj[][]>) moves).peekLast());
			if (boardSetup[xIndexFinal][yIndexFinal] instanceof EmptyPiece) {
				if (boardSetup[xIndexInit][yIndexInit].isValidMove(xIndexInit, yIndexInit, xIndexFinal, yIndexFinal)) {
					registerMove(selectedPiece, boardSetup, xIndexInit, yIndexInit, xIndexFinal, yIndexFinal, nextColor);
				} 



				int xCaptured = getNumberBetweenTwo(xIndexInit, xIndexFinal);
				int yCaptured = getNumberBetweenTwo(yIndexInit, yIndexFinal);
				if (boardSetup[xIndexFinal][yIndexFinal] instanceof EmptyPiece &&  
						boardSetup[xIndexInit][yIndexInit].isValidCapture(xIndexInit, yIndexInit, xIndexFinal, yIndexFinal) &&
						boardSetup[xCaptured][yCaptured].getColor().equals(nextColor) ) {
					boardSetup[xCaptured][yCaptured] = new EmptyPiece(0,0, Color.WHITE);												
					registerMove(selectedPiece, boardSetup, xIndexInit, yIndexInit, xIndexFinal, yIndexFinal, nextColor);
				}

			}
			repaint();
		}
	}

	public Mode getCurrentMode() {
		return mode;
	}

	//Register that a move occurred after a move or capture
	private void registerMove(GameObj selectedPiece, GameObj[][] boardSetup, int xIndexInit, int yIndexInit, 
			int xIndexFinal, int yIndexFinal, Color nextColor) {
		selectedPiece.move(xIndexFinal, yIndexFinal);
		boardSetup[xIndexFinal][yIndexFinal] = selectedPiece;
		boardSetup[xIndexInit][yIndexInit] = new EmptyPiece(0,0, Color.WHITE);
		int [] scores = checkAfterTurnScoreAndKing(boardSetup);
		moves.add(boardSetup);
		rScores.add(scores[0]);
		bScores.add(scores[1]);
		mode = new SelectMode(nextColor);

	}

	//Find indices of piece to capture
	private int getNumberBetweenTwo(int lo, int hi) {		
		if (hi > lo) {
			return lo + 1;
		} else {
			return lo -1;
		}
	}

	//Update scores and determine if any pieces should be kinged 
	private int[] checkAfterTurnScoreAndKing (GameObj [][] boardSetup) {
		int redScore = 0;
		int blueScore = 0;
		for (int i = 0; i < boardSetup.length; i++) {
			for (int j = 0; j < boardSetup[0].length; j++) {
				if(boardSetup[i][j].getColor().equals(Color.RED)) {
					if (j == 0) {
						boardSetup[i][j]= new KingPiece(i, j, Color.RED);
					}

					if (boardSetup[i][j] instanceof RegularPiece) {
						redScore = redScore + 1;
					} else if (boardSetup[i][j] instanceof KingPiece) {
						redScore = redScore + 3;
					}

				} else if(boardSetup[i][j].getColor().equals(Color.BLUE)) {

					if (j == 7) {
						boardSetup[i][j]= new KingPiece(i, j, Color.BLUE);
					}

					if (boardSetup[i][j] instanceof RegularPiece) {
						blueScore = blueScore + 1;
					} else if (boardSetup[i][j] instanceof KingPiece) {
						blueScore = blueScore + 3;
					}

				}
			} 
		}
		int [] scores = {redScore, blueScore};
		return scores ;
	}

	//Undo both players' moves
	public void undo() {
		if (mode instanceof SelectMode) {
			if (mode.checkWhoseTurn().equals(Color.RED)) {
				if (redUndosRemaining > 0) {
					if (moves.size() > 2) {
						undoActions();
						redUndosRemaining--;
						repaint();
					}
				}
			} else if (mode.checkWhoseTurn().equals(Color.BLUE)) {
				if (blueUndosRemaining > 0) {
					if (moves.size() > 2) {
						undoActions();
						blueUndosRemaining--;
						repaint();
					}
				}
			}
		}
	}

	//Undo helper
	private void undoActions() {
		((LinkedList<GameObj[][]>) moves).removeLast();
		((LinkedList<GameObj[][]>) moves).removeLast();
		((LinkedList<Integer>) rScores).removeLast();
		((LinkedList<Integer>) rScores).removeLast();
		((LinkedList<Integer>) bScores).removeLast();
		((LinkedList<Integer>) bScores).removeLast();
	}

	//Return how many undos each player has left
	public int getUndos(Color c) {
		if (c.equals(Color.RED)) {
			return redUndosRemaining;
		} return blueUndosRemaining;
	}

	//Skip a player's turn
	public void skipTurn() {
		if (mode.checkWhoseTurn().equals(Color.RED)) {
			mode = new SelectMode(Color.BLUE);
		} else {
			mode = new SelectMode(Color.RED);
		}
	}

	//Return score
	public int getScore(Color c) {
		if (c.equals(Color.RED)) {
			return ((LinkedList<Integer>) rScores).peekLast();
		}
		return ((LinkedList<Integer>) bScores).peekLast();
	}

	//Return copy of moves
	public LinkedList<GameObj[][]> getMoves() {
		LinkedList<GameObj[][]> copyOfMoves = new LinkedList<GameObj[][]>();
		copyOfMoves.addAll(moves);
		return copyOfMoves;
	}

	//initial board setup
	public void constructInitialPiecesArray() {
		GameObj[][] piecesArray = new GameObj[8][8]; 
		for (int i = 0; i < piecesArray.length; i++) {
			for (int j = 0; j < piecesArray[0].length; j++) {
				if (j <= 2) {
					if (j % 2 == 0) {
						if (i % 2 == 0 ) {
							piecesArray[i][j] = new RegularPiece(i, j, Color.BLUE);;
						} else {
							piecesArray[i][j] = new EmptyPiece(0, 0, Color.WHITE);
						}
					} else {
						if (i % 2 == 0 ) {
							piecesArray[i][j] = new EmptyPiece(0, 0, Color.WHITE);
						} else {
							piecesArray[i][j] = new RegularPiece(i, j, Color.BLUE);
						}
					}
				} else if (j >= 5) {
					if (j % 2 == 0) {
						if (i % 2 == 0 ) {
							piecesArray[i][j] = new RegularPiece(i, j, Color.RED);
						} else {
							piecesArray[i][j] = new EmptyPiece(0, 0, Color.WHITE);
						}
					} else {
						if (i % 2 == 0 ) {
							piecesArray[i][j] = new EmptyPiece(0, 0, Color.WHITE);
						} else {
							piecesArray[i][j] = new RegularPiece(i, j, Color.RED);
						}
					}
				} else {
					piecesArray[i][j] = new EmptyPiece(0, 0, Color.WHITE);
				}
			}
		} 
		LinkedList<GameObj[][]> intialMove = new LinkedList<GameObj[][]>();
		intialMove.add(Arrays.copyOf(piecesArray, piecesArray.length));
		moves = intialMove;
		rScores.add(12);
		bScores.add(12);

	}

	//Get preferred size
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(COURT_WIDTH, COURT_HEIGHT);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		//Draw the board
		Color squareColor = Color.BLACK;
		for (int row = 0; row < 400; row = row + 50) {
			for (int col = 0; col < 400; col = col + 50) {
				if ((row + col) % 100 == 0) {
					squareColor = Color.WHITE;
				} else {
					squareColor = Color.BLACK;
				}
				g.setColor(squareColor);
				g.fillRect(row, col, 50, 50);
			}
		} 

		//Draw all the pieces
		for (int i = 0; i < ((LinkedList<GameObj[][]>) moves).peekLast().length; i++) {
			for (int j = 0; j < ((LinkedList<GameObj[][]>) moves).peekLast()[0].length; j++) {
				((LinkedList<GameObj[][]>) moves).peekLast()[i][j].draw(g);
			} 
		}

		//Draw selected piece
		if (mode instanceof MoveMode) {
			g.setColor(Color.YELLOW);
			g.drawOval(((MoveMode) mode).getSelectedX()*50, ((MoveMode) mode).getSelectedY()*50, 50, 50);
		}
	} 

}


