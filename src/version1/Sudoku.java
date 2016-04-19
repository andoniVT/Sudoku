package version1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

public class Sudoku 
{
	 int N = 9;
	 int type;
	 String inputFile;
	 Vector matrices;
	 int counter;
	 Map<String,Vector> probable_values;
	
	public Sudoku(int type, String inputFile) throws IOException
	{
	   this.type = type;
	   this.inputFile = inputFile;
	   this.matrices = Utils.parseFile(this.inputFile , N);    
	   this.counter = 0;	   
	}
	
	public int[] searchFreeSpace(int[][] matrix)
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
	
	public boolean isInRow(int[][] matrix, int row, int value)
	{
		for(int i=0; i<N; i++)		
			if(matrix[row][i]==value) return true;		
		return false;
	}
	
	public boolean isInCol(int[][] matrix, int col, int value)
	{
		for(int i=0; i<N; i++)
			if(matrix[i][col]==value) return true;
		return false;
	}
	
	public boolean isInBox(int[][] matrix, int row, int col, int value)
	{
		int beginRow = row - row%3 ;
		int beginCol = col - col%3;
		
		for(int i=beginRow; i<beginRow+3; i++)
		{
			for(int j=beginCol; j<beginCol+3; j++)
				if(matrix[i][j]==value) return true;	
		}		
		return false;
	}
	
	public boolean isPerfect(int[][] matrix, int row, int col, int value)
	{
		if (!isInRow(matrix, row, value) && !isInCol(matrix, col, value) && !isInBox(matrix, row, col, value))
			return true;
		return false;
	}
	
	public Vector getPossibleValues(int [][] mat, int row, int col)
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
	
	public Map<String, Vector> getAllPossibleValues(int[][] mat)
	{
		Map<String, Vector> result = new HashMap<String, Vector>();
		for(int i=0; i<N; i++)
		{
			for(int j=0; j<N; j++)
			{
				Vector values = getPossibleValues(mat, i, j);
				if(!values.isEmpty())
				{
					String key = Utils.pair_to_string(i, j);
					result.put(key, values);
				}				
			}
		}		
		return result;		
	}
	
	public Vector getVectorRepresentation(Map<String, Vector> values , String key)
	{
		Vector result = new Vector();
	   if (values.containsKey(key))
	   {
		   Vector value = values.get(key);
		   for(int i=1; i<value.size(); i++)	    
			   if((Integer)value.get(i) != 0)		    
	               result.add(i);   
	   }			   			   		    	    	   	   
	   return result;	  
	}
	
	public boolean allHavePossibleValues(Map<String,Vector> values)
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
	
	public Vector removePossibleValue(int value, Vector keys)  ///// cuidado!!!
	{
		Vector keys_removed = new Vector();
		for(int i=0; i<keys.size(); i++)
		{
			String key = (String) keys.get(i);
			Vector vector = this.probable_values.get(key);
			if((int)vector.get(value)!=0)
			{
				vector.set(value, 0);
				int size = (int) vector.get(0);			
				 vector.set(0, size-1);
				 this.probable_values.put(key, vector);
				keys_removed.add(key);
			}									
		}
		return keys_removed;
	}
	
	public void insertPossibleValue(int value, Vector keys)  //// cuidado!!  Solo mandar los que fueron removidos
	{
		for(int i=0; i<keys.size(); i++)
		{
			String key = (String) keys.get(i);
			Vector vector = this.probable_values.get(key);
			vector.set(value, 1);
			int size = (int) vector.get(0);
			vector.set(0, size+1);
			this.probable_values.put(key, vector);
		}
	}
	
