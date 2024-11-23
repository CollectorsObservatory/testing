/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;
import equipe06.Domaine.Utils.ElementCoupe;

import java.util.UUID;

/**
 *
 * @author katia
 */
public abstract class Coupe {

    // Attributs privés
    //private Outils outils;
    private float marge_profondeur = 0.5f;
    private float Profondeur;
    private Outil outil;
    public UUID uuid;
    private String typeCoupe;

    public Coupe(ElementCoupe e){
        
        assert e != null : "l'element coupe ne pas etre invalide" ;
        assert e.getProfondeur() > 0 : "La profondeur doit etre positive.";
        assert e.getMarge() > 0 : "La marge de profondeur doit être positive.";
        assert e.getOutil() != null : "L'outil ne peut pas être invalide.";
        assert e.getTypeCoupe() != null : "La typeCoupe doit etre positive.";

        this.Profondeur = e.getProfondeur();
        this.marge_profondeur = e.getMarge();
        this.outil = e.getOutil();
        this.uuid = UUID.randomUUID();
        this.typeCoupe = e.getTypeCoupe();
    }
    

     public float getMargeProfondeur() {
        return marge_profondeur;
    }

    public void setMargeProfondeur(float margeProfondeur) {
        assert margeProfondeur > 0 : "La marge de profondeur doit etre positive.";
        this.marge_profondeur = margeProfondeur;
    }

    public float getProfondeur() {
        return Profondeur;
    }


    public void setProfondeur(float profondeur) {
        assert profondeur > 0 : "La profondeur doit etre positive.";
        this.Profondeur = profondeur;
    }
    
    public Outil getOutil() {
        return outil;
    }
    public void setOutil(Outil outil) {

    assert outil != null : "L'outil ne peut pas etre invalide.";
    
    this.outil = outil;
}
public UUID getUUID() {
        return uuid;
}
public void setUUID(UUID uuid) {
        this.uuid = uuid;
}
public String getTypeCoupe() {
        return typeCoupe;
    }
}

