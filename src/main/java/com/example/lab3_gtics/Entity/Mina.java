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
    private ArrayList<String> posiciones;
    private ArrayList<ArrayList<String>> minaGrande;
}