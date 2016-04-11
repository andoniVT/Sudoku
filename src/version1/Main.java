package version1;

import java.io.IOException;
import java.util.Vector;
import version1.Utils;

public class Main 
{
	static int N = 9;
	
	static int[] searchFreeSpace(int[][] matrix)
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
		int beginRow = row - row%3 ;
		int beginCol = col - col%3;
		
		for(int i=beginRow; i<beginRow+3; i++)
		{
			for(int j=beginCol; j<beginCol+3; j++)
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
	
	static boolean solveSudoku(int[][] matrix, int count)
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
				count+=1;
				
				if(solveSudoku(matrix, count))									
					return true;
								
				matrix[row][col] = 0;
				count+=1;
			}
		}		
		return false;
	}
	
	

	public static void main(String[] args) throws IOException 
	{
		/*
		Vector matrices = Utils.parseFile("entrada.txt");
		
		int[][] mat =  (int[][]) matrices.get(2);
		Utils.printM(mat);
		
		System.out.println("");
		
		if(solveSudoku(mat, 0))
			Utils.printM(mat);
		else
			System.out.println("waaaaaaa");*/
		
		
		
		
		
		

		
		
	  


	}

}
