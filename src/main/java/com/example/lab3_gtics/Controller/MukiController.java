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

                                    if (matriz[k][h] == 20) {
                                        counter++;
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


        if(mina.getPosiciones()[fila][columna] ==30) {
            //No escoge bomba
            //Recorremos alrededor de la casilla escogida

            ArrayList<Integer> lista =new ArrayList<Integer>();
            lista=mina.minar(mina, matriz, fila, columna);
            ArrayList<Integer> aux= new ArrayList<>();
            while(!lista.isEmpty()){
                fila=lista.remove(0);
                columna=lista.remove(0);
                System.out.println("fila : " + fila);
                System.out.println("columna : " + columna);
                aux = mina.minar(mina,matriz,fila,columna);
                if(!aux.isEmpty()) {
                    ArrayList<String> listaCoordenadas = new ArrayList<>();
                    for (int o = 1; o <= lista.size() / 2; o++) {
                        String coordenadaGuardada = "" + lista.get(2 * o - 2) + lista.get(2 * o - 1);
                        listaCoordenadas.add(coordenadaGuardada);
                    }

                    for (int e = 1; e <= aux.size() / 2; e++) {
                        String coordenada = "" + aux.get(2 * e - 2) + aux.get(2 * e - 1);
                        int auxCounter = 0;
                        for(String uu : listaCoordenadas){
                            if(uu.equals(coordenada)){
                                auxCounter++;
                            }
                        }
                        if (auxCounter != 0) {
                            lista.add(aux.get(2 * e - 2));
                            lista.add(aux.get(2 * e - 1));
                        }
                    }
                }
            }

/*
                for(int j=1; j<=lista.size()/2 ; j++){
                    int filaFila = lista.get(j*2-2);
                    int columnaColumna = lista.get(j*2-1);
                    listaLista.add(mina.minar(mina,matriz,filaFila,columnaColumna));
                }

                    ArrayList<ArrayList<Integer>> prime=  new ArrayList<>();
                    for (ArrayList<Integer> l : listaLista) {
                        for (int j = 1; j <= l.size() / 2; j++) {
                            int filaFila = l.get(j * 2 - 2);
                            int columnaColumna = l.get(j*2 - 1);
                            prime.add(mina.minar(mina, matriz, filaFila, columnaColumna));
                        }
                    }
*/


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
        } else if (mina.getPosiciones()[fila][columna] ==20  ) {
            //Escoge bomba
            matriz[fila][columna] = matriz[fila][columna]*(-1);
            mina.setPosiciones(matriz);
            mina.setFallas(mina.getFallas()+1);
        }else{
            matriz[fila][columna] = matriz[fila][columna] *(-1);
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
        }
        return "juego";
    }


}

