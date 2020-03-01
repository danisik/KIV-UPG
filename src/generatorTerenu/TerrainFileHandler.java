package generatorTerenu;
import java.io.DataInputStream;
import java.io.FileInputStream;

/**
 * Nacteni souboru se vstupnimi daty
 * 
 * @author Vojtech Danisik
 *
 */
public class TerrainFileHandler 
{
	/** pole nadmorskych vysek*/
	 public int[][] terrain;
	 
	 /** udava sirku mapy*/
	 public int sloupce;
	 
	 /** udava vysku mapy*/
	 public int radky;
	 
	 /** udava rozestup sloupcu*/
	 public int deltaX; 
	 
	 /** udava rozestup radku*/
	 public int deltaY;
	 
	 /** poloha strelce (sloupec)*/
	 public int strelecX;
	 
	 /** pohola strelce(radek)*/
	 public int strelecY;
	 
	 /** poloha cile (sloupec)*/
	 public int cilX;
	 
	 /** poloha cile (radek)*/
	 public int cilY;
	 
	/** pro pøevod mm na m */
	static final double mmToM = 1000.0;
	
	/** x-ova souradnice strelce v metrech*/
	public double strelecSouradniceX;
	
	/** y-ova souradnice strelce v metrech*/
	public double strelecSouradniceY;
	
	/** x-ova souradnice cilu v metrech*/
	public double cilSouradniceX;
	
	/** y-ova souradnice cilu v metrech*/
	public double cilSouradniceY;
	
	/** hodnota urcujici zda je strelec mimo mapu ci ne*/
	public boolean strelecMimoMapu = false;
	
	/** hodnota urcujici zda je cil mimo mapu ci ne*/
	public boolean cilMimoMapu = false;
	 
	 /**
	  * Nacte vstupni soubor a nastavi parametry
	  * @param fileName nazev vstupniho souboru
	  */
	 public void loadTerFile(String fileName)
	 {
	        try
	        {
	        	DataInputStream hodnoty = new DataInputStream(new FileInputStream(fileName));
	        	sloupce = hodnoty.readInt();
	        	radky = hodnoty.readInt();
	        	deltaX = hodnoty.readInt();
	        	deltaY = hodnoty.readInt();
	        	strelecX = hodnoty.readInt();
	        	strelecY = hodnoty.readInt();
	        	cilX = hodnoty.readInt();
	        	cilY = hodnoty.readInt();
	        	
	        		
	        	terrain = new int[radky][sloupce];
	        	
	        	for (int i = 0; i < radky; i++)
	        	{
	        		for (int j = 0; j < sloupce; j++)
	        		{
	        			terrain[i][j] = hodnoty.readInt();
	        		}
	        	}
	        	hodnoty.close();
	        	
	         	strelecSouradniceX = strelecX * deltaX / mmToM;
	         	strelecSouradniceY = strelecY * deltaY / mmToM;
	         	cilSouradniceX = cilX * deltaX / mmToM;
	         	cilSouradniceY = cilY * deltaX / mmToM;
	        }
	        catch(Exception e)
	        {
	        	System.out.println("Neexistujici soubor nebo spatne zadane hodnoty!!");
	        	System.exit(0);
	        }
	 } 
	 
	 /**
	  * Naètená data vypíše na konzoli
	  */
	 public void printData()
	 {
		 System.out.println("Pocet sloupcu: " + sloupce + ", Pocet radku: " + radky);
		 System.out.println("Rozestup sloupcu: " + deltaX/mmToM + "m, Rozestup radku: " + deltaY/mmToM + "m");
		
		 System.out.println("Poloha strelce (x,y): " + strelecSouradniceX + "m," + 
		 strelecSouradniceY + "m v " + strelecX + ".sloupci a " + strelecY + ".radku");
		 if (strelecX < 0 || strelecY < 0 || strelecX >= sloupce || strelecY >= radky)
		 {
			 System.out.println("Strelec je mimo mapu!!");
			 strelecMimoMapu = true;
		 }
		 else
		 {
			 System.out.println("Nadmorska vyska strelce: " + terrain[strelecY][strelecX] / mmToM + "m");
		 }
		
		 System.out.println("Poloha cilu (x,y): " + cilSouradniceX + "m," +
		 cilSouradniceY + "m v " + cilX + ".sloupci a " + cilY + ".radku");
		 if (cilX < 0 || cilY < 0 || cilX >= sloupce || cilY >= radky)
		 {
			 System.out.println("Cil je mimo mapu!!");
			 cilMimoMapu = true;
		 }
		 else
		 {
			 System.out.println("Nadmorska vyska cile: " + terrain[cilY][cilX] / mmToM + "m");
		 }
		 
		 if (sloupce * deltaX == 0 || radky * deltaY == 0)
		 {
			 System.out.println("Teren neobsahuje zadna vyskova data!!");
			 System.exit(0);
		 }
		 else
		 {/*
			 System.out.println("Nadmorske vysky");
			 
			 for (int i = 0; i < radky; i++)
	     	 {
	     		System.out.println((i+1) + ". øádek: ");
	     		for (int j = 0; j < sloupce; j++)
	     		{
	     			System.out.print(terrain[i][j] / mmToM + "m ");
	     		}
	     		System.out.println("");
	     	 }*/
		 }
	 }
	 
	 /**
	  * naplní atributy testovacími daty
	  */
	 public void generateData(String[] args)
	 {
     	sloupce = Integer.parseInt(args[0]);
     	radky = Integer.parseInt(args[1]);
     	deltaX = Integer.parseInt(args[2]);
     	deltaY = Integer.parseInt(args[3]);
     	strelecX = Integer.parseInt(args[4]);
     	strelecY = Integer.parseInt(args[5]);
     	cilX = Integer.parseInt(args[6]);
     	cilY = Integer.parseInt(args[7]);
     	
     	strelecSouradniceX = strelecX * deltaX / mmToM;
     	strelecSouradniceY = strelecY * deltaY / mmToM;
     	cilSouradniceX = cilX * deltaX / mmToM;
     	cilSouradniceY = cilY * deltaX / mmToM;
     	int argsNo = 8;
     	terrain = new int[radky][sloupce];
     	
    	for (int i = 0; i < radky; i++)
    	{
    		for (int j = 0; j < sloupce; j++)
    		{
    			terrain[i][j] = Integer.parseInt(args[argsNo]);
    			argsNo++;
    		}
    	}
	 }
}
