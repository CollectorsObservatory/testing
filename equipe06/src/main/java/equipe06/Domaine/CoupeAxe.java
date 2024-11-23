/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;
import equipe06.Domaine.Utils.ElementCoupe;
import equipe06.Domaine.Coupe;
/**
 *
 * @author katia
 */
public class CoupeAxe extends Coupe {

    // Attributs spécifiques à CoupeAxe
    private float axe;
    private boolean composante;


    // Implémentation de la méthode abstraite coupe
    public CoupeAxe(ElementCoupe e) {
        super(e);
        assert e != null : "L'element de coupe est invalide.";
        assert e.getAxe() >= 0 : "La valeur de l'axe doit être superieure ou egale a zero.";
        
        this.axe = e.getAxe();
        this.composante = e.getComposante();
    }

    // Getter et Setter pour Axe
    public float getAxe() {
        return axe;
    }

    public void setAxe(float axe) {
         if (axe < 0) {
            throw new IllegalArgumentException("La valeur de l'axe doit etre superieure ou egale a zero.");
        }
        this.axe = axe;
    }

    // Getter et Setter pour Composante
    public boolean getComposante() {

        return composante;
    }

    public void setComposante(boolean composante) {

        this.composante = composante;
    }

}

