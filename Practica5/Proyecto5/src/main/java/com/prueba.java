package com;

import java.util.ArrayList;

public class prueba {
    public static void main(String[] args) {
        ArrayList<String> frutas = new ArrayList<>();
        frutas.add("sandia");
        frutas.add("melon");
        frutas.add("guayaba");

        if(frutas.contains("guayaba")){
            System.out.println("En el arreglo hay una guayaba");
        }
        

    }
}
