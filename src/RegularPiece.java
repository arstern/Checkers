import java.awt.Color;
import java.awt.Graphics;

@SuppressWarnings("serial")
public class RegularPiece extends GameObj {


	public static final int SIZE = 50;
	public int px = 0;
	public int py = 0;
	private final Color color;

	public RegularPiece(int px, int py, Color color) {
		super(px, py, color);
		this.px = px;
		this.py = py;
		this.color = color;
	}

	public int getX() {
		return this.px;
	}

	public int getY() {
		return this.py;
	}

	@Override
	public void move(int finalX, int finalY) {
		this.px = finalX;
		this.py = finalY;
	}

	@Override
	public void draw(Graphics g) {	
		g.setColor(this.color);
		g.fillOval(this.px*50, this.py*50, RegularPiece.SIZE, RegularPiece.SIZE);	
	}

	@Override
	public boolean isValidMove(int initX, int initY, int finalX, int finalY) {
		if (color.equals(Color.RED)) {
			if (((initY - finalY) == 1) && (Math.abs(finalX - initX) == 1)) {
				return true;
			}
		} else if (color.equals(Color.BLUE)) {
			if (((initY - finalY) == -1) && (Math.abs(finalX - initX) == 1)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean isValidCapture(int initX, int initY, int finalX, int finalY) {
		if (color.equals(Color.RED)) {
			if (((initY - finalY) == 2) && (Math.abs(finalX - initX) == 2)) {
				return true;
			}
		} else if (color.equals(Color.BLUE)) {
			if (((initY - finalY) == -2) && (Math.abs(finalX - initX) == 2)) {
				return true;
			}
		}
		return false;
	}

}


