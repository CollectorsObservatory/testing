/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;
import equipe06.Domaine.CNC;
import equipe06.drawing.Afficheur;
import equipe06.gui.MainWindow;
import java.awt.Point;
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

     // Implémentation de la méthode pour créer une coupe axiale - Proposition  
     public void creerCoupeAxiale(float axe, float y, boolean composante) {


        cnc.creerCoupe(axe, y, composante);
    }

   
    public void modifierCoupe(float axe) {
        cnc.ModifierCoupe(axe);
        //TODO: check if inPanneau
    }

 
    public void supprimerCoupe() {
            cnc.supprimerCoupe();

    }


    
    public void SetPanneau(float longueurX, float largeurY, float profondeurZ) {
        // Appel de CNC pour créer le panneau avec les valeurs
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
    

}
    
    

