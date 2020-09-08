import java.awt.*;
import javax.swing.JComponent;

@SuppressWarnings("serial")
public abstract class GameObj extends JComponent {

	private int px; 
    private int py;
    private final Color color;

    public GameObj( int px, int py, Color color) {
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
    
    public Color getColor() {
    	return this.color;
    }
        
    
    public abstract boolean isValidMove(int initX, int initY, int finalX, int finalY);
        
    public abstract boolean isValidCapture(int initX, int initY, int finalX, int finalY);

    public abstract void move(int finalX, int finalY);
    
    public abstract void draw(Graphics g);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameObj other = (GameObj) obj;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		return true;
	}
}