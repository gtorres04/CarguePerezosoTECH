package com.tech.web.prueba.support;

public class Utilidades {
	private Utilidades(){}
	
	/**
     * Este método ordena en forma ascendente el arreglo pasado como argumento usando el
     * algoritmo de selección.
     * 
     * @param arreglo el arreglo que sera ordenado.
     */
    public static void ordenarAscendentemente(int[] arreglo) {
        //iteramos sobre los elementos del arreglo
        for (int i = 0 ; i < arreglo.length - 1 ; i++) {
            int min = i;
 
            //buscamos el menor número
            for (int j = i + 1 ; j < arreglo.length ; j++) {
                if (arreglo[j] < arreglo[min]) {
                    min = j;    //encontramos el menor número
                }
            }
 
            if (i != min) {
                //permutamos los valores
                int aux = arreglo[i];
                arreglo[i] = arreglo[min];
                arreglo[min] = aux;
            }
        }
    }
 
    /**
     * Este método ordena en forma descendente el arreglo pasado como argumento usando el
     * algoritmo de selección.
     * 
     * @param arreglo el arreglo que sera ordenado.
     */
    public static void ordenarDescendentemente(int[] arreglo) {
        //iteramos sobre los elementos del arreglo
        for (int i = 0 ; i < arreglo.length - 1 ; i++) {
            int max = i;
 
            //buscamos el mayor número
            for (int j = i + 1 ; j < arreglo.length ; j++) {
                if (arreglo[j] > arreglo[max]) {
                    max = j;    //encontramos el mayor número
                }
            }
 
            if (i != max) {
                //permutamos los valores
                int aux = arreglo[i];
                arreglo[i] = arreglo[max];
                arreglo[max] = aux;
            }
        }
    }
	
}
