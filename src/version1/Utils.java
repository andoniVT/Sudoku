package version1;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class Utils 
{

static int N = 9;
	
	static void printM(int[][] matrix, int N)
	{
	  for(int i=0; i<N; i++)
	  {
		  for(int j=0; j<N; j++)
			  System.out.print(matrix[i][j] + " ");
		  System.out.print("\n");
	  }
	}
		
	public static String readFile(String filename) throws IOException
	{
	    String content = null;
	    File file = new File(filename); //for ex foo.txt
	    FileReader reader = null;
	    try {
	        reader = new FileReader(file);
	        char[] chars = new char[(int) file.length()];
	        reader.read(chars);
	        content = new String(chars);
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    } finally {
	        if(reader !=null){reader.close();}
	    }
	    return content;
	}
	
	public static Vector parseFile(String file, int N) throws IOException
	{
		String content = readFile(file);
		int n_tests = Integer.parseInt(content.substring(0, content.indexOf("\n")));
		content = content.substring(content.indexOf("\n")+1);
		Vector matrices = new Vector();
		for(int i=0; i<n_tests; i++)
		{
		   int [][] matrix = new int[N][N]; 
		  for(int j=0; j<N; j++)
		   {
			   String line;
			   if(content.indexOf("\n")!=-1)
					line = content.substring(0, content.indexOf("\n"));
				else
					line = content; 			   
			   content = content.substring(content.indexOf("\n")+1);	   			   			   
			   for(int k=0; k<N; k++)
			   {
				   String value;
				   if(line.indexOf(" ")!=-1)
				       value = line.substring(0, line.indexOf(" "));
				   else
					   value = line;
				   line = line.substring(line.indexOf(" ")+1);				  
				   matrix[j][k] = Integer.parseInt(value);
			   }  
		   }		   
		   content = content.substring(content.indexOf("\n")+1);		   
		   matrices.addElement(matrix);
		}
		return matrices;
	}
		
	public static String pair_to_string(int row, int col)
	{
	   return String.valueOf(row) + String.valueOf(col);	   
	}
	
	public static Vector string_to_pair(String key)
	{
		int row = Integer.parseInt(String.valueOf(key.charAt(0)));
		int col = Integer.parseInt(String.valueOf(key.charAt(1)));
		Vector pair = new Vector(); pair.add(row); pair.add(col); 
		return pair; 
	}
		
	
	static void printMap(Map<String, Vector> map)
	{
		Iterator it = map.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String) it.next();
			Vector res = map.get(key);
			System.out.println("(" + key + " , " + res + ")");		
		}	
	}
	
	
	public static void main(String[] args) throws IOException 
	{
		
		Vector matrices  = parseFile("entrada.txt", 9);
		int [][] matrix = (int[][]) matrices.get(2);
		printM(matrix, 9);
		
	
		//int [][] mat = {{0,0,0,0},{0,2,0,1},{4,0,1,0},{0,0,0,0}};
		//int [][] mat = {{1,4,2,3},{3,2,4,1},{4,3,1,2},{2,1,3,4}};
		//printM(mat, 4);
		System.out.println("\n");
						
		
	}

}
