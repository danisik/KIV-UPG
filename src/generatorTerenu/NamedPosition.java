package generatorTerenu;
import java.awt.Color;
import java.awt.Graphics2D;

import javafx.scene.shape.Ellipse;

/**
 * Vytvoreni objektu pro ulozeni hodnot strelce, cile apod.
 * 
 * @author Vojtech Danisik
 *
 */
public class NamedPosition 
{
	/** konstanta pro pøevod mm na m*/
	static final double mmToM = 1000.0;
	
	/** x-ova souradnice objektu v metrech*/
	public double x;
	
	/** y-ova souradnice objektu v metrech*/
	public double y;
	
	/** typ objektu (strelec, cíl, oblast zasahu, ...)*/
	public Object positionType;
	
	/** barva objektu*/
	public Color color;
	
	/** nazev objektu*/
	private String nazev;
	
	/** velikost objektu v metrech*/
	public double size;
	
	/** vykreslovací X*/
	public double xVykr;
	
	/** vykreslovací Y*/
	public double yVykr;
	
	/**
	 * Vytvoreni objektu ruznych typu
	 * 
	 * @param x souradnice x
	 * @param y souradnice y
	 * @param positionType typ objektu
	 * @param color barva objektu
	 * @param size velikost objektu
	 */
	public NamedPosition(double x, double y, Object positionType, Color color, double size, String nazev)
	{
		this.x = x;
		this.y = y;
		this.positionType = positionType;
		this.color = color;
		this.size = size;
		this.nazev = nazev;
	}
	
	/**
	 * Zjisteni vzdusne vzdalenosti dvou objektu od sebe
	 * @param position druhy porovnavany objekt
	 * @return vzdalenost dvou objektu
	 */
	public double getDistance(NamedPosition position)
	{
		double rozdilX = Math.pow(this.x - position.x, 2);
		double rozdilY = Math.pow(this.y - position.y, 2);
		double vzdalenost = Math.sqrt(rozdilX + rozdilY);
		
		return vzdalenost;
	}

	/**
	 * Vykresli nam strelce/hitSpot/cil
	 * @param g2 nastroj pro vykresleni
	 * @param scale cislo ktere prevede metry na pixely
	 */
	public void draw(Graphics2D g2, double scaleX, double scaleY) 
	{
		g2.setColor(color);
		if (positionType instanceof Ellipse)
		{
			//System.out.println("");
			//výpoèet šíøky a výšky elipsy (není to kruh, vykreslovací plocha je obdélník) na pixely
			double sizeNewX = size * scaleX; 
			double sizeNewY = size * scaleY; 
			
			//výpoèet souøadnic elipsy na pixely
			xVykr = (x * scaleX) - sizeNewX/2;
			yVykr = (y * scaleY) - sizeNewY/2 ;
			g2.fillOval((int)xVykr, (int)yVykr, (int)sizeNewX, (int)sizeNewY);
		}
		else 
		{
			//výpoèet souøadnic køížku na pixely (hodnoty -5, size - 5 slouží k tomu, aby souøadnice køížku byli ve støedu)
			xVykr = (int)(x * scaleX);
			yVykr = (int)(y * scaleY);
			if (nazev.equals("cil"))
			{
				g2.setColor(Color.GREEN);
				g2.fillOval((int)xVykr - 5, (int)yVykr - 5, 10, 10);
				g2.setColor(color);
				g2.drawLine((int)xVykr - 5, (int)yVykr, (int)xVykr + (int)size - 6, (int)yVykr);
				g2.drawLine((int)xVykr, (int)yVykr - 5, (int)xVykr, (int)yVykr + (int)size - 6);	
			}	
			else if (nazev.equals("strelec"))
			{
				g2.fillRect((int)xVykr - 5, (int)yVykr - 5, 10, 10);
				g2.setColor(Color.yellow);
				g2.fillRect((int)xVykr - 2, (int)yVykr - 2, 4, 4);
			}	
		}
		
	}
}
