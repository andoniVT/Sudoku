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

static int N = 4;
static Map<String, Vector> valores_probables;


	
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
	
	
	static boolean isInRow(int[][] matrix, int row, int value)
	{
		for(int i=0; i<N; i++)		
			if(matrix[row][i]==value) return true;		
		return false;
	}
	
	static boolean isInCol(int[][] matrix, int col, int value)
	{
		for(int i=0; i<N; i++)
			if(matrix[i][col]==value) return true;
		return false;
	}
	
	static boolean isInBox(int[][] matrix, int row, int col, int value)
	{
		int beginRow = row - row%2 ;
		int beginCol = col - col%2;
		
		for(int i=beginRow; i<beginRow+2; i++)
		{
			for(int j=beginCol; j<beginCol+2; j++)
				if(matrix[i][j]==value) return true;	
		}		
		return false;
	}
	
	static boolean isPerfect(int[][] matrix, int row, int col, int value)
	{
		if (!isInRow(matrix, row, value) && !isInCol(matrix, col, value) && !isInBox(matrix, row, col, value))
			return true;
		return false;
	}
	
	public static Vector getValues(int [][] mat, int row, int col)
	{
		Vector res = new Vector();
		if(mat[row][col]!=0)
		{
		  return res;	
		}
		
		for(int num=1; num<=N; num++)
		{
           if(isPerfect(mat, row, col, num))
           {
        	   res.add(num);
           }
		}
		return res; 	  
	}
	
	public static Vector getValues2(int [][] mat, int row, int col)
	{
		Vector res = new Vector();
		if(mat[row][col]!=0)
		{
		  return res;	
		}
		int sum = 0;
		res.add(0);
		for(int num=1; num<=N; num++)
		{           
		  if(isPerfect(mat, row, col, num))
           {
        	   //res.add(num);
			  res.add(1);
			  sum+=1;
           }
		  else
		  {
			  res.add(0);
		  }
		}
		res.set(0, sum);	
		return res; 	  
	}
	
	public static String pair_to_string(int row, int col)
	{
	   return String.valueOf(row) + String.valueOf(col);	   
	}
	
	public static Map<String, Vector> fill_map(int[][] mat)
	{
		Map<String, Vector> result = new HashMap<String, Vector>();
		for(int i=0; i<N; i++)
		{
			for(int j=0; j<N; j++)
			{
				Vector values = getValues2(mat, i, j);
				if(!values.isEmpty())
				{
					String key = pair_to_string(i, j);
					result.put(key, values);
				}				
			}
		}		
		return result;		
	}
	
 
	
	static void valuesSameRowColBox(int[][] matrix, int row, int col)
	{
		Map<String, Integer> result = new HashMap<String,Integer>();  
		//Vector values = new Vector();
		for(int i=0; i<N; i++)
		{
		   if(matrix[row][i]==0 && i!=col)
		   {
			   String pair = pair_to_string(row, i);
			   //values.addElement(pair);
			   result.put(pair, 0);
		   }
		}
		
		for(int i=0; i<N; i++)
		{
			if(matrix[i][col]==0 && i!=row)
			{
				String pair = pair_to_string(i, col);
				//values.addElement(pair);
				result.put(pair, 0);
			}
		}
				
		int beginRow = row - row%2 ;
		int beginCol = col - col%2;
		
		for(int i=beginRow; i<beginRow+2; i++)
		{
			for(int j=beginCol; j<beginCol+2; j++)
				if(matrix[i][j]==0 )
				{
					String pair = pair_to_string(i, j);
					//values.addElement(pair);
					result.put(pair, 0);
				}
		}
		Iterator it = result.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String) it.next();
			System.out.println(key);		
		}
		
		//return values;						
	}
	
	public static int[] searchFreeSpace(int[][] matrix)
	{
	   for(int i=0; i< N; i++)
	   {
		   for(int j=0; j<N; j++)
		   {
			   if(matrix[i][j] == 0)
			   {				  
				   int [] result = {i,j};				  
				   return result;  
			   }				   
		   }
	   }
	     int [] free = {-1,-1};
		return free;	
	}
	
	
	
	static void solveSudoku(int[][] matrix)
	{		
		int[] values =  searchFreeSpace(matrix);
		int row = values[0];
		int col = values[1];
		System.out.println(row  + " " +col);
		
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
	
	
	public static void main(String[] args) 
	{
	
		int [][] mat = {{0,0,0,0},{0,2,0,1},{4,0,1,0},{0,0,0,0}};
		//printM(mat, 4);
		
		valores_probables = fill_map(mat);
		//solveSudoku(mat);
		
		printMap(valores_probables);
		
		
		
		//valuesSameRowColBox(mat, 0, 0);
		
		
		
		//fill_map(mat);
		
		
		
	}

}
