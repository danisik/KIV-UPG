package generatorTerenu;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Slouzi pro obsluhu mapy
 * 
 * @author Vojtech Danisik
 *
 */
public class Terrain 
{
	/** pole nadmorskych vysek*/
	public int[][] terrain;
	
	/** roztec sloupcu*/
	private int deltaXInMM;
	
	/** roztec radku*/
	private int deltaYInMM;
	
	/** konstanta pro pøevod mm na m*/
	static final double mmToM = 1000.0;
	
	List<Integer> listVysky = new ArrayList<>();
	
	private int maxVyska;
	
	private int minVyska;
	
	private BufferedImage terrainImage;
	
	List<Integer> listRGB = new ArrayList<>();
	
	/**
	 * Vytvoreni terenu
	 * @param terrain pole nadmorskych vysek
	 * @param deltaXlnMM sirka sloupce
	 * @param deltaYlnMM vyska radku
	 */
	public Terrain(int[][] terrain, int deltaXlnMM, int deltaYlnMM)
	{
		this.terrain = terrain;
		this.deltaXInMM = deltaXlnMM;
		this.deltaYInMM = deltaYlnMM;
		
		naplnListVyskama();
		getMaxVyska();
		getMinVyska();
		naplnListRGB();
	}
	
	/**
	 * Pomoci zadanych parametru (sloupec,radek) zjistime z terenu nadmorskou vysku
	 * 
	 * @param x x-ova souradnice objektu v metrech
	 * @param y y-ova souradnice objektu v metrech
	 * @return nadmorskou vysku v metrech
	 */
	public double getAltitudeInM(double x, double y)
	{
		try
		{
			int xSouradnice = (int)(x / deltaXInMM * mmToM);
			int ySouradnice = (int)(y / deltaYInMM * mmToM);		
			double nVyska = terrain[ySouradnice][xSouradnice] / mmToM;
			return nVyska;
		}
		catch(Exception e)
		{
			return 0;
		}
	}

	/**
	 * Vraci nam sirku sloupcu
	 * @return sirka sloupcu
	 */
	public int getDeltaXInMM()
	{
		return deltaXInMM;
	}
	
	/**
	 * Vraci nam vysku radku
	 * @return vyska radku
	 */
	public int getDeltaYInMM()
	{
		return deltaYInMM;
	}
	
	/**
	 * Vykresli teren
	 * @param g2 nastroj pro vykresleni
	 * @param scale cislo ktere prevede metry na pixely
	 */
	public void draw(Graphics2D g2, double sirkaPanelu, double vyskaPanelu) 
	{	
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
		makeImage();
		
		if (minVyska == maxVyska)
		{
			for(int i = 0; i < terrainImage.getWidth(); i++)
				for (int j = 0; j < terrainImage.getHeight(); j++)
					terrainImage.setRGB(i,j,new Color(128,128,128).getRGB());
		}
		else
		{
			int rgb = 0;
			int argNo = 0;
			
			for (int i = 0; i < terrain.length; i++)
			{
				for (int j = 0; j < terrain[0].length; j++)
				{
					rgb = listRGB.get(argNo);
					terrainImage.setRGB(j, i, new Color(rgb,rgb,rgb).getRGB());
					argNo++;
				}
			}
		}
		g2.drawImage(terrainImage, 0, 0, (int)sirkaPanelu, (int)vyskaPanelu, null);
	}
	
	/**
	 * vytvori image
	 */
	private void makeImage()
	{
		terrainImage = new BufferedImage(terrain[0].length, terrain.length, BufferedImage.TYPE_INT_RGB);
		terrainImage.createGraphics();
	}
	
	/**
	 * naplni nam list nadmorskyma vyskama
	 */
	private void naplnListVyskama()
	{
		for (int i = 0; i < terrain.length; i++)
		{
			for (int j = 0; j < terrain[0].length; j++)
			{
				listVysky.add(terrain[i][j]);
			}
		}
	}
	
	/**
	 * naplni nam promennou minVyska minimalni vyskou v listu
	 */
	private void getMinVyska()
	{
		int hodnota;
		minVyska = Integer.MAX_VALUE;
		
		for (int i = 0; i < listVysky.size(); i++)
		{
			hodnota = listVysky.get(i);
			if (hodnota < minVyska) minVyska = hodnota;
		}
	}
	
	/**
	 * naplni nam promennou maxVyska maximalni vyskou v listu
	 */
	private void getMaxVyska()
	{
		int hodnota;	
		for (int i = 0; i < listVysky.size(); i++)
		{
			hodnota = listVysky.get(i);
			if (hodnota > maxVyska) maxVyska = hodnota;
		}
	}
	
	/**
	 * naplni nam druhy list hodnotama RGB
	 */
	private void naplnListRGB()
	{
		double t;
		for (int i = 0; i < listVysky.size(); i++)
		{
			t = (double)listVysky.get(i)/maxVyska;
			listRGB.add((int)(255 * t));
		}
	}
	
	/**
	 * vrati nam maximalni vysku v terenu (potreba kvuli grafu 2)
	 * @return maximalni vyska
	 */
	public int getMaximumVyska()
	{
		return maxVyska;
	}
}
