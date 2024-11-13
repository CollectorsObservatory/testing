/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;
import java.util.UUID;
import java.awt.Point;
import equipe06.Domaine.Utils.ZoneInterdite;
/**
 *
 * @author ziedd
 */
public class Panneau {
   
    private float longueur;
    private float largeur;
    private float profondeur;
    private ZoneInterdite zoneInterdite;
    private UUID Uuid;

    // Constructeur
    public Panneau(float longueur, float largeur, float profondeur) {
        if (longueur < 0) {
            throw new IllegalArgumentException("La longueur du panneau doit etre superieure a zero.");
        }
        if (largeur < 0) {
            throw new IllegalArgumentException("La largeur du panneau doit etre superieure a zero.");
        }
        if (profondeur < 0) {
            throw new IllegalArgumentException("La profondeur du panneau doit être superieure a zero.");
        }

        this.longueur = longueur;
        this.largeur = largeur;
        this.profondeur = profondeur;
        //this.Uuid = UUID.randomUUID();
    }

    // Getters pour les dimensions
    public float getLongueur() { return longueur; }
    public float getLargeur() { return largeur; }
    public float getProfondeur() { return profondeur; }  
    public UUID getUUID() { return Uuid; }

    // setters pour les dimensions
    public void setLongueur(float longueur) {
        if (longueur <= 0) {
             throw new IllegalArgumentException("La longueur du panneau doit etre superieure a zero.");
        }
        this.longueur = longueur;
    }
    public void setLargeur(float largeur) {
         if (largeur <= 0) {
            throw new IllegalArgumentException("La largeur du panneau doit etre superieure a zero.");
        }
        this.largeur = largeur;
    }
    public void setProfondeur(float profondeur) {
         if (profondeur <= 0) {
            throw new IllegalArgumentException("La profondeur du panneau doit être superieure a zero.");
        }
        this.profondeur = profondeur;
    }

    // Gestion de la zone interdite
    public ZoneInterdite getZoneInterdite() {
        return zoneInterdite;
    }
    public void setZoneInterdite(ZoneInterdite zoneInterdite) {
        if (zoneInterdite == null) {
            throw new IllegalArgumentException("La zone interdite ne peut pas etre invalide.");
        }
        this.zoneInterdite = zoneInterdite;
    }

    public void ajouterZoneInterdite(ZoneInterdite zoneInterdite) {
         if (zoneInterdite == null) {
            throw new IllegalArgumentException("La zone interdite ne peut pas etre invalide.");
        }
        this.zoneInterdite = zoneInterdite;
    }

    public void supprimerZoneInterdite() {
        this.zoneInterdite = null;
    }
    //la fonction qui verifie si on est a l'interieur de panneau ou pas
     public boolean inPanneau(Point p, Panneau panneau)
{
        assert p != null : "Le point ne peut pas etre invalide.";
        assert panneau != null : "Le panneau ne peut pas etre invalide.";
        int minX = 130;
        int maxX = (int) panneau.getLargeur() + 130;
        int minY = 0;
        int maxY = (int) panneau.getLongueur() - 130;
        return (p.x >= minX && p.x <= maxX) && ((1500 - p.y) >= minY && (1500 - p.y) <= maxY);
    }
}
 