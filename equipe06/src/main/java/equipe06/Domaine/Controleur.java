/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;
import equipe06.Domaine.CNC;
import equipe06.drawing.Afficheur;
import equipe06.gui.MainWindow;
import java.awt.Point;
import java.util.UUID;
import java.util.Vector;

import equipe06.Domaine.Utils.ElementCoupe;

/**
 *
 * @author ziedd
 */
public class Controleur {
    
    private static Controleur instance; // Instance unique de Controleur
    private CNC cnc;
    private MainWindow mainWindow;
    public boolean suprim = false;
    public static double scaleFactor = 0.25; // Réduit la taille à 25% les dimensions elli hab alihom ell prof kbar donc hatit ell facteur hedha juste tempo bech tawwa matkallaknech
    // Constructeur privé pour empêcher la création directe
    private Controleur() {
        this.cnc = new CNC();
    }

    // Méthode statique pour obtenir l'instance unique
    public static Controleur getInstance() {
        if (instance == null) {
            instance = new Controleur();
        }
        return instance;
    }

    // Méthode pour initialiser la référence à MainWindow
    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        //transmettreDimensionsPanneau();
    }
    
    
    // cette methode permet au controleur de transmettre la valeur de x a mainwindow et de mettre a jours distancex (jtextframe ou s'affiche la valeur a cote bout modif coupe)
    public void mettreAJourDistanceX(float x) {
    if (mainWindow != null) {
        //System.out.println("MainWindow est bien référencée"); // verification console ne peut l'enlever
        mainWindow.afficherValeurDistanceX(x); // Appeler la méthode de MainWindow pour afficher la distance 
    }else {
        System.out.println("MainWindow est null"); // verification console ne peut l'enlever 
    }
}
    //getters
    public Repere getRepere() {return cnc.getRepere();}
    public Vector<CoupeDTO> getCoupes() {return cnc.getCoupes();}
    public PanneauDTO getPanneau() {return cnc.getPanneau();}
    //fares
    //TODO liaison controleur afficheur, controleur CNC : amen, katia
     // TODO: change content
     public void CreerCoupeAxiale(Point p, boolean composante) {


        cnc.CreerCoupeAxe(Repere.getInstance().convertirEnMmDepuisPixels((int) p.getX()),
                    Repere.getInstance().convertirEnMmDepuisPixels((int) p.getY())
                , composante);
    }
     
    public void SetCoupeBordure(float BordureXValue, float BordureYValue) {
        cnc.CreerCoupeBordure(BordureXValue, BordureYValue);
    }     
     
    //TODO: get the uuid from the click on panneau to use in modifying or deleting coupe
    public UUID getUUID(){
        return UUID.randomUUID();//to change remove by @amen
    }
    // TODO: appel correct
    public void modifierCoupe(float axe) {
        cnc.ModifierCoupe(axe);
    }

    // TODO: corriger appel , faut le UUID
    public void supprimerCoupe() {
       if ( getUUID() != null )
        { cnc.supprimerCoupe(getUUID());
        }

    }


    
    public void SetPanneau(float longueurX, float largeurY, float profondeurZ) {
        if (longueurX <= 0 || largeurY <= 0 || profondeurZ <= 0) {
            throw new IllegalArgumentException("Les dimensions doivent être positives.");
        }
        cnc.creerPanneau(longueurX, largeurY, profondeurZ);
    }

    
    public void SetOutil(String nomOutil, float epaisseur) {
        cnc.ajouterOutil(nomOutil, epaisseur);    
    }

    
    // on appelle cette methode lors d'ajouter ou modifier un outil pour que la table des outils fait une mise a jour
    public void mettreAJourTableauOutils() {
        Vector<OutilDTO> outils = cnc.getOutils(); // Récupérer le vecteur d'OutilDTO depuis CNC
        mainWindow.afficherOutilsDansTable(outils); // Mettre à jour la table dans MainWindow
    }
    
    public void supprimerOutil(int index) {
        cnc.supprimerOutilParIndex(index); // Supprime l'outil en fonction de l'index
    }


    public void CreerCoupeRect(Point origin, Point dest) {
        cnc.CreerCoupeRect(origin, dest);
    }

    public void CreerCoupeL(Point origin, Point destination) {
        cnc.CreerCoupeL(origin, destination);
    }
 public CoupeDTO selectionnerCoupe(UUID uuid) {
    Vector<CoupeDTO> coupes = getCoupes(); // Récupérer les coupes depuis CNC.
    for (CoupeDTO coupe : coupes) { // Parcourir toutes les coupes.
        if (coupe.getUuidDTO().equals(uuid)) {
            return coupe;
        }
    }
    return null; // Aucun match trouvé.
}



}
