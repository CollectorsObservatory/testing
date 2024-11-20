/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine.Utils;

/**
 *
 * @author ziedd
 */
public class ZoneInterdite {
    
    private float longueur;
    private float largeur;
    private float distanceToX;
    private float distanceToY;

    // Constructeur
    public ZoneInterdite(float longueur, float largeur, float distanceToX, float distanceToY) {
        if (longueur <= 0) {
            throw new IllegalArgumentException("La longueur doit etre superieure a zero.");
        }
        if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur doit etre superieure a zero.");
        }
        if (distanceToX < 0) {
            throw new IllegalArgumentException("La distance par rapport à l'axe X doit etre non negative.");
        }
        if (distanceToY < 0) {
            throw new IllegalArgumentException("La distance par rapport à l'axe Y doit etre non negative.");
        }
        this.longueur = longueur;
        this.largeur = largeur;
        this.distanceToX = distanceToX;
        this.distanceToY = distanceToY;
    }

    // Getters et setters pour les attributs
    public float getLongueur() {
        return longueur;
    }

    public void setLongueur(float longueur) {
        if (longueur <= 0) {
            throw new IllegalArgumentException("La longueur doit etre superieure a zero.");
        }
        this.longueur = longueur;
    }

    public float getLargeur() {
        return largeur;
    }

    public void setLargeur(float largeur) {
        if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur doit etre superieure a zero.");
        }
        this.largeur = largeur;
    }

    public float getDistanceToX() {
        return distanceToX;
    }

    public void setDistanceToX(float distanceToX) {
        if (distanceToX < 0) {
            throw new IllegalArgumentException("La distance par rapport à l'axe X doit etre non negative.");
        }
        this.distanceToX = distanceToX;
    }

    public float getDistanceToY() {
        return distanceToY;
    }

    public void setDistanceToY(float distanceToY) {
        if (distanceToY < 0) {
            throw new IllegalArgumentException("La distance par rapport à l'axe Y doit etre non negative.");
        }
        this.distanceToY = distanceToY;
    }

    // Méthode pour modifier la zone interdite
    public void modifierZone(float longueur, float largeur, float distanceToX, float distanceToY) {
        if (longueur <= 0) {
            throw new IllegalArgumentException("La longueur doit etre superieure a zero.");
        }
        if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur doit etre superieure a zero.");
        }
        if (distanceToX < 0) {
            throw new IllegalArgumentException("La distance par rapport à l'axe X doit etre non negative.");
        }
        if (distanceToY < 0) {
            throw new IllegalArgumentException("La distance par rapport à l'axe Y doit etre non negative.");
        }
        this.longueur = longueur;
        this.largeur = largeur;
        this.distanceToX = distanceToX;
        this.distanceToY = distanceToY;
    }
    
}
