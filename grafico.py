
import pandas as pd 
import matplotlib.pyplot as plt
import numpy as np 

def convert(vector):
	nuevo = []
	for i in vector:
		nuevo.append(int(i))
	return nuevo



def read():
	fileName = "datos.txt"
	file = open(fileName, 'r')
	matrix = []
	for i in file:
		i = i.rstrip("\n")
		i = " ".join(i.split())
		matrix.append(convert(i.split()) ) 
	return matrix



'''
plt.plot([1,2,3,4,5], [1674,725,25,118,23365], 'ro')
plt.plot([1,2,3,4,5], [1960,678,16,90, 24825], 'go')
plt.plot([1,2,3,4,5], [1423,512,9,393, 16715], 'bo')
plt.axis([0, 6, 0, 30000])
plt.show()
'''

if __name__ == '__main__':
	read()

	vector_b = []
	vector_fc = []
	vector_mvr = []
	results = []

	matrix = read()
	for i in range(len(matrix)):
		vector = matrix[i]		
		vector_b.append(vector[0]) 
		vector_fc.append(vector[1]) 
		vector_mvr.append(vector[2]) 


	columnas = [x for x in range(95)]

	vector_b = np.sort(vector_b)
	vector_fc = np.sort(vector_fc)
	vector_mvr = np.sort(vector_mvr)
	val = max(vector_mvr)


	plt.plot(columnas, vector_b, 'ro')
	plt.plot(columnas, vector_fc, 'go')
	plt.plot(columnas, vector_mvr, 'bo')
	#plt.ticklabel_format(style='sci', axis='y', scilimits=(0,0))
	plt.axis([0, 95, 0, val])
	
	plt.show()






	


