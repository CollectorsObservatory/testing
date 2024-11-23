package equipe06.drawing;

import equipe06.Domaine.*;

import java.awt.*;

public class Afficheur {
    private Controleur controleur;

    public Afficheur(Controleur controleur) {
        this.controleur = controleur;
    }

    public void DessinerPanneau(Graphics g, int hauteurTable) {
        Repere repere = Repere.getInstance();
        // Dessiner le panneau au-dessus de la table CNC en marron clair, positionné en bas à gauche
        g.setColor(new Color(205, 133, 63)); // Couleur marron clair pour le panneau

        PanneauDTO panneau = controleur.getPanneau();        
        int panneauX = repere.convertirEnPixelsDepuisMm(panneau.getLongueur()); // Positionner le panneau à gauche (même x que la table CNC)
        int panneauY = repere.convertirEnPixelsDepuisMm(panneau.getLargeur()); // Positionner en bas (table hauteur - panneau hauteur)

        g.fillRect(0, hauteurTable-panneauY, panneauX, panneauY); // Dessiner le panneau

        // Dessiner une bordure noire autour du panneau
        g.setColor(Color.BLACK); // Couleur pour la bordure
        g.drawRect(0, hauteurTable-panneauY, panneauX, panneauY);
    }

    //rendre cette fonction capable à dessiner toutes les coupes possible
    //accepte l'outil courant comme critere s'epaisseur de ligne
    //TODO hedi dessiner Coupe Axe
    public void dessinerCoupeAxiale(Graphics g,CoupeDTO coupe, int hauteurTable, int largeurTable, boolean xy){

          // if (x != -1) {
        Repere repere = Repere.getInstance();
        int axe_pixel = repere.convertirEnPixelsDepuisMm(coupe.getAxeDTO());
        //float x_mm = x;
        if(xy) {

            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2f)); // TODO: largeur outil
            g2d.setColor(Color.BLACK); // Set color for the line
            //controleur.mettreAJourDistanceX(x_mm);
            int ligneY1 = hauteurTable; // Starting point of the line
            g2d.drawLine(axe_pixel, ligneY1, axe_pixel, ligneY1 - repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLargeur())); // Draw the vertical line
        }
        else{     
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2f)); //for now
            g2d.setColor(Color.BLACK); // Set color for the line
            //controleur.mettreAJourDistanceX(x_mm);
            int ligneX1 = largeurTable; // Starting point of the line
            g2d.drawLine(0, axe_pixel, repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLongueur()), axe_pixel); // Draw the vertical line
        }

    }


    //rendre la fonction de modification declenche le Dessiner coupe au lieu de celle là
    public void dessinerCoupeModifie(Graphics g, int hauteurTable){
        CoupeDTO coupe = controleur.getCoupes().get(0);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2f));
        float axe = coupe.getAxeDTO();
        Repere repere = Repere.getInstance();
        int x = (int) ( repere.convertirEnPixelsDepuisMm(axe));
        System.out.printf(String.valueOf(x));
        g2d.setColor(Color.BLACK); // Set color for the line

        int ligneY1 = hauteurTable; // Starting point of the line
        g2d.drawLine(x, ligneY1, x, ligneY1-repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLongueur()));

    }
    
    //TODO zied Change en rect
    public void dessinerRectangleAVdeuxpoints (Graphics g, Point origine, Point destination) {
        if (origine != null && destination != null) {
            // Extraire les coordonnées des points
            int x1px = origine.x;
            int y1px = origine.y;
            int x2px = destination.x;
            int y2px = destination.y; 
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2f));
            g2d.setColor(Color.BLACK);
            // Calculer la position (coin supérieur gauche) et les dimensions du rectangle
            int x = Math.min(x1px, x2px);
            int y = Math.min(y1px, y2px);
            int largeur = Math.abs(x2px - x1px);
            int hauteur = Math.abs(y2px - y1px);
            g2d.drawRect(x, y, largeur, hauteur);
    }
    }
    //TODO dessiner un L : Katia
    public void dessinerL (Graphics g, Point origine, Point destination) {
        if (origine != null && destination != null) {
        System.out.println("here");
            // Extraire les coordonnées des point
        int x1 = origine.x;
        int y1= origine.y;
        int x2 = destination.x;
        int y2 = destination.y; 
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(Color.BLACK);
        // dessiner le trait vertical
        //J 'ai essayé avec origine.x origine.y desti.x origi.y et tout ca marche pas aussi 
        g2d.drawLine(x1, y1, x2, y1);
        // dessiner trai horizontal
        g2d.drawLine(x2, y1, x2, y2);
       }
    }
    //TODO Bordure Zied
    
    public void dessinerBordure(Graphics g, float bordureX, float bordureY, int hauteurTable) {
        if (bordureX != -1) {
        Repere repere = Repere.getInstance();
        int bordureXPx = repere.convertirEnPixelsDepuisMm(bordureX);
        int bordureYPx = repere.convertirEnPixelsDepuisMm(bordureY);
        int longueurOriginalePx = repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLongueur());
        int largeurOriginalePx = repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLargeur());
        int xOrigine = (longueurOriginalePx - bordureXPx) / 2;
        int yOrigine = hauteurTable - largeurOriginalePx + (largeurOriginalePx - bordureYPx) / 2;
        if (xOrigine >= 0 && yOrigine >= 0 && bordureXPx <= longueurOriginalePx && bordureYPx <= largeurOriginalePx){
            Graphics2D g2d = (Graphics2D) g;
            g2d.setColor(Color.BLACK);
            g2d.setStroke(new BasicStroke(2f));
            g2d.drawRect(xOrigine, yOrigine, bordureXPx, bordureYPx);
            System.out.println("here");
        }   
        else{
            System.out.println("out");
        }
    }
} 
}