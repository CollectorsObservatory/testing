/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;

/**
 *
 * @author ziedd 
 */

public class Repere {
    // Ajout final pour indiquer que c'est des constantes
    private static final float PIXELS_PAR_MM = 3.78f; // Conversion de mm en pixels
    private static final float MM_PAR_POUCE = 25.4f;  // Conversion de pouces en mm
    
    // Méthode pour convertir une longueur de mm en pixels
    public int convertirEnPixels(double valeurEnMm) {
        if (valeurEnMm < 0) {
            throw new IllegalArgumentException("La valeur en millimètres doit être positive.");
        }
        return (int) (valeurEnMm * PIXELS_PAR_MM);
    }

    // Méthode pour convertir des pixels en mm
    public float convertirEnMm(float valeurEnPixels) {
        if (valeurEnPixels < 0) {
            throw new IllegalArgumentException("La valeur en pixels doit être positive.");
        }
        return valeurEnPixels / PIXELS_PAR_MM;
    }

    // Méthode pour convertir des mm en pouces
    public double convertirEnPouces(double valeurEnMm) {
        if (valeurEnMm < 0) {
            throw new IllegalArgumentException("La valeur en millimètres doit être positive.");
        }
        return valeurEnMm / MM_PAR_POUCE;
    }

    // Méthode pour convertir des pouces en mm
    public double convertirEnMmDepuisPouces(double valeurEnPouces) {
        if (valeurEnPouces < 0) {
            throw new IllegalArgumentException("La valeur en pouces doit être positive.");
        }
        return valeurEnPouces * MM_PAR_POUCE;
    }
}
