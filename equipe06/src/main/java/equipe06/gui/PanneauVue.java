package equipe06.gui;

import equipe06.gui.MainWindow;
import equipe06.Domaine.Repere;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import equipe06.drawing.Afficheur;
import equipe06.Domaine.Controleur;
import equipe06.Domaine.PanneauDTO;

public class PanneauVue extends JPanel {
    private MainWindow mainWindow;
    public int largeurPixelsTable;
    public int hauteurPixelsTable;
    private int largeurPixelsPanneau;
    private int hauteurPixelsPanneau;
    private Repere repere;
    private Controleur controleur;
    private int lastClickX = -1;
    private int lastClickY = -1;
    public boolean deleteTriggered = false;
    public boolean modifyTriggered;
    private float BordureX;
    private float BordureY;
    private double zoomFactor = 1.0;
    private boolean peutCreerCoupe = false;
    private boolean peutCreerCoupeRect = false;
    private boolean peutCreerCoupeBordure = false;
    private boolean peutCreerCoupeL = false;
    private boolean peutCreerCoupeH = false;
    private boolean peutCreerCoupeV = false;

    // Variables pour gérer le décalage de la vue lors du zoom
    private double offsetX = 0.0;
    private double offsetY = 0.0;

    // Variable pour la coupe rect (clic)
    private int rectX1 = -1;
    private int rectY1 = -1;
    private int rectX2 = -1;
    private int rectY2 = -1;

    // Variable pour la coupe L
    private int x1 = -1;
    private int y1 = -1;
    private int x2 = -1;
    private int y2 = -1;

    public PanneauVue(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.repere = Repere.getInstance();
        this.controleur = Controleur.getInstance();
        PanneauDTO panneauDTO = controleur.getPanneau();

        // Conversion des dimensions de la table CNC en appliquant un facteur d'échelle
        this.largeurPixelsTable = (int) (repere.convertirEnPixelsDepuisPouces(120));
        this.hauteurPixelsTable = (int) (repere.convertirEnPixelsDepuisPouces(60));

        // Conversion des dimensions du panneau avec facteur d'échelle
        this.largeurPixelsPanneau = (int) (repere.convertirEnPixelsDepuisMm(panneauDTO.getLargeur()));
        this.hauteurPixelsPanneau = (int) (repere.convertirEnPixelsDepuisMm(panneauDTO.getLongueur()));

        // Définir la taille préférée du panneau basé sur la table CNC
        this.setPreferredSize(new Dimension(largeurPixelsTable, hauteurPixelsTable));

        // Ajouter un MouseListener pour la création de coupe
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drawingPanelMouseClicked(evt);
            }
        });

        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                captureRectanglePoints(evt);
            }
        });

        // Ajouter un écouteur pour la roulette de la souris
        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                int notches = e.getWheelRotation();
                double oldZoomFactor = zoomFactor;

                if (notches < 0) {
                    // Molette vers le haut -> zoom avant
                    zoomFactor += 0.1;
                    if (zoomFactor > 3.0) {
                        zoomFactor = 3.0;
                    }
                } else {
                    // Molette vers le bas -> zoom arrière
                    zoomFactor -= 0.1;
                    if (zoomFactor < 1.0) {
                        zoomFactor = 1.0; // Réinitialiser à l'état initial
                        resetView();
                    }
                }

                // Si le zoomFactor est revenu à 1, réinitialiser complètement la vue
                if (zoomFactor == 1.0) {
                    resetView();
                } else {
                    // Sinon, ajuster le décalage proportionnellement au facteur de zoom
                    double scaleChange = zoomFactor / oldZoomFactor;
                    int mouseX = e.getX();
                    int mouseY = e.getY();

                    offsetX = mouseX - (mouseX - offsetX) * scaleChange;
                    offsetY = mouseY - (mouseY - offsetY) * scaleChange;
                }
                repaint(); // Redessiner après le changement de zoom
            }
        });
    }

    private void resetView() {
        // Réinitialiser les variables de zoom et de décalage
        zoomFactor = 1.0;
        offsetX = 0.0;
        offsetY = 0.0;
        repaint(); // Redessiner la vue pour l'état initial
    }

    private void drawingPanelMouseClicked(java.awt.event.MouseEvent evt) {
        if (peutCreerCoupeH) {
            lastClickX = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
            lastClickY = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
            repaint();
        } else if (peutCreerCoupeV) {
            lastClickX = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
            lastClickY = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
            repaint();
        }
    }

    private int ajusterCoordonneePourVue(int coordonnee, double offset, double zoomFactor) {
        return (int) ((coordonnee - offset) / zoomFactor);
    }
