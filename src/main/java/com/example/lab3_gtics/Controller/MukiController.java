package com.example.lab3_gtics.Controller;

import com.example.lab3_gtics.Entity.Mina;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

        /*
        for(int i=0;  i<mina.getNumFilas(); i++){
            for(int j=0;  j<mina.getNumColumnas(); j++) {
                System.out.println(matriz[i][j]);
            }
        }*/

        int counter= 0;
        for(int i=0;  i<mina.getNumFilas(); i++){
            for(int j=0;  j<mina.getNumColumnas(); j++){
                if(matriz[i][j]!=20) {
                    counter = 0;
                    //Recorremos elementos contiguos
                    for (int k = i - 1; k <= i + 1; k++) {
                        for (int h = j - 1; h <= j + 1; h++) {
                            if ((k >= 0 && k <= mina.getNumFilas() - 1) && (h >= 0 && h <= mina.getNumFilas() - 1)) {
                                if (h != k) {
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
        mina.setPosiciones(matriz);

        //Ahora seteamos estados como string
        String[][] matrizEstado = new String[mina.getNumFilas()][mina.getNumColumnas()];
        for(int i=0;  i<mina.getNumFilas(); i++){
            for(int j=0;  j<mina.getNumColumnas(); j++) {
                matrizEstado[i][j] = "Oculto";
            }
        }
        mina.setEstadoMina(matrizEstado);

        /*
        for(int i=0;  i<mina.getNumFilas(); i++){
            for(int j=0;  j<mina.getNumColumnas(); j++) {
                System.out.println(matriz[i][j]);
            }
        }
*/

        model.addAttribute("mina",mina);
        return "juego";
    }

    @PostMapping("/minar")
    public String minar() {
        //LLAMEN A DIOS
        return "juego";
    }


}
