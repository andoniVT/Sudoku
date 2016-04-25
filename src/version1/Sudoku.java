package version1;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Vector;

public class Sudoku 
{
	 int N = 9; /// size of sudoku
	 int type;  /// type of heuristic to apply
	 String inputFile;    /// input file : entrada or entrada10
	 Vector matrices;   /// matrices gotten from input file
	 int counter;   /// number of attributions
	 
	 /*   map for represent probable values for each free cell of matrix
	  *   key : position (row,col) converted to string
	  *   value: first element of vector represent the number of probable values for key and the rest represent which are the probable values
	  * */
	 Map<String,Vector> probable_values; 
	 
	 
	 /*
	  *     Class constructor whose parameters are type of heuristic and input file
	  * 
	  * */
	public Sudoku(int type, String inputFile) throws IOException
	{
	   this.type = type;
	   this.inputFile = inputFile;
	   this.matrices = Utils.parseFile(this.inputFile , N);    
	   this.counter = 0;	   
	}
	
	
	 /*
	  *       Looks for first position of matrix which have 0 value      
	  * 
	  * */
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
	
	
	 /*
	  *  Verify if value is legal in the specified row
	  * 
	  * */
	public boolean isInRow(int[][] matrix, int row, int value)
	{
		for(int i=0; i<N; i++)		
			if(matrix[row][i]==value) return true;		
		return false;
	}

	
	 /*
	  *    Verify if value is legal in the specified col 
	  * 
	  * */
	public boolean isInCol(int[][] matrix, int col, int value)
	{
		for(int i=0; i<N; i++)
			if(matrix[i][col]==value) return true;
		return false;
	}
	
	
	 /*
	  *   Verify if value is legal in a box
	  * 
	  * */
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
	
	
	 /*
	  *     Verify if a value is legal in a row, col, box
	  * 
	  * */
	public boolean isPerfect(int[][] matrix, int row, int col, int value)
	{
		if (!isInRow(matrix, row, value) && !isInCol(matrix, col, value) && !isInBox(matrix, row, col, value))
			return true;
		return false;
	}
	
	
	 /*
	  *    Generate the vector of posible values for a specified position (row,col) of the matrix
	  * 
	  * */
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
	
	
	 /*
	  *  Generate the map which contents all of possible values of every free element of the matrix
	  * 
	  * */
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
		
	
	 /*
	  *   Verify if all of elements has at least one possible value
	  * 
	  * */
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
	
	
	 /*
	  *   Given a vector of keys(positions row-col) , a function removes possible values equal to value parameter 
	  * 
	  * */
	public Vector removePossibleValue(int value, Vector keys)  
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
	
	
	 /*
	  *   Given a vector of keys(positions row-col) , a function re-insert possible values equal to value parameter
	  * 
	  * */
	public void insertPossibleValue(int value, Vector keys) 
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
	
	
	 /*
	  *  get the incident values of a position (row,col) 
	  *  incident values are values in the same row, col ,box
	  * 
	  * */
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
	
	
	 /*
	  *   find the position (row,col) of the minimum value remanecente 
	  * 
	  * */
	public Vector findMVR(int[][] matrix)
	{
		Vector positions = new Vector();
		Iterator it = probable_values.keySet().iterator();
		int menor = 9999;
		String key_menor = null;		
		while(it.hasNext())
		{
			String key = (String) it.next();
			Vector position = Utils.string_to_pair(key);
			
			int row = (int) position.get(0);
			int col = (int) position.get(1);
			
			Vector values = probable_values.get(key);
			int value = (int) values.get(0);			
			if(value<menor && matrix[row][col]==0)
			{
				menor = value;
				key_menor = key;
			}
		}
		
		Vector position = Utils.string_to_pair(key_menor);
		int row = (int) position.get(0);
		int col = (int) position.get(1);
		positions.add(row);
		positions.add(col);					
		return positions;				
	}
	
	 /*
	  *     solve sudoku with Backtracking , Forward Checking or  MVR
	  * 
	  * */
	public boolean solveSudoku(int[][] matrix)
	{		
	
		int[] values = searchFreeSpace(matrix);
		if(values[0]==-1)
			return true;
				
		int row, col;
		if(this.type==0 || this.type==1) ///Backtracking or Forward Checking
		{
			row = values[0];
			col = values[1];				
		}
		else   /// MVR
		{
			Vector position = findMVR(matrix);
			row = (int) position.get(0);
		    col = (int) position.get(1);
		}				
				
		for(int i=1; i<=9; i++)
		{
			if(isPerfect(matrix, row, col, i))
			{
				matrix[row][col] = i;
				this.counter++;
				Vector incident_keys = new Vector();
				Vector keys_with_values_removed = new Vector();
				if(this.type!=0) /// Forward Checking or MVR
				{
					incident_keys = valuesSameRowColBox(matrix, row, col);
					keys_with_values_removed = removePossibleValue(i, incident_keys);
					boolean pass_forward_checking = allHavePossibleValues(this.probable_values);
					if(!pass_forward_checking)
					{		
						matrix[row][col] = 0;
						insertPossibleValue(i, keys_with_values_removed);
						continue;
					}
				}
								
				if(solveSudoku(matrix))								
					return true;
				if(this.type!=0)  /// Forward Checking or MVR
					insertPossibleValue(i, keys_with_values_removed);
				matrix[row][col] = 0;
			}
		}	
		return false;	
	}

	
	 /*
	  * 
	  * 
	  * */
	public void solve()
	{
	   Vector times = new Vector();
	   Vector asignations = new Vector();
	  for(int i=0; i<matrices.size(); i++)
	   {
		  int [][] matrix = (int[][]) matrices.get(i);		
		  long time_start, time_end;
		  time_start = System.currentTimeMillis();
		  if(this.type!=0)
			  this.probable_values = getAllPossibleValues(matrix);
		  if(solveSudoku(matrix))
		  {
			  Utils.printM(matrix,N);
			  System.out.println("");  
		  }
		  else
				System.out.println("Not solution found!");
		   time_end = System.currentTimeMillis();			   
		   times.add(time_end-time_start);			  
		   asignations.add(this.counter);			   
		   this.counter=0;		  		  		  
	   }
	  
	  /*
	  System.out.println("Asignations \n");
	  for(int i=0; i<asignations.size();i++)
		  System.out.println(asignations.get(i));
	  
	  System.out.println("Times \n");
	  for(int i=0; i<times.size();i++)
		  System.out.println(times.get(i));
	  */
	  
	}
		
	
	public static void main(String[] args) throws IOException 
	{
		/*
		 * 0 -> Backtracking Simple
		 * 1 -> Backtracking with Forward Checking
		 * 2 -> Backtracking with Forward Checking and MVR
		 * */
			
		String test_default = "entrada.txt",  test_10 = "entrada10.txt";
		Vector inputFiles = new Vector();
		inputFiles.add(test_default); inputFiles.add(test_10);
				
		Scanner reader = new Scanner(System.in);
		System.out.println("Enter a type of test (0->Backtracking, 1->Forward Checking, 2->MVR)");
		int type = reader.nextInt();
		System.out.println("Enter option for input test (0->90 tests,1->10 tests)");
		int input = reader.nextInt();		
		
		Sudoku test = new Sudoku(type, (String) inputFiles.get(input));
		test.solve();		
					
		System.out.println("\n");								
	}
}