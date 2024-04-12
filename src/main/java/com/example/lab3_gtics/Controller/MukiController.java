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
    public String nuevo() {
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
        /*System.out.println("nombre recibido = " + nombre);
        */
       Mina mina =  new Mina();
        mina.setNumFilas(Integer.parseInt(filas));
        mina.setNumColumnas(Integer.parseInt(columnas));
        mina.setNumIntentos(Integer.parseInt(intentos));
        mina.setNumBombas(Integer.parseInt(numBombas));

        //(1,2) (2,3) (1,3)
        String[] position = "\\s".split(posiciones);
        int filaBomba ;
        ArrayList<String> lista = new ArrayList<String>();
        for(String i: position){
            lista.add((String.valueOf(i.charAt(1) + i.charAt(3))));
        }
        mina.setPosiciones(lista);
        ArrayList<ArrayList<String>> minaGrande ;
        int counterLista = 1;
        int fila = 0 ;
        int columna = 0 ;
        while(fila<=(mina.getNumFilas()-1)){
            while (columna<=(mina.getNumColumnas())-1){
                if(Integer.parseInt()){

                }
                columna++;
            }
            fila++;
        }


        model.addAttribute("mina",mina);
        return "juego";
    }

    @PostMapping("/minar")
    public String minar() {
        return "juego";
    }


}
