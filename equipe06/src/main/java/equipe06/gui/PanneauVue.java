package equipe06.gui;

import equipe06.Domaine.CoupeDTO;
import equipe06.gui.MainWindow;
import equipe06.Domaine.Repere;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import equipe06.drawing.Afficheur;
import equipe06.Domaine.Controleur;
import equipe06.Domaine.PanneauDTO;
import java.util.Vector;
import java.util.UUID;

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
    // Ajouter cette ligne au début de la classeà
    private CoupeDTO coupeSelectionnee;
    private boolean modeSelection = false; 
    // Variables pour gérer le décalage de la vue lors du zoom
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    public Point myPoint;
    // Variable pour la coupe rect (clic)
    private int rectX1 = -1;
    private int rectY1 = -1;
    private int rectX2 = -1;
    private int rectY2 = -1;

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
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        this.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                if(peutCreerCoupeL||peutCreerCoupeRect){
                    captureRectanglePoints(evt);
                    }
            }
           public void mousePressed(java.awt.event.MouseEvent evt) {
                        handleMousePress(evt);
    }         
        });
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
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
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////     
    }
    public void setModeSelection(boolean isActive) {
    this.modeSelection = isActive;
    System.out.println("Mode selection " + (isActive ? "active" : "desactive") + ".");
}
    private CoupeDTO trouverCoupeCliquee(int clickedMmX, int clickedMmY) {
    Vector<CoupeDTO> coupes = controleur.getCoupes();
    for (CoupeDTO coupe : coupes) {
        if (coupe.getTypeCoupeDTO().equals("Axe")) {
            if (coupe.isComposanteDTO()) { // Coupe verticale
                int axeX = (int) coupe.getAxeDTO();
                if (Math.abs(clickedMmX - axeX) < 10) { // Marge de 10 mm
                    System.out.println("Coupe verticale détectee a X = " + axeX);
                    return coupe;
                }
            } else { // Coupe horizontale
                int axeY = (int) coupe.getAxeDTO();
                if (Math.abs(clickedMmY - axeY) < 10) { // Marge de 10 mm
                    System.out.println("Coupe horizontale detectee à Y = " + axeY);
                    return coupe;
                }
            }
        }
    }
    return null;
}
private void handleMousePress(java.awt.event.MouseEvent evt) {
    if (!modeSelection) {
        return; // Ignorer le clic si le mode sélection n'est pas activé
    }

    // Coordonnées cliquées en pixels
    int pixelX = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
    int pixelY = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);

    // Convertir en millimètres
    float mmX = Repere.getInstance().convertirEnMmDepuisPixels(pixelX);
    float mmY = Repere.getInstance().convertirEnMmDepuisPixels(pixelY);
    System.out.println("Utilisateur a clique a (pixels) : (" + pixelX + ", " + pixelY + ")");
    System.out.println("Utilisateur a clique a (mm) : (" + mmX + ", " + mmY + ")");

    // Chercher une coupe à ces coordonnées
    CoupeDTO coupeCliquee = trouverCoupeCliquee((int) mmX, (int) mmY);
    if (coupeCliquee != null) {
        UUID uuid = coupeCliquee.getUuidDTO();
        if (uuid != null) {
            CoupeDTO coupeSelectionnee = controleur.selectionnerCoupe(uuid);
            setCoupeSelectionnee(coupeSelectionnee);
        }
    } else {
        System.out.println("Aucune coupe detectee.");
    }

    // Désactiver le mode sélection
    setModeSelection(false);
}

public void setCoupeSelectionnee(CoupeDTO coupe) {
    if (coupe == null) {
        System.err.println("Erreur : Aucune coupe sélectionnee.");
        return;
    }

    this.coupeSelectionnee = coupe;

    // Vérification du type de coupe
    if (coupe.getTypeCoupeDTO().equals("Axe")) {
        // Coupe axiale
        float axe = coupe.getAxeDTO();
        if (coupe.isComposanteDTO()) {
            // Coupe verticale : X = axe, Y = 0
            System.out.println("Coupe verticale detectee. Axe X = " + axe);
            mainWindow.updateDimensions(axe, 0); // X est défini, Y est 0
        } else {
            // Coupe horizontale : X = 0, Y = axe
            System.out.println("Coupe horizontale detectee. Axe Y = " + axe);
            mainWindow.updateDimensions(0, axe); // Y est défini, X est 0
        }
    } else if (coupe.getPointOrigineDTO() != null) {
        // Autres types de coupes avec origine/destination
        Point origine = coupe.getPointOrigineDTO();
        System.out.println("Coupe non axiale detectee. Origine = (" + origine.x + ", " + origine.y + ")");
        mainWindow.updateDimensions((float) origine.x, (float) origine.y);
    } else {
        System.err.println("Erreur : Type de coupe non pris en charge.");
    }

    repaint(); // Redessiner pour afficher la sélection
}



private void updateMouseCoordinates(java.awt.event.MouseEvent evt) {
    // Coordonnées brutes de la souris
    int rawX = evt.getX();
    int rawY = evt.getY();
    // Ajustement en fonction du zoom et du décalage
    int adjustedX = ajusterCoordonneePourVue(rawX, offsetX, zoomFactor);
     int adjustedY = hauteurPixelsTable - ajusterCoordonneePourVue(rawY, offsetY, zoomFactor);
  
    // Envoi des coordonnées ajustées à MainWindow
    mainWindow.updateDimensions(adjustedX, adjustedY);
}


