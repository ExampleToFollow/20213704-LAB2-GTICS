package com.example.lab3_gtics.Controller;

import com.example.lab3_gtics.Entity.Mina;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
@SessionAttributes("mina")
@Controller
public class MukiController {
    @GetMapping("/buscaminas")
    public String nuevo()
    {
        return "FormularioInicio";
    }

    @PostMapping("/jugar")
    public String guardar(
            @RequestParam("filas") String filas,
            @RequestParam("columnas") String columnas,
            @RequestParam("intentos") String intentos,
            @RequestParam("numBombas") String numBombas,
            @RequestParam("posiciones") String posiciones,
            Model model
    ) {
        System.out.println(filas + "  " + columnas + " " + posiciones +  intentos + numBombas );

       Mina mina =  new Mina();
        mina.setNumFilas(Integer.parseInt(filas));
        mina.setNumColumnas(Integer.parseInt(columnas));
        mina.setNumIntentos(Integer.parseInt(intentos));
        mina.setNumBombas(Integer.parseInt(numBombas));
        //Debemos sacar las coordenadas
        char caracter;
        ArrayList<Integer> listaNum=  new ArrayList<>();
        for(int i=0 ; i< posiciones.length() ; i++){
            caracter = posiciones.charAt(i);
            try{
                listaNum.add(Integer.parseInt(String.valueOf(caracter))-1);
                //System.out.println(caracter);
            }catch(NumberFormatException e) {
                continue;
            }
        }
        //Ahora debemos asignar esos valores a las posiciones , en este caso propondremos dos estados como números para las celdas de la matriz
        //los números corresponderán a la cantidad de bombas que estarán a su alrededor
        int [][] matriz =  new int[mina.getNumFilas()][mina.getNumColumnas()];
        for(int i = 1;  i<=(listaNum.toArray().length)/2 ; i++){
            matriz[listaNum.get(2*i-2)][listaNum.get(2*i-1)] = 20;
        }

        int counter= 0;


        for(int i=0;  i<mina.getNumFilas(); i++){
            for(int j=0;  j<mina.getNumColumnas(); j++){
                if(matriz[i][j]!=20) {
                    counter = 0;
                    //Recorremos elementos contiguos
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int h = j - 1; h <= j + 1; h++) {
                            if ((k >= 0 && k <= mina.getNumFilas() - 1) && (h >= 0 && h <= mina.getNumColumnas() - 1) ) {
                                if (h != i || k !=j) {
                                    if (matriz[k][h] == 20) {
                                        counter++;
                                    }
                                }
                            }
                        }
                    }
                    matriz[i][j] = counter;
                }

            }
        }

        for(int i = 0 ;i<mina.getNumFilas() ; i++){
            for(int j= 0;j<mina.getNumColumnas() ; j++){
                if(matriz[i][j]==0){
                    matriz[i][j]= 30;
                }
            }
        }
        mina.setPosiciones(matriz);

        //Ahora seteamos estados como string
        String[][] matrizEstado = new String[mina.getNumFilas()][mina.getNumColumnas()];
        for(int i=0;  i<mina.getNumFilas(); i++){
            for(int j=0;  j<mina.getNumColumnas(); j++) {
                matrizEstado[i][j] = "Oculto";
            }
        }
        mina.setEstadoMina(matrizEstado);

        model.addAttribute("mina",mina);
        return "juego";
    }

    @PostMapping("/minar")
    public String minar(@RequestParam("posicion") String posicion,
                        @ModelAttribute("mina") Mina mina
                        ) {
        char caracter;
        ArrayList<Integer> listaNum=  new ArrayList<>();
        for(int i=0 ; i< posicion.length() ; i++){
            caracter = posicion.charAt(i);
            try{
                listaNum.add(Integer.parseInt(String.valueOf(caracter))-1);
            }catch(NumberFormatException e) {
                continue;
            }
        }


        int fila = listaNum.get(0);
        int columna = listaNum.get(1);
        int[][] matriz = mina.getPosiciones();

        if(mina.getPosiciones()[fila][columna] !=20 ) {
            //No escoge bomba
            //Recorremos alrededor de la casilla escogida
            matriz[fila][columna]= matriz[fila][columna]*(-1);
            for(int i= fila-1 ; i<=fila+1; i++ ){
                for(int j= columna-1 ; j<=columna+1 ; j++){
                    //Restriccion para los bordes de la matriz
                    if((i>=0 && i<= mina.getNumFilas()-1) && (j>=0 && j<=mina.getNumColumnas()-1) && (i!=fila || j!=columna)){
                        //Restriccion para detectar otra bomba
                        if(matriz[i][j] >0 && matriz[i][j] !=20 ){
                            matriz[i][j] =matriz[i][j]*(-1);
                        }
                    }
                }
            }


            mina.setPosiciones(matriz);

            //Verificacion por si es que gano
            int celdasDescubiertas= 0;
            int celdasOcultas= 0;

            for(int i =0;i<mina.getNumFilas() ; i++){
                for(int j =0 ; j<mina.getNumColumnas(); j++){
                    if(matriz[i][j] < 0){
                        celdasDescubiertas++;
                    }
                    if(matriz[i][j] > 0){
                        celdasOcultas++;
                    }

                }
            }
            //Puede ganar si solo quedan celdas de bombas
            if(celdasOcultas<=mina.getNumBombas()  && mina.getFallas()<mina.getNumIntentos()){
                mina.setGano(true);
            }
        }else{
            //Escoge bomba
            matriz[fila][columna] = matriz[fila][columna]*(-1);
            mina.setPosiciones(matriz);
            mina.setFallas(mina.getFallas()+1);
        }

        //LLAMEN A DIOS
        return "juego";
    }


}
