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
			for(int j=beginCol; j<beginCol+2; j++)
				if(matrix[i][j]==value) return true;					
		return false;
	}
	
	static boolean isPerfect(int[][] matrix, int row, int col, int value)
	{
		if (!isInRow(matrix, row, value) && !isInCol(matrix, col, value) && !isInBox(matrix, row, col, value))
			return true;
		return false;
	}
	
	public static String pair_to_string(int row, int col)
	{
	   return String.valueOf(row) + String.valueOf(col);	   
	}
	
	public static Vector getPossibleValues(int [][] mat, int row, int col)
	{
		Vector res = new Vector();
		if(mat[row][col]!=0)		
		  return res;	
		
		int sum = 0;
		res.add(0);
		
		for(int num=1; num<=N; num++)
		{           
		  if(isPerfect(mat, row, col, num))
           {        	
			  res.add(1);
			  sum+=1;
           }
		  else		  
			  res.add(0);		  
		}
		res.set(0, sum);	
		return res; 	  
	}
		
	public static Map<String, Vector> getAllPossibleValues(int[][] mat)
	{
		Map<String, Vector> result = new HashMap<String, Vector>();
		for(int i=0; i<N; i++)
		{
			for(int j=0; j<N; j++)
			{
				Vector values = getPossibleValues(mat, i, j);
				if(!values.isEmpty())
				{
					String key = pair_to_string(i, j);
					result.put(key, values);
				}				
			}
		}		
		return result;		
	}
	
	public static Vector getVectorRepresentation(Map<String, Vector> values , String key)
	{
	   Vector result = new Vector();
	   Vector value = values.get(key);
	   for(int i=1; i<value.size(); i++)	    
		   if((Integer)value.get(i) != 0)		    
               result.add(i);			   		    	    	   	   
	   return result;	  
	}
	
	public static boolean allHavePossibleValues(Map<String,Vector> values)
	{
		Iterator it = values.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String) it.next();
			Vector value = values.get(key);
			if ((Integer)value.get(0) == 0)
				return false;
		}
		return true;
	}
	
	public static Vector removePossibleValue(int value, Vector keys)  ///// cuidado!!!
	{
		Vector keys_removed = new Vector();
		for(int i=0; i<keys.size(); i++)
		{
			String key = (String) keys.get(i);
			Vector vector = valores_probables.get(key);
			if((int)vector.get(value)!=0)
			{
				vector.set(value, 0);
				int size = (int) vector.get(0);			
				vector.set(0, size-1);
				valores_probables.put(key, vector);
				keys_removed.add(key);
			}									
		}
		return keys_removed;
	}
	
	public static void insertPossibleValue(int value, Vector keys)  //// cuidado!!  Solo mandar los que fueron removidos
	{
		for(int i=0; i<keys.size(); i++)
		{
			String key = (String) keys.get(i);
			Vector vector = valores_probables.get(key);
			vector.set(value, 1);
			int size = (int) vector.get(0);
			vector.set(0, size+1);
			valores_probables.put(key, vector);
		}
	}
	
	
	
	static Vector valuesSameRowColBox(int[][] matrix, int row, int col)
	{		
		Map<String, Integer> result = new HashMap<String,Integer>();  		
		for(int i=0; i<N; i++)
		{
		   if(matrix[row][i]==0 && i!=col)
		   {
			   String pair = pair_to_string(row, i);		
			   result.put(pair, 0);
		   }
		}
		
		for(int i=0; i<N; i++)
		{
			if(matrix[i][col]==0 && i!=row)
			{
				String pair = pair_to_string(i, col);
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
					result.put(pair, 0);
				}
		}
		
		String row_col = pair_to_string(row, col);
		result.remove(row_col);
		
		Vector values = new Vector();
		
		Iterator it = result.keySet().iterator();
		while(it.hasNext())
		{
			String key = (String) it.next();
			values.add(key);
		}		
		return values;						
	}
		
	
	static boolean solveSudoku(int[][] matrix)
	{		
		int[] values =  searchFreeSpace(matrix);
		int row = values[0];
		int col = values[1];
		String row_col = pair_to_string(row, col);		
		Vector vector = getVectorRepresentation(valores_probables , row_col);
		
		//System.out.println(row_col);
		//System.out.println(vector);
		
		printM(matrix, 4);
		System.out.println("\n");
		
		
		for(int i=0; i<vector.size(); i++)
		{
			int value_to_insert = (int) vector.get(i);
			matrix[row][col] = value_to_insert;
			//Vector incident_keys = new Vector(); // llamar a funcion q devuelve los keys indicentes a row,col
			Vector incident_keys = valuesSameRowColBox(matrix, row, col);
			
			Vector keys_with_values_removed = removePossibleValue(value_to_insert, incident_keys);
			
			boolean pass_forward_checking = allHavePossibleValues(valores_probables);
			if(pass_forward_checking)
			{
				if(solveSudoku(matrix))
					return true;
				 matrix[row][col] = 0;				
			}
			else
			{
			    insertPossibleValue(value_to_insert, keys_with_values_removed);
			    matrix[row][col] = 0;
			}					
		}
		
		return false;		
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
		printM(mat, 4);
		
		valores_probables = getAllPossibleValues(mat);
		
		if (solveSudoku(mat))
		{
			printM(mat, 4);
		}
		else
		{
			System.out.println("=(");
		}
		
		
		
		
		//removePossibleValue(3, keys);
		//insertPossibleValue(4, keys);		
		//printMap(valores_probables);		
		
		
		
		
		
		//System.out.println(allHavePossibleValues(valores_probables));
		
				
		
		
		//System.out.println(valuesSameRowColBox(mat, 0, 0));
		
				
		
	}

}
