package com.example.lab3_gtics.Entity;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Mina {
    private int numFilas ;
    private int numColumnas;
    private int numIntentos;
    private int numBombas;
    private int [][] posiciones;
    private String[][] estadoMina;
    private int fallas = 0;
    private Boolean gano= false;


    public ArrayList<Integer> minar(Mina mina, int [][] matrix , int fila , int columna ){
        ArrayList<Integer> nuevosMinados = new ArrayList<>();
        matrix[fila][columna]= matrix[fila][columna]*(-1);
        for(int i= fila-1 ; i<=fila+1; i++ ){
            for(int j= columna-1 ; j<=columna+1 ; j++){
                //Restriccion para los bordes de la matriz
                if((i>=0 && i<= mina.getNumFilas()-1) && (j>=0 && j<=mina.getNumColumnas()-1) && (i!=fila || j!=columna)){
                    //Restriccion para detectar otra bomba
                    if(matrix[i][j] >0 && matrix[i][j] !=20 ){
                        if(matrix[i][j]==30) {
                            nuevosMinados.add(i);
                            nuevosMinados.add(j);
                        }
                        matrix[i][j] =matrix[i][j]*(-1);
                    }
                }
            }
        }
        return nuevosMinados;
    }
    public ArrayList<Integer> minar2(Mina mina, int [][] matrix , int fila , int columna ){
        ArrayList<Integer> nuevosMinados = new ArrayList<>();
        matrix[fila][columna]= matrix[fila][columna]*(-1);
        for(int i= fila-1 ; i<=fila+1; i++ ){
            for(int j= columna-1 ; j<=columna+1 ; j++){
                //Restriccion para los bordes de la matriz
                if((i>=0 && i<= mina.getNumFilas()-1) && (j>=0 && j<=mina.getNumColumnas()-1) && (i!=fila || j!=columna)){
                    //Restriccion para detectar otra bomba
                    if(matrix[i][j] >0 && matrix[i][j] !=20 ){
                        matrix[i][j] =matrix[i][j]*(-1);
                        nuevosMinados.add(i);
                        nuevosMinados.add(j);
                    }
                }
            }
        }
        return nuevosMinados;
    }
}
