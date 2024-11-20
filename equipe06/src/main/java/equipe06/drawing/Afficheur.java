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
    public void dessinerCoupe(Graphics g, int x, int y, int hauteurTable, int largeurTable, boolean xy){
           System.out.println(x);
           if (x != -1) {
        Repere repere = Repere.getInstance();
        float x_mm = x;
        x_mm = repere.convertirEnMmDepuisPixels(x_mm);  
        float y_mm = y;
        y_mm = repere.convertirEnMmDepuisPixels(y_mm);
        controleur.CreerCoupe((float) x_mm, (float) y_mm, xy); // vertical est true
        System.out.printf("ENTREE 1");
        if(!controleur.getCoupes().isEmpty()){
        if (xy){
        // Transmettre la distance au contrôleur pour affichage dans MainWindow
        
            System.out.printf("ENTREE 2");
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2f)); //for now
            g2d.setColor(Color.BLACK); // Set color for the line
            controleur.mettreAJourDistanceX(x_mm);
            int ligneY1 = hauteurTable; // Starting point of the line
            g2d.drawLine(x, ligneY1, x, ligneY1 - repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLargeur())); // Draw the vertical line
        
        }
        else{
        // Transmettre la distance au contrôleur pour affichage dans MainWindow
        
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2f)); //for now
            g2d.setColor(Color.BLACK); // Set color for the line
            controleur.mettreAJourDistanceX(x_mm);
            int ligneX1 = largeurTable; // Starting point of the line
            g2d.drawLine(0, y, repere.convertirEnPixelsDepuisMm(controleur.getPanneau().getLongueur()), y); // Draw the vertical line
        
        }
        }
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
    public void dessinerRectangleAVdeuxpoints (Graphics g, int x1px, int y1px, int x2px, int y2px) {
        if (x1px != -1) {
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
    public void dessinerL (Graphics g, int x1, int y1, int x2 , int y2) {
        if (x1 != -1 && x2 != -1){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setStroke(new BasicStroke(2f));
        g2d.setColor(Color.BLACK);
        // dessiner le trait vertical
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
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawRect(xOrigine, yOrigine, bordureXPx, bordureYPx);
        
    }

    }

    
    
    
} 