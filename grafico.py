
import pandas as pd 
import matplotlib.pyplot as plt
import numpy as np 

def convert(vector):
	nuevo = []
	for i in vector:
		nuevo.append(int(i))
	return nuevo



def read():
	fileName = "asignaciones.txt"
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
	max_value = max([max(vector_mvr) , max(vector_b) , max(vector_mvr)])
	 
	


	fig = plt.figure(figsize=(15, 8), dpi=80)
	ax = plt.subplot(111)
	#l1, =plt.plot(columnas, vector_b, 'ro' , label='B')
	#l2,=plt.plot(columnas, vector_fc, 'go' , label='FC')
	#l3,=plt.plot(columnas, vector_mvr, 'bo' , label='MVR')
	ax.plot(columnas, vector_b, 'ro' , label='B')
	ax.plot(columnas, vector_fc, 'go' , label='FC')
	ax.plot(columnas, vector_mvr, 'bo' , label='MVR')


	#plt.legend(handles=[l1, l2, l3])
	ax.legend(bbox_to_anchor=(1.1, 1.05))
	#plt.ticklabel_format(style='sci', axis='y', scilimits=(0,0))
	plt.yscale('log')	
	plt.xscale('linear')
	#plt.xscale('log')	
	plt.axis([0, 95, 0, max_value])
	
	plt.show()






	


