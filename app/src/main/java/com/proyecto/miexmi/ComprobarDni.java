package com.proyecto.miexmi;

public class ComprobarDni {

    public static boolean validarDNI(String dni) {

        // Comprobar longitud
        if (dni == null || dni.length() != 9) return false;

        // Separar número y letra
        String numeros = dni.substring(0, 8);
        char letra = dni.charAt(8);

        // Comprobar que los números son válidos
        if (!numeros.matches("[0-9]+")) return false;

        // Tabla oficial
        String letras = "TRWAGMYFPDXBNJZSQVHLCKE";

        // Calcular letra correcta
        int num = Integer.parseInt(numeros);
        char letraCorrecta = letras.charAt(num % 23);

        // Comparar (mayúsculas)
        return Character.toUpperCase(letra) == letraCorrecta;
    }
}
