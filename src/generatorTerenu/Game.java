package generatorTerenu;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Scanner;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.*;

/**
 * Spusteni cele aplikace
 * 
 * @author Vojtech Danisik
 *
 */
public class Game 
{
	/** sirka frame*/
	private static int sirkaOkna;
	
	/** vyska frame*/
	private static int vyskaOkna;
	
	/** sirka panelu*/
	private static int sirkaPanelu;
	
	/** vyska panelu*/
	private static int vyskaPanelu;
	
	/** sirka frame grafu*/
	private static int sirkaFrameGrafu = 1300;
	
	/** vyska frame grafu*/
	private static int vyskaFrameGrafu = 600;
	
	/** sirka grafu*/
	private static int sirkaGrafu = 600;
	
	/** vyska grafu*/
	private static int vyskaGrafu = 500;
	
	/** vytvoreni frame*/
	private static JFrame frame = new JFrame();
	
	/** vytvoreni frame pro graf*/
	private static JFrame grafFrame = new JFrame();
	
	/** nadefinovani gamepanelu*/
	private static GamePanel drawarea;
	
	/** nadefinovani grafu 1*/
	private static Graf1 graf1 = new Graf1(sirkaGrafu, vyskaGrafu);
	
	/** nadefinovani grafu 2*/
	private static Graf2 graf2 = new Graf2(sirkaGrafu, vyskaGrafu);
	
	/** atribut strelce*/
	public static NamedPosition strelec;
	
	/** atribut cile*/
	public static NamedPosition cil;
	
	/** atribut mista strely*/
	public static NamedPosition hitSpot;
	
	/** atribut terenu*/
	public static Terrain terrain;
	
	/** atribut vetru*/
	public static Wind wind;
	
	/** pro pøevod mm na m */
	static final double mmToM = 1000.0;
	
	/** atribut pro vytvareni hitspotu*/
	static ShootingCalculator strileni;
	
	/** atribut pro praci s pomerem stran*/
	private static double pomerStran;
	
	/**
	 * Vraci nam sirku frame
	 * @return sirka frame
	 */
	public static int getSirkaOkna()
	{
		return sirkaOkna;
	}
	
	/**
	 * Vraci nam vyska frame
	 * @return vyska frame
	 */
	public static int getVyskaOkna()
	{
		return vyskaOkna;
	}
	
	/**
	 * Vraci nam sirku panelu
	 * @return sirka panelu
	 */
	public static int getSirkaPanelu()
	{
		return sirkaPanelu;
	}
	
	/**
	 * Vraci nam vyska panelu
	 * @return vyska panelu
	 */
	public static int getVyskaPanelu()
	{
		return vyskaPanelu;
	}
	