	public Vector valuesSameRowColBox(int[][] matrix, int row, int col)
	{		
		Map<String, Integer> result = new HashMap<String,Integer>();  		
		for(int i=0; i<N; i++)
		{
		   if(matrix[row][i]==0 && i!=col)
		   {
			   String pair = Utils.pair_to_string(row, i);		
			   result.put(pair, 0);
		   }
		}
		
		for(int i=0; i<N; i++)
		{
			if(matrix[i][col]==0 && i!=row)
			{
				String pair = Utils.pair_to_string(i, col);
				result.put(pair, 0);
			}
		}
				
		int beginRow = row - row%3 ;
		int beginCol = col - col%3;
		
		for(int i=beginRow; i<beginRow+3; i++)
		{
			for(int j=beginCol; j<beginCol+3; j++)
				if(matrix[i][j]==0 )
				{
					String pair = Utils.pair_to_string(i, j);
					result.put(pair, 0);
				}
		}
		
		String row_col = Utils.pair_to_string(row, col);
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
		
	public boolean solveSudoku(int[][] matrix)
	{
		int[] values =  searchFreeSpace(matrix);
		if(values[0]==-1)
			return true;
		int row = values[0];
		int col = values[1];
		
		for(int i=1; i<=N; i++)
		{
			if(isPerfect(matrix, row, col, i))
			{
				matrix[row][col] = i;
				this.counter++;
								
				if(solveSudoku(matrix))									
					return true;						
				matrix[row][col] = 0;				
			}
		}		
		return false;
	}
	
	public boolean solveSudokuFC(int[][] matrix)
	{
		int[] values =  searchFreeSpace(matrix);
		if(values[0]==-1)
			return true;
		
		int row = values[0];
		int col = values[1];
			
		String row_col = Utils.pair_to_string(row, col);		
		Vector vector = getVectorRepresentation(this.probable_values , row_col);
		
		for(int i=0; i<vector.size(); i++)
		{
			int value_to_insert = (int) vector.get(i);
			matrix[row][col] = value_to_insert;
			this.counter++;
			Vector incident_keys = valuesSameRowColBox(matrix, row, col);
			
			Vector keys_with_values_removed = removePossibleValue(value_to_insert, incident_keys);
			
			boolean pass_forward_checking = allHavePossibleValues(this.probable_values);
			if(!pass_forward_checking)
			{
				matrix[row][col] = 0;
				continue;
			}
			if(solveSudoku(matrix))
				return true;
			 matrix[row][col] = 0;	
			 insertPossibleValue(value_to_insert, keys_with_values_removed);
			
			
			/*
			if(pass_forward_checking)
			{
				if(solveSudoku(matrix))
					return true;
				 matrix[row][col] = 0;				
			}
			    insertPossibleValue(value_to_insert, keys_with_values_removed);
			    matrix[row][col] = 0;	*/		    							
		}		
		return false;
		
	}
	
	
	public void solve()
	{
	   for(int i=0; i<matrices.size(); i++)
	   {
		   int [][] matrix = (int[][]) matrices.get(i);
		   
		   if(this.type==0)
		   {
			   System.out.println("Backtracking");
			   long time_start, time_end;
			   time_start = System.currentTimeMillis();
			   if(solveSudoku(matrix))
			   {
				 Utils.printM(matrix,N);
				   System.out.println("");  
			   }
					
				else
					System.out.println("Not solution found!");
			   time_end = System.currentTimeMillis();
			   System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
			   System.out.println(this.counter);
			   System.out.println("\n");
			   
		   }
		   if(this.type==1)
		   {
			   System.out.println("Forward Checking");
			   long time_start, time_end;
			   time_start = System.currentTimeMillis();
			   this.probable_values = getAllPossibleValues(matrix);
			   if(solveSudokuFC(matrix))
			   {
				 Utils.printM(matrix, N);
				   System.out.println(""); 
			   }
				   
			   else
				   System.out.println("Not solution found!");
			   time_end = System.currentTimeMillis();
			   System.out.println("the task has taken "+ ( time_end - time_start ) +" milliseconds");
			   System.out.println(this.counter);
			   System.out.println("\n");
			   
			   
		   }
		   if(this.type==2)
		   {
			   System.out.println("FC and MVR");
		   }		   		   		   
	   }
		
	}
		
	
	public static void main(String[] args) throws IOException 
	{
		/*
		 * 0 -> Backtracking Simple
		 * 1 -> Backtracking with Forward Checking
		 * 2 -> Backtracking with Forward Checking and MVR
		 * */
		
		Sudoku test = new Sudoku(0, "entrada10.txt");
		Sudoku test2 = new Sudoku(1, "entrada10.txt");
		test.solve();
		test2.solve();
		
		System.out.println("\n \n");
				
		
			
	}
}