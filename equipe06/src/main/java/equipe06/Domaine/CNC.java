package equipe06.Domaine;

import equipe06.Domaine.Panneau;
import equipe06.Domaine.Repere;
import equipe06.Domaine.Coupe;
import equipe06.Domaine.*;
import equipe06.Domaine.Utils.ElementCoupe;

import java.awt.Point;
import java.util.Vector;


public class CNC {
    private Panneau panneau;
    private Repere repere;
    private Vector<Coupe> coupes;
    private Vector<Outil> outils;
    private Outil outil_courant;


    public CNC() {
        panneau = new Panneau(0,0,0);
        //panneau = new Panneau(1219.2f,914.4f , 0.5f); // Dimensions en mm
        //panneau = new Panneau(914.4f, 1219.2f, 0.5f); // Dimensions en mm
        repere = new Repere(); // Repère pour gérer les conversions
        coupes = new Vector <Coupe>();
        outils = new Vector<Outil>(12);
        
    }
    public void creerPanneau(float longueurX, float largeurY, float profondeurZ) {
        // Création de l'objet Panneau avec les attributs donnés
        this.panneau = new Panneau(longueurX, largeurY, profondeurZ);
    }
    
    public PanneauDTO getPanneau() {
        if (panneau == null) {
            throw new NullPointerException("Le panneau n'a pas encore été créé.");
        }
        return new PanneauDTO(panneau);
    }

    public Repere getRepere() {
        return repere;
    }

    public Vector<CoupeDTO> getCoupes() {
        Vector<CoupeDTO> cDTO = new Vector<CoupeDTO>();
        for (Coupe coupe : coupes) cDTO.add(new CoupeDTO(coupe));
        return cDTO;
    }

    public void ajouterOutil(String nom, float largeurCoupe){
        if (outils.size() < 12){
            Outil outil = new Outil(nom, largeurCoupe);
            outils.add(outil);
            System.out.println("Outil ajouté avec succès : " + outil);
        } else {
            System.out.println("Le nombre maximum d'outils (12) est atteint. Impossible d'ajouter un nouvel outil.");
        }
    }
    public Vector<OutilDTO> getOutils() {
        Vector<OutilDTO> v = new Vector<>();
        for(Outil outil : outils) v.add(new OutilDTO(outil));
        return v;
    }

    public OutilDTO getOutil_courant() {
        return new OutilDTO(outil_courant);
    }
    
    public void supprimerOutilParIndex(int index) {
        if (index >= 0 && index < outils.size()) {
            outils.remove(index);
            System.out.println("Outil supprimé avec succès.");
        } else {
            System.out.println("Index invalide. Impossible de supprimer l'outil.");
        }
    }
    
    
    public void creerCoupe(float axe,  float y, boolean composante) {
        Point pointOrigine = new Point((int)axe, (int)y);
        Point pointDestination = new Point((int)axe, 0);
        ElementCoupe e = new ElementCoupe( // elle doit etre dans le cnc pas dans controleur
                pointOrigine, pointDestination, 5.0f, 0.3f, axe, composante, 0.0f, 0.0f, "CoupeAxiale", null
        );
        assert e != null : "l'element de la coupe ne peut pas etre invalide" ; 
        
        CoupeAxe ma_coupe = new CoupeAxe(e); // this is only for now, further we will build this using a switch case bloc
        
        if (panneau.inPanneau(e.getPointOrigine(), panneau))
           AjouterCoupe(ma_coupe);
        else {
            
            assert false : "La coupe est invalide et ne peut pas etre ajoutée.";//to change, throws you out of the app
            

        }
    } // to change in the next livrable
    /// à discuter attribut de la fonction coupe ou bien element
    public void ModifierCoupe(float axe) {
            CoupeAxe coupe = (CoupeAxe) coupes.get(0);
            coupe.setAxe(axe);


    } // to change in the next livrable
    public boolean CoupeValide(Coupe coupe, Panneau panneau) {

        assert coupe != null : "La coupe ne peut pas etre invalide.";
        assert panneau != null : "Le panneau ne peut pas être invalide.";

        //to change when we have more coupes
        if(coupe instanceof CoupeAxe) {
            if(((CoupeAxe) coupe).getComposante()){
                return ((CoupeAxe) coupe).getAxe() < panneau.getLongueur(); //check either largeur or longueur
            }
            else return ((CoupeAxe) coupe).getAxe() < panneau.getLargeur();
        }
        else return false;//for now
    } // to change in the next livrable
    //juste penser a enlever cette fonction d'ici 
/*    
    public boolean inPanneau(Point p, Panneau panneau){
        assert p != null : "Le point ne peut pas etre invalide.";
        assert panneau != null : "Le panneau ne peut pas etre invalide.";
        int minX = 130;
        int maxX = (int) panneau.getLargeur() + 130;
        int minY = 0;
        int maxY = (int) panneau.getLongueur() - 130;
        return (p.x >= minX && p.x <= maxX) && ((1500 - p.y) >= minY && (1500 - p.y) <= maxY);
    }
    
  */  
    public void AjouterCoupe(Coupe coupe) {
        // S'assurer que l objet coupe est initialise et est valide

        coupes.add(coupe);

    }
    public void supprimerCoupe() {
        if(!coupes.isEmpty()) coupes.removeLast();

    }
    

    
    
}
