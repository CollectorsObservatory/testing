/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;

import java.awt.*;

/**
 *
 * @author ziedd 
 */

public  class Repere {
    private final float PPI; // = 6; // Pixels par pouce, à ajuster selon l'écran
    private final float PPM ; // = PPI / 25.4f; // Pixels par millimètre
    private static Repere instance = null;
    public Point myPoint;
    
    private Repere() {
        this.PPI = 6; // Valeur par défaut
        this.PPM = PPI / 25.4f;
    }
    
    public static Repere getInstance() {
        if (instance == null) {
            instance = new Repere();
        }
        return instance;
    }
    
    
    public int convertirEnPixelsDepuisMm(double valeurEnMm) {
        if (valeurEnMm < 0) {
            throw new IllegalArgumentException("La valeur en millimètres doit être positive.");
        }
        return (int) (valeurEnMm * PPM);
    }
    public float convertirEnPixelsDepuisPouces (float valeurEnPouces){
        if (valeurEnPouces < 0) {
            throw new IllegalArgumentException("La valeur en pouces doit être positive.");
        }
        return valeurEnPouces * PPI;
    }
    public float convertirEnMmDepuisPixels(float valeurEnPixels) {
        if (valeurEnPixels < 0) {
            throw new IllegalArgumentException("La valeur en pixels doit être positive.");
        }
        return valeurEnPixels / PPM;
    }
    public float convertirEnUnitesDepuisPixels(float valeurEnPixels) {
        return valeurEnPixels / PPM;
    }

    public void savePoint(Point p) {
        myPoint = p;
    }
}
