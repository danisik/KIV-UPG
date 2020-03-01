package generatorTerenu;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

/**
 * Vykreslovani panelu
 * 
 * @author Vojtech Danisik
 *
 */
public class GamePanel extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	/** atribut pro praci s terenem*/
	public Terrain terrain;
	
	/** atribut pro praci s vetrem*/
	public Wind wind;

	/** atribut pro vykresleni strelce na mapu*/
	public NamedPosition strelec;
	
	/** atribut pro vykresleni cile na mapu*/
	public NamedPosition cil;
	
	/** atribut pro vykresleni oblasti zasahu na mapu*/
	public NamedPosition hitSpot; 
	
	/** konstanta pro pøevod mm na m*/
	private static final double mmToM = 1000.0;
	
	/** prevod metru na pixely u X-ove souradnice*/
	public static double scaleX;
	
	/** prevod metru na pixely u Y-ove souradnice*/
	public static double scaleY;
	
	/** atribut pro praci s trajektorii*/
	private Trajectory trajectory;
	
	/** pomocny atribut pro vykreslovani trajektorie*/
	public int index = 0;
	
	/**
	 * Vytvoreni panelu pro vykreslovani
	 * @param width sirka panelu
	 * @param height vyska panelu
	 */
	public GamePanel(int width, int height)
	{
		this.setLocation(10, 10);
		this.setSize(width, height);
		this.setPreferredSize(this.getSize());
	}
	
	/**
	 * vykresleni vsech objektu
	 */
	@Override
	public void paint(Graphics g) 
	{
		super.paint(g);
		
		/** pro vykreslování 2D objektù*/
		Graphics2D g2 = (Graphics2D) g;
		
		scaleX = ((double)this.getWidth()) / ((double)terrain.getDeltaXInMM() * (double)terrain.terrain[0].length/mmToM);
		scaleY = ((double)this.getHeight()) / ((double)terrain.getDeltaYInMM() * (double)terrain.terrain.length/mmToM);
		
		if (terrain != null) 
		{
			terrain.draw(g2, this.getWidth(), this.getHeight());
		}
		if (hitSpot != null) 
		{	
			//hitSpot.draw(g2, scaleX, scaleY);
		}
		if (strelec != null) 
		{
			strelec.draw(g2, scaleX, scaleY);
		}
		if (cil != null) 
		{	
			cil.draw(g2, scaleX, scaleY);
		}
		if (wind != null)
		{
			wind.draw(g2);
		}
		if (trajectory != null)
		{
			try
			{
				trajectory.draw(g2, scaleX, scaleY, index);
				if (index <= (trajectory.size()*3)-3) index += 1500;
			}
			catch(Exception e)
			{
				index = (trajectory.size()*3)-3;
				trajectory.draw(g2, scaleX, scaleY, index);
			}
		}
		
	}	
	
	/**
	 * Ulozeni terenu do atributu
	 * 
	 * @param terrain teren, ktery chci ulozit
	 */
	public void setTerrain(Terrain terrain)
	{
		this.terrain = terrain;
	}
	
	/**
	 * Ulozeni strelce do atributu
	 * 
	 * @param strelec objekt strelec, ktery chci ulozit
	 */
	public void setStrelec(NamedPosition strelec)
	{
		this.strelec = strelec;
	}
	
	/**
	 * Ulozeni cilu do atributu
	 * 
	 * @param cil objekt cil, ktery chci ulozit
	 */
	public void setCil(NamedPosition cil)
	{
		this.cil = cil;
	}
	
	/**
	 * Ulozeni hitSpotu do atributu
	 * 
	 * @param hitSpot objekt hitSpot, ktery chci ulozit
	 */
	public void setHitSpot(NamedPosition hitSpot)
	{
		this.hitSpot = hitSpot;
	}
	
	/**
	 * ulozeni vetru do atributu
	 * 
	 * @param wind vitr, se kterym budu pracovat
	 */
	public void setWind(Wind wind)
	{
		this.wind = wind;
	}
	
	/**
	 * ulozeni trajectory do atributu
	 * 
	 * @param trajectory trajectory, se kterym budu pracovat
	 */
	public void setTrajectory(Trajectory trajectory)
	{
		this.trajectory = trajectory;
	}
}
