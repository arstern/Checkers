import java.awt.Color;
import javax.swing.JLabel;
import org.junit.Test;
import static org.junit.Assert.*;

public class GameTest {


	@Test
	public void testInitialBoardSetUp() {

		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameObj[][] locations = court.getMoves().peekLast();

		assertEquals(new RegularPiece(0, 0, Color.BLUE), locations[0][0]);
		assertEquals(new RegularPiece(2, 0, Color.BLUE), locations[2][0]);
		assertEquals(new RegularPiece(4, 0, Color.BLUE), locations[4][0]);
		assertEquals(new RegularPiece(6, 0, Color.BLUE), locations[6][0]);
		assertEquals(new RegularPiece(1, 1, Color.BLUE), locations[1][1]);
		assertEquals(new RegularPiece(3, 1, Color.BLUE), locations[3][1]);
		assertEquals(new RegularPiece(5, 1, Color.BLUE), locations[5][1]);
		assertEquals(new RegularPiece(7, 1, Color.BLUE), locations[7][1]);
		assertEquals(new RegularPiece(0, 2, Color.BLUE), locations[0][2]);
		assertEquals(new RegularPiece(2, 2, Color.BLUE), locations[2][2]);
		assertEquals(new RegularPiece(4, 2, Color.BLUE), locations[4][2]);
		assertEquals(new RegularPiece(6, 2, Color.BLUE), locations[6][2]);

		assertEquals(new RegularPiece(1, 5, Color.RED), locations[1][5]);
		assertEquals(new RegularPiece(3, 5, Color.RED), locations[3][5]);
		assertEquals(new RegularPiece(5, 5, Color.RED), locations[5][5]);
		assertEquals(new RegularPiece(7, 5, Color.RED), locations[7][5]);
		assertEquals(new RegularPiece(0, 6, Color.RED), locations[0][6]);
		assertEquals(new RegularPiece(2, 6, Color.RED), locations[2][6]);
		assertEquals(new RegularPiece(4, 6, Color.RED), locations[4][6]);
		assertEquals(new RegularPiece(6, 6, Color.RED), locations[6][6]);
		assertEquals(new RegularPiece(1, 7, Color.RED), locations[1][7]);
		assertEquals(new RegularPiece(3, 7, Color.RED), locations[3][7]);
		assertEquals(new RegularPiece(5, 7, Color.RED), locations[5][7]);
		assertEquals(new RegularPiece(7, 7, Color.RED), locations[7][7]);
		assertEquals(1, court.getMoves().size()); 
	}

	@Test
	public void testGetters() {
		RegularPiece regPiece = new RegularPiece(0, 2, Color.BLUE);
		assertEquals(0, regPiece.getX());
		assertEquals(2, regPiece.getY());
		assertEquals(Color.BLUE, regPiece.getColor());

		KingPiece kingPiece = new KingPiece(4, 6, Color.RED);
		assertEquals(4, kingPiece.getX());
		assertEquals(6, kingPiece.getY());
		assertEquals(Color.RED, kingPiece.getColor());

		EmptyPiece empty = new EmptyPiece(0, 0, Color.WHITE);
		assertEquals(0, empty.getX());
		assertEquals(0, empty.getY());
		assertEquals(Color.WHITE, empty.getColor());

	}
	
	@Test
	public void testMovers() {
		RegularPiece regPieceBlue = new RegularPiece(0, 2, Color.BLUE);
		assertTrue(regPieceBlue.isValidMove(0, 2, 1, 3));
		assertTrue(regPieceBlue.isValidCapture(0, 2, 2, 4));
		assertFalse(regPieceBlue.isValidMove(0, 2, 1, 1));
		assertFalse(regPieceBlue.isValidCapture(0, 2, 2, 0));

		RegularPiece regPieceRed = new RegularPiece(0, 2, Color.RED);
		assertTrue(regPieceRed.isValidMove(0, 2, 1, 1));
		assertTrue(regPieceRed.isValidCapture(0, 2, 2, 0));
		assertFalse(regPieceRed.isValidMove(0, 2, 1, 3));
		assertFalse(regPieceRed.isValidCapture(0, 2, 2, 4));
		
		KingPiece kingPiece = new KingPiece(0, 2, Color.RED);
		assertTrue(kingPiece.isValidMove(0, 2, 1, 1));
		assertTrue(kingPiece.isValidCapture(0, 2, 2, 0));
		assertTrue(kingPiece.isValidMove(0, 2, 1, 3));
		assertTrue(kingPiece.isValidCapture(0, 2, 2, 4));
	}

