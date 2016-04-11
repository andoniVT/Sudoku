package version1;

import java.io.IOException;
import java.util.Vector;

public class Sudoku 
{
	 static int N = 9;
	 int type;
	 String inputFile;
	 Vector matrices;
	
	public Sudoku(int type, String inputFile) throws IOException
	{
	   this.type = type;
	   this.inputFile = inputFile;
	   this.matrices = Utils.parseFile(this.inputFile , N);    
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
								
				if(solveSudoku(matrix))									
					return true;						
				matrix[row][col] = 0;				
			}
		}		
		return false;
	}
	
	public void solve()
	{
	   if (this.type==0)
	   {
		   System.out.println("Backtracking");
		   
		   for(int i=0; i<matrices.size(); i++)
		   {
			   int [][] matrix = (int[][]) matrices.get(i);
			   if(solveSudoku(matrix))
					Utils.printM(matrix,N);
				else
					System.out.println("Not solution found!");
			  
			   System.out.println("\n");
		   }		   		   		   		   		   
	   }
		   
	   if (this.type==1) System.out.println("FC");
	   if (this.type==2) System.out.println("FC and MVR");	   
	}
		
	
	public static void main(String[] args) throws IOException 
	{
		/*
		 * 0 -> Backtracking Simple
		 * 1 -> Backtracking with Forward Checking
		 * 2 -> Backtracking with Forward Checking and MVR
		 * */
		
		Sudoku test = new Sudoku(0, "entrada.txt");
		test.solve();
		
		
		
		
		
		
		
		
		
			
	}
}