	/**
	 * Vykonavani celeho programu
	 * @param args vstupni parametry
	 */
	public static void main(String[] args) 
	{       
		frame.addComponentListener(new ComponentAdapter() {
		    @Override
		    public void componentResized(ComponentEvent e) 
		    {	
		    	Component c = (Component)e.getSource(); 	
		    	int staraSirka = sirkaOkna;
		    	int staraVyska = vyskaOkna;
		    	sirkaOkna = c.getWidth();
		    	vyskaOkna = c.getHeight();
		    	
		    	if (staraSirka == sirkaOkna)
		    	{
				    vyskaPanelu = vyskaOkna;
				    sirkaPanelu = (int)(vyskaPanelu * pomerStran);
				    vyskaPanelu -= 55;
				    sirkaPanelu -= 55;
				    
			    	if (sirkaPanelu+55 < sirkaOkna)
			    	{
					    drawarea.setSize(sirkaPanelu, vyskaPanelu);
			    	}
		    	}
		    	else if (staraVyska == vyskaOkna)
		    	{
		    		sirkaPanelu = sirkaOkna;
		    		vyskaPanelu = (int)(sirkaPanelu / pomerStran);
				    vyskaPanelu -= 55;
				    sirkaPanelu -= 55;
				    
			    	if (vyskaPanelu+55 < vyskaOkna)
			    	{
					    drawarea.setSize(sirkaPanelu, vyskaPanelu);
			    	}
		    	}
		    	else
		    	{
				    vyskaPanelu = vyskaOkna;
				    sirkaPanelu = (int)(vyskaPanelu * pomerStran);
				    vyskaPanelu -= 55;
				    sirkaPanelu -= 55;
				    
			    	if (sirkaPanelu+55 < sirkaOkna)
			    	{
					    drawarea.setSize(sirkaPanelu, vyskaPanelu);
			    	}
		    	}
		    	drawarea.repaint();
		    }
		});
		
		TerrainFileHandler terrainFile = new TerrainFileHandler();
		try
		{
			terrainFile.generateData(args);
		}
		catch(Exception e)
		{
			System.out.println("Navrhovane soubory: rovny1metr.ter, rovny1metr_1km_x_1km.ter");
			System.out.println("sikmy45stupnu.ter, terrain257x257.ter, terrain512x512.ter");
			System.out.println("Zadejte nazev souboru: ");
			Scanner sc = new Scanner(System.in);
			terrainFile.loadTerFile(sc.nextLine());
			//terrainFile.loadTerFile("terrain512x512.ter");
			//terrainFile.loadTerFile("terrain257x257.ter");
			//terrainFile.loadTerFile("sikmy45stupnu.ter");
			//terrainFile.loadTerFile("rovny1metr_1km_x_1km.ter");
			//terrainFile.loadTerFile("rovny1metr.ter");
		}
		terrainFile.printData();
		
		
		double sirkaTerenu = terrainFile.terrain[0].length * terrainFile.deltaX / mmToM;
		double vyskaTerenu = terrainFile.terrain.length * terrainFile.deltaY / mmToM;
		
		pomerStran = sirkaTerenu / vyskaTerenu;
		
		vyskaPanelu = 300;
		sirkaPanelu = (int)(vyskaPanelu * pomerStran);
		drawarea = new GamePanel(sirkaPanelu, vyskaPanelu);
		sirkaOkna = sirkaPanelu + 55;
		vyskaOkna = vyskaPanelu + 55;
		
		strelec = new NamedPosition(terrainFile.strelecSouradniceX,terrainFile.strelecSouradniceY,
				new Object(), Color.BLUE, 10.0, "strelec");
		cil = new NamedPosition(terrainFile.cilSouradniceX,terrainFile.cilSouradniceY,
				new Object(), Color.RED, 10.0, "cil");
		double strelecCilVzdalenost = strelec.getDistance(cil);
		System.out.println("Vzdalenost strelce od cile: " + strelecCilVzdalenost);
		terrain = new Terrain(terrainFile.terrain, terrainFile.deltaX, terrainFile.deltaY);
		
		if(terrainFile.strelecMimoMapu == false)
		System.out.println("Nadmorska vyska strelce (v metrech): " + terrain.getAltitudeInM(strelec.x,strelec.y));
		else System.out.println("Nejde urcit nadmorska vyska strelce, strelec je mimo mapu.");
		
		if(terrainFile.cilMimoMapu == false)
		System.out.println("Nadmorska vyska cile (v metrech): " + terrain.getAltitudeInM(cil.x,cil.y));
		else System.out.println("Nejde urcit nadmorska vyska cile, strelec je mimo mapu.");
		
		wind = new Wind(40);
		
		drawarea.setTerrain(terrain);
		drawarea.setStrelec(strelec);
		drawarea.setCil(cil);
		drawarea.setWind(wind);
		
		makeWindow();
		gameMainLoop();
	}
	
