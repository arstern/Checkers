import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class EmptyPiece extends GameObj{

    public EmptyPiece(int px, int py, Color color) {
		super(px, py, color);
	}
	
	@Override
	public void draw(Graphics g) {	
	}

	@Override
	public void move(int finalX, int finalY) {		
	}

	@Override
	public boolean isValidMove(int initX, int initY, int finalX, int finalY) {
		return false;
	}

	@Override
	public boolean isValidCapture(int initX, int initY, int finalX, int finalY) {
		return false;
	}
	
}
