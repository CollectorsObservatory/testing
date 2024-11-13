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
    private static final double SCALE_FACTOR = 0.09;

    private double zoomFactor = 1.0;
    private boolean peutCreerCoupe = false;

    // Variables pour gérer le décalage de la vue lors du zoom
    private double offsetX = 0.0;
    private double offsetY = 0.0;

    public PanneauVue(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        this.repere = new Repere();
        this.controleur = Controleur.getInstance();
        PanneauDTO panneauDTO = controleur.getPanneau();

        // Conversion des dimensions de la table CNC en appliquant un facteur d'échelle
        this.largeurPixelsTable = (int) (repere.convertirEnPixels(3000) * SCALE_FACTOR);
        this.hauteurPixelsTable = (int) (repere.convertirEnPixels(1500) * SCALE_FACTOR);

        // Conversion des dimensions du panneau avec facteur d'échelle
        this.largeurPixelsPanneau = (int) (repere.convertirEnPixels(panneauDTO.getLargeur()) * SCALE_FACTOR);
        this.hauteurPixelsPanneau = (int) (repere.convertirEnPixels(panneauDTO.getLongueur()) * SCALE_FACTOR);

        // Définir la taille préférée du panneau basé sur la table CNC
        this.setPreferredSize(new Dimension(largeurPixelsTable + 100, hauteurPixelsTable + 100));

        // Ajouter un MouseListener pour la création de coupe
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                drawingPanelMouseClicked(evt);
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
        if (peutCreerCoupe) {
            lastClickX = evt.getX();
            lastClickY = evt.getY();
            repaint();
            peutCreerCoupe = false;
        } else {
            // Zoom centré sur le clic
            double oldZoomFactor = zoomFactor;
            zoomFactor += 0.2;
            if (zoomFactor > 3.0) {
                zoomFactor = 3.0; // Limiter le zoom maximal
            }

            // Calculer la différence pour recadrer le point autour du clic
            double scaleChange = zoomFactor / oldZoomFactor;
            int clickX = evt.getX();
            int clickY = evt.getY();

            offsetX = clickX - (clickX - offsetX) * scaleChange;
            offsetY = clickY - (clickY - offsetY) * scaleChange;

            repaint();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Appliquer le facteur de zoom et le décalage
        g2d.translate(offsetX, offsetY);
        g2d.scale(zoomFactor, zoomFactor);

        // Dessiner la table CNC en gris clair avec une bordure noire
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(50, 50, largeurPixelsTable, hauteurPixelsTable);
        g2d.setColor(Color.BLACK);
        g2d.drawRect(50, 50, largeurPixelsTable, hauteurPixelsTable);

        // Dessiner les axes X et Y
        dessinerAxes(g2d);

        // Utiliser l'Afficheur pour dessiner les autres éléments (panneau, coupes, etc.)
        Afficheur afficheur = new Afficheur(mainWindow.controleur);
        afficheur.DessinerPanneau(g2d, SCALE_FACTOR * zoomFactor, hauteurPixelsTable);

        if (modifyTriggered) {
            afficheur.dessinerCoupeModifie(g2d, (float) (SCALE_FACTOR * zoomFactor), hauteurPixelsTable);
        } else {
            afficheur.dessinerCoupe(g2d, lastClickX, lastClickY, (float) (SCALE_FACTOR * zoomFactor), hauteurPixelsTable);
        }

        // Réinitialiser après dessin
        lastClickX = -1;
        lastClickY = -1;
        modifyTriggered = false;
    }

    private void dessinerAxes(Graphics g) {
        g.setColor(Color.BLACK);

        // Dessiner l'axe X (horizontal)
        int xStart = 50;
        int yPosition = 50 + hauteurPixelsTable;
        g.drawLine(xStart, yPosition, xStart + largeurPixelsTable, yPosition);

        // Ajouter des graduations sur l'axe X tous les 200 mm
        for (int i = 0; i <= 3000; i += 200) {
            int xPos = xStart + (int) (i * 3.78 * SCALE_FACTOR * zoomFactor);
            g.drawLine(xPos, yPosition - 5, xPos, yPosition + 5);
            g.drawString(String.valueOf(i), xPos - 10, yPosition + 20);
        }

        // Dessiner l'axe Y (vertical)
        int yStart = 50;
        int xPosition = 50;
        g.drawLine(xPosition, yStart, xPosition, yStart + hauteurPixelsTable);

        // Ajouter des graduations sur l'axe Y tous les 100 mm
        for (int i = 0; i <= 1500; i += 100) {
            int yPos = yStart + hauteurPixelsTable - (int) (i * 3.78 * SCALE_FACTOR * zoomFactor);
            g.drawLine(xPosition - 5, yPos, xPosition + 5, yPos);
            g.drawString(String.valueOf(i), xPosition - 45, yPos + 5);
        }
    }

    // Activer la création de la coupe
    public void activerCreationCoupe() {
        this.peutCreerCoupe = true;
    }

    public boolean isAttenteClicPourCoupe() {
        return peutCreerCoupe;
    }
}