// Houni fama mochkla , ki nzid zoom bech to5rejch ml panneau , maadech najmou 
    //nsawrou coupe a partie mn 996 mm , fhmtch aaleh meme si limite hattha
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Appliquer le facteur de zoom et le décalage
        g2d.translate(offsetX, offsetY);
        g2d.scale(zoomFactor, zoomFactor);

        // Dessiner la table CNC en gris clair avec une bordure noire
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(0, 0, largeurPixelsTable, hauteurPixelsTable);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(0, 0, largeurPixelsTable - 1, hauteurPixelsTable - 1);

        // Utiliser l'Afficheur pour dessiner les autres éléments (panneau, coupes, etc.)
        Afficheur afficheur = new Afficheur(mainWindow.controleur);
        afficheur.DessinerPanneau(g, hauteurPixelsTable);

        // Dessiner les axes X et Y
        dessinerAxes(g2d);

        // Dessiner les coupes selon le zoom et le décalage
        if (modifyTriggered) {
            afficheur.dessinerCoupeModifie(g, hauteurPixelsTable);
        } else if (lastClickX != -1 && lastClickY != -1) {
            // Ajuster les coordonnées de la coupe au zoom et au décalage
            int adjustedX = ajusterCoordonneePourVue(lastClickX, offsetX, zoomFactor);
            int adjustedY = ajusterCoordonneePourVue(lastClickY, offsetY, zoomFactor);

            // S'assurer que la coupe reste dans les limites du panneau (zone orange)
            adjustedX = Math.max(0, Math.min(adjustedX, largeurPixelsPanneau - 1));
            adjustedY = Math.max(hauteurPixelsTable - hauteurPixelsTable, Math.min(adjustedY, hauteurPixelsTable - 1));

            if (peutCreerCoupeH) {
                afficheur.dessinerCoupe(g, adjustedX, adjustedY, hauteurPixelsTable, largeurPixelsTable, false);
            }
            if (peutCreerCoupeV) {
                afficheur.dessinerCoupe(g, adjustedX, adjustedY, hauteurPixelsTable, largeurPixelsTable, true);
            }
        }

        if (peutCreerCoupeRect && rectX1 != -1 && rectY1 != -1 && rectX2 != -1 && rectY2 != -1) {
            int adjustedX1 = rectX1;
            int adjustedY1 = rectY1;
            int adjustedX2 = rectX2;
            int adjustedY2 = rectY2;
            afficheur.dessinerRectangleAVdeuxpoints(g, adjustedX1, adjustedY1, adjustedX2, adjustedY2);
        }

        if (peutCreerCoupeL && x1 != -1 && y1 != -1 && x2 != -1 && y2 != -1) {
            int adjustedX1 = x1;
            int adjustedY1 = y1;
            int adjustedX2 = x2;
            int adjustedY2 = y2;
            afficheur.dessinerL(g, adjustedX1, adjustedY1, adjustedX2, adjustedY2);
        }

        if (peutCreerCoupeBordure) {
            float adjustedBordureX = BordureX;
            float adjustedBordureY = BordureY;
            afficheur.dessinerBordure(g, adjustedBordureX, adjustedBordureY, hauteurPixelsTable);
        }
        
        // Réinitialiser après dessin - Zoom ma t5dmch bel partie hethi
        // Supprimer coupe t5dmch menghirha 
        // Naarch chnowa l hall , nhebech nzid nbarbech 
        /* 
        lastClickX = -1;
        lastClickY = -1;
        rectX1 = -1;
        rectY1 = -1;
        rectY1 = -1;
        rectY2 = -1;
        modifyTriggered = false;
        peutCreerCoupeRect = false;
        peutCreerCoupeBordure = false;
        peutCreerCoupeL = false; */ 

    }
    public void resetVariables() {
    lastClickX = -1;
    lastClickY = -1;
    rectX1 = -1;
    rectY1 = -1;
    rectX2 = -1;
    rectY2 = -1;
    modifyTriggered = false;
    peutCreerCoupeRect = false;
    peutCreerCoupeBordure = false;
    peutCreerCoupeL = false;
}


    private void dessinerAxes(Graphics g) {
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.PLAIN, 12));
        FontMetrics metrics = g.getFontMetrics();

        Repere repere = Repere.getInstance();
        // Axe X (horizontal, sur la bordure inférieure)
        int yPosition = hauteurPixelsTable;
        g.drawLine(0, yPosition, largeurPixelsTable, yPosition);

        for (int i = 0; i <= 3000; i += 100) {
            int xPos = repere.convertirEnPixelsDepuisMm(i);
            g.drawLine(xPos, yPosition - 5, xPos, yPosition + 5);
            String texte = String.valueOf(i);
            int texteLargeur = metrics.stringWidth(texte);
            g.drawString(texte, xPos - texteLargeur / 2, yPosition + metrics.getHeight() + 5);
        }

        // Axe Y (vertical, sur la bordure gauche)
        int xPosition = 0;
        g.drawLine(xPosition, 0, xPosition, hauteurPixelsTable);

        for (int i = 0; i <= 1500; i += 100) {
            int yPos = hauteurPixelsTable - repere.convertirEnPixelsDepuisMm(i);
            g.drawLine(xPosition - 5, yPos, xPosition + 5, yPos);
            String texte = String.valueOf(i);
            int texteHauteur = metrics.getHeight();
            g.drawString(texte, xPosition - metrics.stringWidth(texte) - 10, yPos + texteHauteur / 4);
        }
    }

    public void activerCreationCoupeL() {
        this.peutCreerCoupeL = true;
    }

    public void activerCreationCoupeH() {
        this.peutCreerCoupeH = true;
    }

    public void activerCreationCoupeV() {
        this.peutCreerCoupeV = true;
    }

    public void activerCreationCoupeRect() {
        this.peutCreerCoupeRect = true;
    }

    public boolean isAttenteClicPourCoupe() {
        return peutCreerCoupe;
    }

    private void captureRectanglePoints(java.awt.event.MouseEvent evt) {
        if (rectX1 == -1 && rectY1 == -1 && (peutCreerCoupeRect || peutCreerCoupeL)) {
            rectX1 = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
            rectY1 = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
        } else {
            rectX2 = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
            rectY2 = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
            repaint();
        }
    }

    public void activerCreationCoupeBordure() {
        this.peutCreerCoupeBordure = true;
    }

    public void DimensionsBordure(float BordureXValue, float BordureYValue) {
        this.BordureX = BordureXValue;
        this.BordureY = BordureYValue;
        this.peutCreerCoupeBordure = true;
        repaint();
    }
}