	/**
	 * Vykresluje okno s objektama
	 */
	public static void makeWindow()
	{
		
		frame.setTitle("Vojtech Danisik A16B0019P");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.setLayout(null);
		
		frame.setSize(sirkaOkna, vyskaOkna);

		frame.setPreferredSize(frame.getSize());
		
		frame.setResizable(true);
		
		frame.add(drawarea,BorderLayout.CENTER);
		
		frame.pack();

		frame.setLocationRelativeTo(null);
			
		frame.setVisible(true);
	}
	
	/**
	 * Vykresluje okno s objektama
	 */
	public static void makeGraf()
	{
		grafFrame.setTitle("Vojtech Danisik A16B0019P");
		
		//grafFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		grafFrame.setLayout(null);
		
		grafFrame.setSize(sirkaFrameGrafu, vyskaFrameGrafu);

		grafFrame.setPreferredSize(grafFrame.getSize());
		
		grafFrame.setResizable(true);
		
		grafFrame.add(graf1);
		grafFrame.add(graf2);
		
		grafFrame.pack();

		grafFrame.setLocationRelativeTo(null);
			
		grafFrame.setVisible(true);
	}
	
	
	/**
	 * Smyèka, která zajišuje neustálé støílení pokud chceme
	 */
	public static void gameMainLoop()
	{
	      Timer timer = new Timer();
	      timer.schedule(new TimerTask() 
	      {
	    	  @Override
	          public void run() 
	    	  {
	    		  wind.generateParams();
	    		  drawarea.repaint();  
	          }
	        }
	      , 0, 500);
	    
	
		strileni = new ShootingCalculator(strelec, cil);
		strileni.setTerrain(terrain);
		strileni.setWind(wind);
		Scanner sc = new Scanner(System.in);
		boolean pokracuj = true;
		String[] pole = new String[3];
		try
		{
			while(pokracuj)
			{
				System.out.println("Zadejte azimuth ve stupnich, elevaci ve stupnich a rychlost strely v m/s");
				String hodnoty = sc.nextLine();
				pole = hodnoty.split(" ");
				drawarea.index = 0;
				System.out.println("Chcete vykreslit vizualizaci nebo støílet? (vizualizace/strelba)");
				hodnoty = sc.nextLine();
				while (hodnoty.contains("strelba") == false && hodnoty.contains("vizualizace") == false)
				{
					System.out.println("Zadejte vizualizace nebo strelba !");
					hodnoty = sc.nextLine();
				}

				if (hodnoty.contains("strelba"))
				{
					strileni.shoot(Double.parseDouble(pole[0]), Double.parseDouble(pole[1]), Double.parseDouble(pole[2]));
					if (strileni.testTargetHit()) { System.out.println("ZASAH"); }
					else { System.out.println("VEDLE"); }
					drawarea.setHitSpot(strileni.getHitSpot());
					drawarea.setTrajectory(strileni.getTrajectory());
					drawarea.repaint();
				}
				else if (hodnoty.contains("vizualizace"))
				{
					graf1.setElevace(Double.parseDouble(pole[1]));
					graf1.setpocRychlostStrelby(Double.parseDouble(pole[2]));
					graf2.setElevace(Double.parseDouble(pole[1]));
					graf2.setTerrain(terrain);
					graf2.setStrelec(strelec);
					graf2.setAzimut(Double.parseDouble(pole[0]));
					graf2.setPocRychlostStrelby(Double.parseDouble(pole[2]));
					makeGraf();
				}
				
				System.out.println("Chcete pokracovat ? (ano/ne)");
				String pismeno = sc.nextLine();
				while(pismeno.contains("ano") == false && pismeno.contains("ne") == false)
				{
					System.out.println("Zadejte ano nebo ne !");
					pismeno = sc.nextLine();
				}
				hodnoty = "";
				drawarea.setTrajectory(null);
				if (pismeno.contains("ne") == true) break;
			}
			sc.close();
			System.out.println("Konec Programu");
			System.exit(0);
		}
		catch (Exception e)
		{
			System.out.println("Špatnì zadané hodnoty!!");
			gameMainLoop();
		}
	}
}
