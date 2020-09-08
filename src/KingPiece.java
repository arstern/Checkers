import java.awt.*;

@SuppressWarnings("serial")
public class KingPiece extends GameObj {

	public static final int SIZE = 50;
	public int px = 0;
	public int py = 0;
	private final Color color;

	public KingPiece(int px, int py, Color color) {
		super(px, py, color);
		this.px = px;
		this.py = py;
		this.color = color;
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
		g.setColor(Color.WHITE);
		int fontSize = 25;
		g.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
		g.drawString("K", this.px*50+17, this.py*50+30);
	}


	@Override
	public boolean isValidMove(int initX, int initY, int finalX, int finalY) {
		if ((Math.abs(finalY - initY) == 1) && (Math.abs(finalX - initX) == 1)) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidCapture(int initX, int initY, int finalX, int finalY) {
		if ((Math.abs(finalY - initY) == 2) && (Math.abs(finalX - initX) == 2)) {
			return true;
		}
		return false;
	}

}