public void selectionnerUneCoupe() {
    Vector<CoupeDTO> coupes = controleur.getCoupes();

    for (CoupeDTO coupe : coupes) {
        if (coupe.getTypeCoupeDTO().equals("Axe")) {
            coupeSelectionnee = coupe;
            float axe = coupe.getAxeDTO();

            if (coupe.isComposanteDTO()) {
               
                mainWindow.updateDimensions(axe, 0);
            } else {
                // Coupe horizontale : X = 0, Y = axe
                mainWindow.updateDimensions(0, axe);
            }

            System.out.println("Coupe axiale selectionnee : " +
                               (coupe.isComposanteDTO() ? "Verticale" : "Horizontale") +
                               " à l'axe=" + axe);
            repaint();
            return;
        }
    }
    System.out.println("Aucune coupe axiale trouvee !");
    coupeSelectionnee = null;
    repaint();
}
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void resetView() {
        // Réinitialiser les variables de zoom et de décalage
        zoomFactor = 1.0;
        offsetX = 0.0;
        offsetY = 0.0;
        repaint(); // Redessiner la vue pour l'état initial
    }

    public void drawingPanelMouseClicked(java.awt.event.MouseEvent evt) {
        //if (peutCreerCoupeH) {
            lastClickX = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
            lastClickY = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
        //    repaint();
        //} else if (peutCreerCoupeV) {
        //    lastClickX = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
         //   lastClickY = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
         //   repaint();
        //}
        myPoint = new Point(lastClickX, lastClickY);
        if(peutCreerCoupeV) {    
           mainWindow.updateDimensions(lastClickX, 0); // Mise à jour de DimX et DimY
           controleur.CreerCoupeAxiale(myPoint, true);
            repaint();
            peutCreerCoupeV=false;
            System.out.print("Coupe cree avec succes!\n");
        }
        if(peutCreerCoupeH) {
            int invertedY = hauteurPixelsTable - lastClickY;
             mainWindow.updateDimensions(0, invertedY);
            controleur.CreerCoupeAxiale(myPoint, false);
            repaint();
            peutCreerCoupeH = false;
            System.out.print("Coupe cree avec succes!\n");
        }
        lastClickX = -1;
        lastClickY = -1;
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
        Vector<CoupeDTO> Coupes = controleur.getCoupes();
        for(CoupeDTO coupe: Coupes)
        {
            if(coupe.getTypeCoupeDTO()=="Axe" && coupe.isComposanteDTO()) {
                  boolean composante = coupe.isComposanteDTO();
                afficheur.dessinerCoupeAxiale(g, coupe, hauteurPixelsTable, largeurPixelsTable, true);
            }
            else if(coupe.getTypeCoupeDTO()=="Axe" && !coupe.isComposanteDTO())  {
                  boolean composante = coupe.isComposanteDTO();
                afficheur.dessinerCoupeAxiale(g,coupe, hauteurPixelsTable, largeurPixelsTable, false);
            }
            else if(coupe.getTypeCoupeDTO()=="Bordure") {
                afficheur.dessinerBordure(g, coupe.getBordureXDTO(), coupe.getBordureYDTO(), hauteurPixelsTable);
            }
            else if(coupe.getTypeCoupeDTO().equals("Rect")){
                //TODO fix dessiner avec argument coupe
                Point pointOrigine = coupe.getPointOrigineDTO();
                Point pointDestino = coupe.getPointDestinoDTO();
                //System.out.println("Point Origine DTO: x=" + pointOrigine.x + ", y=" + pointOrigine.y);
                //System.out.println("Point Destino DTO: x=" + pointDestino.x + ", y=" + pointDestino.y);
                afficheur.dessinerRectangleAVdeuxpoints(g, coupe.getPointOrigineDTO(), coupe.getPointDestinoDTO());
            }
            else if (coupe.getTypeCoupeDTO().equals("CoupeenL"))  {
            Point pointOrigine = coupe.getPointOrigineDTO();
            Point pointDestino = coupe.getPointDestinoDTO();
            System.out.println("Point Origine DTO: x=" + pointOrigine.x + ", y=" + pointOrigine.y);
            System.out.println("Point Destino DTO: x=" + pointDestino.x + ", y=" + pointDestino.y);
            afficheur.dessinerL(g, pointOrigine, pointDestino);
             }
            System.out.println("Valeur de coupe.getTypeCoupeDTO() : " + coupe.getTypeCoupeDTO());
        }
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
            System.out.println(rectX1);
            System.out.println(rectY1);
        } else {
            rectX2 = ajusterCoordonneePourVue(evt.getX(), offsetX, zoomFactor);
            rectY2 = ajusterCoordonneePourVue(evt.getY(), offsetY, zoomFactor);
            System.out.println(rectX2);
            System.out.println(rectY2);
            Point Origin = new Point(rectX1, rectY1);
            Point Dest = new Point(rectX2, rectY2);
            if(peutCreerCoupeRect) {
                controleur.CreerCoupeRect(Origin, Dest);
                repaint();
                peutCreerCoupeRect = false;
            }
            else if(peutCreerCoupeL) {
                controleur.CreerCoupeL(Origin, Dest);
                System.out.printf("avant REpaint");
                repaint();
                System.out.printf("apres REpaint");
                peutCreerCoupeL = false;
            }
            rectX1 = -1;
            rectY1 = -1;

        }
    }

    public void activerCreationCoupeBordure() {
        this.peutCreerCoupeBordure = true;
    }

 /*   public void DimensionsBordure(float BordureXValue, float BordureYValue) {
        this.BordureX = BordureXValue;
        this.BordureY = BordureYValue;
        this.peutCreerCoupeBordure = true;
        repaint();
    }*/
} 