	@Test
	public void testMoveOnePieceValid() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(4, 4); //moved red piece
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][5]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.SelectMode);
		assertEquals(2, court.getMoves().size());  // have this board state and initial state

	}

	@Test
	public void testDeselectOnePieceAndMoveOther() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(5, 5); //tried to move red piece
		assertEquals(new RegularPiece(5, 5, Color.RED), court.getMoves().peekLast()[5][5]);

		GameCourt.Mode mode3 = court.getCurrentMode();
		assertTrue(mode3 instanceof GameCourt.SelectMode);
		mode3.action(3, 5);
		GameCourt.Mode mode4 = court.getCurrentMode();
		assertTrue(mode4 instanceof GameCourt.MoveMode);
		mode4.action(2, 4); //tried to move red piece
		assertEquals(new RegularPiece(2, 4, Color.RED), court.getMoves().peekLast()[2][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[3][4]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.SelectMode);
		assertEquals(2, court.getMoves().size());
	}


	@Test
	public void testMoveOnePieceBlackSquareNotValid() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(4, 5); //tried to move red piece
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[4][5]);
		assertEquals(new RegularPiece(5, 5, Color.RED), court.getMoves().peekLast()[5][5]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.MoveMode);
		GameCourt.Mode mode3 = court.getCurrentMode();
		mode3.action(5, 4);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][4]);
		assertEquals(new RegularPiece(5, 5, Color.RED), court.getMoves().peekLast()[5][5]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.MoveMode);		
		assertEquals(1, court.getMoves().size());
	}

	@Test
	public void testMoveRegularPieceTwoSquaresInvalid() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(3, 3); //tried to move red piece
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[3][3]);
		assertEquals(new RegularPiece(5, 5, Color.RED), court.getMoves().peekLast()[5][5]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.MoveMode);
		assertEquals(1, court.getMoves().size());
	}

	@Test
	public void testSelectTwoRedsInARowInvalid() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(4, 4); //moved red piece
		assertEquals(Color.RED, court.getMoves().peekLast()[4][4].getColor());
		assertEquals(4, court.getMoves().peekLast()[4][4].getX());
		assertEquals(4, court.getMoves().peekLast()[4][4].getY());
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.SelectMode);

		GameCourt.Mode mode3 = court.getCurrentMode();
		assertTrue(mode3 instanceof GameCourt.SelectMode);
		mode3.action(4, 4); 
		assertTrue(court.getCurrentMode() instanceof GameCourt.SelectMode);
		assertEquals(2, court.getMoves().size());
	}

	@Test
	public void testMovePieceToSquareThereIsAPiece() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		
		court.getCurrentMode().action(5, 5); //selected red piece
		court.getCurrentMode().action(4, 4); //moved red piece

		court.getCurrentMode().action(2, 2); //selected blue piece
		court.getCurrentMode().action(3, 3); //moved blue piece
		
		court.getCurrentMode().action(4, 4); //selected red piece
		court.getCurrentMode().action(3, 3); //moved red piece
		
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);
	}

	@Test
	public void testValidCapture() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(4, 4); //moved red piece
		assertEquals(Color.RED, court.getMoves().peekLast()[4][4].getColor());
		assertEquals(4, court.getMoves().peekLast()[4][4].getX());
		assertEquals(4, court.getMoves().peekLast()[4][4].getY());
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][5]);
		assertTrue(court.getCurrentMode() instanceof GameCourt.SelectMode);

		GameCourt.Mode mode3 = court.getCurrentMode();
		assertTrue(mode3 instanceof GameCourt.SelectMode);
		mode3.action(2, 2); 
		GameCourt.Mode mode4 = court.getCurrentMode();
		assertTrue(mode4 instanceof GameCourt.MoveMode);
		mode4.action(3, 3);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[2][2]);

		GameCourt.Mode mode5 = court.getCurrentMode();
		assertTrue(mode5 instanceof GameCourt.SelectMode);
		mode5.action(4, 4); 
		GameCourt.Mode mode6 = court.getCurrentMode();
		assertTrue(mode6 instanceof GameCourt.MoveMode);
		mode6.action(2, 2); 
		assertEquals(new RegularPiece(2, 2, Color.RED), court.getMoves().peekLast()[2][2]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[3][3]);

		assertEquals(12, court.getScore(Color.RED));
		assertEquals(11, court.getScore(Color.BLUE));
		assertEquals(4, court.getMoves().size());
	}

	@Test
	public void testUndoAndNoMoreThan3() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();

		GameCourt.Mode mode1 = court.getCurrentMode();
		assertTrue(mode1 instanceof GameCourt.SelectMode);
		mode1.action(5, 5); //selected red piece
		GameCourt.Mode mode2 = court.getCurrentMode();
		assertTrue(mode2 instanceof GameCourt.MoveMode);
		mode2.action(4, 4); //moved red piece
		assertEquals(Color.RED, court.getMoves().peekLast()[4][4].getColor());
		assertEquals(4, court.getMoves().peekLast()[4][4].getX());
		assertEquals(4, court.getMoves().peekLast()[4][4].getY());
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][5]);

		GameCourt.Mode mode3 = court.getCurrentMode();
		assertTrue(mode3 instanceof GameCourt.SelectMode);
		mode3.action(2, 2); 
		GameCourt.Mode mode4 = court.getCurrentMode();
		assertTrue(mode4 instanceof GameCourt.MoveMode);
		mode4.action(3, 3);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[2][2]);

		court.undo();
		GameObj[][] currentBoard1 = court.getMoves().peekLast();
		assertEquals(new RegularPiece(5, 5, Color.RED), currentBoard1[5][5]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard1[4][4]);
		assertEquals(new RegularPiece(2, 2, Color.BLUE), currentBoard1[2][2]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard1[3][3]);
		assertEquals(2, court.getUndos(Color.RED));
		assertEquals(3, court.getUndos(Color.BLUE));
		assertEquals(1, court.getMoves().size());


		GameCourt.Mode mode5 = court.getCurrentMode();
		assertTrue(mode5 instanceof GameCourt.SelectMode);
		mode5.action(5, 5); //selected red piece
		GameCourt.Mode mode6 = court.getCurrentMode();
		assertTrue(mode6 instanceof GameCourt.MoveMode);
		mode6.action(4, 4); //moved red piece
		assertEquals(Color.RED, court.getMoves().peekLast()[4][4].getColor());
		assertEquals(4, court.getMoves().peekLast()[4][4].getX());
		assertEquals(4, court.getMoves().peekLast()[4][4].getY());
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][5]);

		GameCourt.Mode mode7 = court.getCurrentMode();
		assertTrue(mode7 instanceof GameCourt.SelectMode);
		mode7.action(2, 2); 
		GameCourt.Mode mode8 = court.getCurrentMode();
		assertTrue(mode8 instanceof GameCourt.MoveMode);
		mode8.action(3, 3);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[2][2]);

		court.undo();
		GameObj[][] currentBoard2 = court.getMoves().peekLast();
		assertEquals(new RegularPiece(5, 5, Color.RED), currentBoard2[5][5]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard2[4][4]);
		assertEquals(new RegularPiece(2, 2, Color.BLUE), currentBoard2[2][2]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard2[3][3]);
		assertEquals(1, court.getUndos(Color.RED));
		assertEquals(3, court.getUndos(Color.BLUE));
		assertEquals(1, court.getMoves().size());

		GameCourt.Mode mode9 = court.getCurrentMode();
		assertTrue(mode9 instanceof GameCourt.SelectMode);
		mode9.action(5, 5); //selected red piece
		GameCourt.Mode mode10 = court.getCurrentMode();
		assertTrue(mode10 instanceof GameCourt.MoveMode);
		mode10.action(4, 4); //moved red piece
		assertEquals(Color.RED, court.getMoves().peekLast()[4][4].getColor());
		assertEquals(4, court.getMoves().peekLast()[4][4].getX());
		assertEquals(4, court.getMoves().peekLast()[4][4].getY());
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][5]);

		GameCourt.Mode mode11 = court.getCurrentMode();
		assertTrue(mode11 instanceof GameCourt.SelectMode);
		mode11.action(2, 2); 
		GameCourt.Mode mode12 = court.getCurrentMode();
		assertTrue(mode12 instanceof GameCourt.MoveMode);
		mode12.action(3, 3);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[2][2]);

		court.undo();
		GameObj[][] currentBoard3 = court.getMoves().peekLast();
		assertEquals(new RegularPiece(5, 5, Color.RED), currentBoard3[5][5]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard3[4][4]);
		assertEquals(new RegularPiece(2, 2, Color.BLUE), currentBoard3[2][2]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard3[3][3]);
		assertEquals(0, court.getUndos(Color.RED));
		assertEquals(3, court.getUndos(Color.BLUE));
		assertEquals(1, court.getMoves().size());

		GameCourt.Mode mode13 = court.getCurrentMode();
		assertTrue(mode13 instanceof GameCourt.SelectMode);
		mode13.action(5, 5); //selected red piece
		GameCourt.Mode mode14 = court.getCurrentMode();
		assertTrue(mode14 instanceof GameCourt.MoveMode);
		mode14.action(4, 4); //moved red piece
		assertEquals(Color.RED, court.getMoves().peekLast()[4][4].getColor());
		assertEquals(4, court.getMoves().peekLast()[4][4].getX());
		assertEquals(4, court.getMoves().peekLast()[4][4].getY());
		assertEquals(new RegularPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[5][5]);

		GameCourt.Mode mode15 = court.getCurrentMode();
		assertTrue(mode15 instanceof GameCourt.SelectMode);
		mode15.action(2, 2); 
		GameCourt.Mode mode16 = court.getCurrentMode();
		assertTrue(mode16 instanceof GameCourt.MoveMode);
		mode16.action(3, 3);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), court.getMoves().peekLast()[2][2]);

		court.undo();
		GameObj[][] currentBoard4 = court.getMoves().peekLast();
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard4[5][5]);
		assertEquals(new RegularPiece(4, 4, Color.RED), currentBoard4[4][4]);
		assertEquals(new EmptyPiece(0, 0, Color.WHITE), currentBoard4[2][2]);
		assertEquals(new RegularPiece(3, 3, Color.BLUE), currentBoard4[3][3]);
		assertEquals(0, court.getUndos(Color.RED));
		assertEquals(3, court.getUndos(Color.BLUE));
		assertEquals(3, court.getMoves().size());

	}


	@Test 
	public void pieceBecomesKing() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();

		court.getCurrentMode().action(5, 5); //selected red piece
		court.getCurrentMode().action(4, 4); //moved red piece

		court.getCurrentMode().action(4, 2); //select blue piece
		court.getCurrentMode().action(3, 3); //move blue piece

		court.getCurrentMode().action(4, 4); //selected red piece
		court.getCurrentMode().action(5, 3); //moved red piece

		court.getCurrentMode().action(6, 2); //select blue piece
		court.getCurrentMode().action(7, 3); //move blue piece

		court.getCurrentMode().action(5, 3); //selected red piece
		court.getCurrentMode().action(6, 2); //moved red piece

		court.getCurrentMode().action(5, 1); //select blue piece
		court.getCurrentMode().action(4, 2); //move blue piece

		court.getCurrentMode().action(7, 5); //selected red piece
		court.getCurrentMode().action(6, 4); //moved red piece

		court.getCurrentMode().action(4, 0); //select blue piece
		court.getCurrentMode().action(5, 1); //move blue piece

		court.getCurrentMode().action(6, 2); //selected red piece
		court.getCurrentMode().action(4, 0); //moved red piece

		GameObj[][] currentBoard = court.getMoves().peekLast();
		assertEquals(new KingPiece(4, 0, Color.RED), currentBoard[4][0]);
		assertEquals(14, court.getScore(Color.RED));
		assertEquals(11, court.getScore(Color.BLUE));
		assertEquals(10, court.getMoves().size());
	}


	@Test
	public void skipTurn() {
		
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();

		court.skipTurn(); //red usually starts
		court.getCurrentMode().action(2, 2); //selected blue piece
		court.getCurrentMode().action(1, 3); //moved blue piece
		assertEquals(new RegularPiece(1, 3, Color.BLUE), court.getMoves().peekLast()[1][3]);
		assertEquals(2, court.getMoves().size());

	}

	@Test
	public void testKingMovementAndCapture() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();

		GameObj[][] currentBoard = court.getMoves().peekLast();
		currentBoard[5][5] = new KingPiece(5, 5, Color.RED);

		court.getCurrentMode().action(5, 5); //selected red piece
		court.getCurrentMode().action(4, 4); //moved red piece
		assertEquals(new KingPiece(4, 4, Color.RED), court.getMoves().peekLast()[4][4]);

		court.getCurrentMode().action(2, 2); //selected blue piece
		court.getCurrentMode().action(3, 3); //moved blue piece
		assertEquals(new RegularPiece(3, 3, Color.BLUE), court.getMoves().peekLast()[3][3]);

		court.getCurrentMode().action(4, 4); //selected red piece
		court.getCurrentMode().action(5, 5); //moved red piece
		assertEquals(new KingPiece(5, 5, Color.RED), court.getMoves().peekLast()[5][5]);

		court.getCurrentMode().action(3, 3); //selected blue piece
		court.getCurrentMode().action(4, 4); //moved blue piece
		assertEquals(new RegularPiece(4, 4, Color.BLUE), court.getMoves().peekLast()[4][4]);

		court.getCurrentMode().action(5, 5); //selected red piece
		court.getCurrentMode().action(3, 3); //moved red piece
		assertEquals(new KingPiece(3, 3, Color.RED), court.getMoves().peekLast()[3][3]);

		court.getCurrentMode().action(4, 2); //selected blue piece
		court.getCurrentMode().action(5, 3); //moved blue piece
		assertEquals(new RegularPiece(5, 3, Color.BLUE), court.getMoves().peekLast()[5][3]);

		court.getCurrentMode().action(1, 5); //selected red piece
		court.getCurrentMode().action(0, 4); //moved red piece
		assertEquals(new RegularPiece(0, 4, Color.RED), court.getMoves().peekLast()[0][4]);

		court.getCurrentMode().action(5, 3); //selected blue piece
		court.getCurrentMode().action(4, 4); //moved blue piece
		assertEquals(new RegularPiece(4, 4, Color.BLUE), court.getMoves().peekLast()[4][4]);

		court.getCurrentMode().action(3, 3); //selected red piece
		court.getCurrentMode().action(5, 5); //moved red piece
		assertEquals(new KingPiece(5, 5, Color.RED), court.getMoves().peekLast()[5][5]);
		assertEquals(10, court.getMoves().size());

	}

	@Test(expected = IndexOutOfBoundsException.class) 
	public void testOutOfBounds() {
		JLabel status = new JLabel();
		GameCourt court = new GameCourt(status);
		court.constructInitialPiecesArray();
		court.getCurrentMode().action(7, 5); //selected red piece
		court.getCurrentMode().action(8, 4); //moved red piece
	}

}