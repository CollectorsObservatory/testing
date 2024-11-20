package equipe06.Domaine.Utils;
//import equipe06.Domaine.Utils.Point;
import java.awt.Point;
import java.util.UUID;

import equipe06.Domaine.Outil;
/**
 *
 * @author hedi, katia
 */


public class ElementCoupe {

    // Attributs de la classe
    private Point pointOrigine;
    private Point pointDestination;
    private float profondeur;
    private float marge;
    private float axe;
    private boolean composante;
    private float bordureX;
    private float bordureY;
    private String typeCoupe;
    private Outil outil;
    //private UUID uuid;

    // Constructeur par défaut
    public ElementCoupe() {
    }

    // Constructeur avec paramètres pour initialiser tous les attributs
    public ElementCoupe(Point pointOrigine, Point pointDestination, float profondeur, float marge, float axe,
                        boolean composante, float bordureX, float bordureY, String typeCoupe, Outil outil) {

        if (typeCoupe == null || typeCoupe.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de coupe ne peut pas etre null ou vide.");
        }
        if (profondeur <= 0) {
            throw new IllegalArgumentException("La profondeur doit etre superieure a zero.");
        }
        if (marge < 0) {
            throw new IllegalArgumentException("La marge doit etre non negative.");
        }
        switch (typeCoupe) {
            case "Rect":
                if (bordureX < 0) {
                    throw new IllegalArgumentException("La bordure X doit etre non negative.");
                }
                if (bordureY < 0) {
                    throw new IllegalArgumentException("La bordure Y doit etre non negative.");
                }
                if(pointOrigine == null){
                    throw new IllegalArgumentException("le pointOrigine ne peut pas etre null.");
                }
                if(pointDestination == null){
                    throw new IllegalArgumentException("le pointDestination ne peut pas etre null.");
                }
                break;
            case "axe":
                if (axe < 0) {
                    throw new IllegalArgumentException("L'axe doit etre superieur ou egal a zero.");
                }
                break;
            case "L":
                if (pointOrigine == null) {
                    throw new IllegalArgumentException("le pointOrigine ne peut pas etre null.");
                }
                if (pointDestination == null) {
                    throw new IllegalArgumentException("le pointDestination ne peut pas etre null.");
                }

        }
        //remove comment quand outil est valide
        /*if (outil == null) {
            throw new IllegalArgumentException("L'outil ne peut pas etre null.");
        }*/


        this.pointOrigine = pointOrigine;
        this.pointDestination = pointDestination;
        this.profondeur = profondeur;
        this.marge = marge;
        this.axe = axe;
        this.composante = composante;
        this.bordureX = bordureX;
        this.bordureY = bordureY;
        this.typeCoupe = typeCoupe;
        this.outil = outil;
        UUID u = UUID.randomUUID(); //check the randomness

    }

    // Getters et setters pour chaque attribut
    public Point getPointOrigine() {
        return pointOrigine;
    }
    public void setPointOrigine(Point pointOrigine) {
        if (pointOrigine == null) {
            throw new IllegalArgumentException("Le point d'origine ne peut pas etre invalide.");
        }
        this.pointOrigine = pointOrigine;
    }

    public Point getPointDestination() {
        return pointDestination;
    }
    public void setPointDestination(Point pointDestination) {
        if (pointDestination == null) {
            throw new IllegalArgumentException("Le point de destination ne peut pas etre null.");
        }
        this.pointDestination = pointDestination;
    }

    public float getProfondeur() {
        return profondeur;
    }
    public void setProfondeur(float profondeur) {
        if (profondeur <= 0) {
            throw new IllegalArgumentException("La profondeur doit etre superieure a zero.");
        }
        this.profondeur = profondeur;
    }

    public float getMarge() {
       
        return marge;
    }
    public void setMarge(float marge) {
        if (marge < 0) {
            throw new IllegalArgumentException("La marge doit etre non negative.");
        }
        this.marge = marge;
    }

    public float getAxe() {
        return axe;
    }
    public void setAxe(float axe) {
        if (axe < 0) {
            throw new IllegalArgumentException("L'axe doit etre superieur ou egal a zero.");
        }
        this.axe = axe;
    }

    public boolean getComposante() {
        return composante;
    }
    public void setComposante(boolean composante) {
        this.composante = composante;
    }

    public float getBordureX() {
        return bordureX;
    }
    public void setBordureX(float bordureX) {
        if (bordureX < 0) {
            throw new IllegalArgumentException("La bordure X doit être non negative.");
        }
        this.bordureX = bordureX;
    }

    public float getBordureY() {
        return bordureY;
    }
    public void setBordureY(float bordureY) {
        if (bordureY < 0) {
            throw new IllegalArgumentException("La bordure Y doit être non négative.");
        }
        this.bordureY = bordureY;
    }

    public String getTypeCoupe() {
        return typeCoupe;
    }
    public void setTypeCoupe(String typeCoupe) {
        if (typeCoupe == null || typeCoupe.trim().isEmpty()) {
            throw new IllegalArgumentException("Le type de coupe ne peut pas être null ou vide.");
        }
        this.typeCoupe = typeCoupe;
    }

    public Outil getOutil() {
        return outil;
    }
    public void setOutil(Outil outil) {
        if (outil == null) {
            throw new IllegalArgumentException("L'outil ne peut pas être null.");
        } // to activate when outil is complete
        
        this.outil = outil;
    }

    /*public UUID getUuid() {
        return uuid;
    }*/
}