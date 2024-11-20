package equipe06.Domaine;

import equipe06.Domaine.Panneau;
import equipe06.Domaine.Repere;
import equipe06.Domaine.Coupe;
import equipe06.Domaine.*;
import equipe06.Domaine.Utils.ElementCoupe;

import java.awt.Point;
import java.util.List;
import java.util.UUID;
import java.util.Vector;


public class CNC {
    private Panneau panneau;
    private Repere repere;
    private Vector<Coupe> coupes;
    private Vector<Point> points_de_reference;
    private Vector<Outil> outils;
    private Outil outil_courant;


    public CNC() {
        panneau = new Panneau(1200,1000,0);
        //repere = new Repere(); // Repère pour gérer les conversions
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
    //-------------------------------------------------OUTILS--------------------------------------------
    // TODO fix redundancy
    public void ajouterOutil(String nom, float largeurCoupe){
        Outil outil = new Outil(nom, largeurCoupe);
        if (outils.size() < 12  && !outils.contains(outil)){
            outils.add(outil);
            System.out.println("Outil ajouté avec succès : " + outil); //remove @zied
        } else {
            System.out.println("Le nombre maximum d'outils (12) est atteint. Impossible d'ajouter un nouvel outil."); //remove @zied
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
    //TODO: mettre a jour depuis la selection du outil courant depuis l'interface
    public void setOutil_courant(OutilDTO outil_courant) {}
    //TODO : rendre cette boucle en try catch
    // TODO check if outil est outilCourant
    public void supprimerOutilParIndex(int index) {
        if (index >= 0 && index < outils.size()) {
            Outil outil = outils.get(index);
            outils.remove(index);
            if (outil_courant.getNom() == outil.getNom())  //set another
                outil_courant = outils.get(0);

            System.out.println("Outil supprimé avec succès."); //control local remove @ zied

        } else {
            System.out.println("Index invalide. Impossible de supprimer l'outil."); //control local remove @ zied
        }
    }
    //amen
    public void ModifierOutil(UUID uuid, String NewName, float NewLargeur){

        for(Outil outil : outils){
            if(outil.getId() == uuid){
                if(outil.getNom()!= NewName)
                    outil.setNom(NewName);
                if(outil.getLargeur_coupe() != NewLargeur)
                    outil.setLargeur_coupe(NewLargeur);
            }
        }
    }
    public void ModifierCoupesOutilCourant(){
        for(Coupe coupe: coupes){
            coupe.setOutil(outil_courant);
        }
    }


    //-------------------------------------------------COUPES--------------------------------------------------------
    //amen
    public void CreerCoupe(String TypeCoupe, float axe, float y, boolean composante) {
        switch(TypeCoupe){
            case "axe":
                this.CreerCoupeAxe(axe, y, composante);
                break;
            case "Rect":
                //this.CreerCoupeRect(e);
                break;
            case "L":
                //this.CreerCoupeL(e);
                break;
            case "Bordure":
                //this.CreerCoupeBordure(e);
                break;

        }
    }
    //Katia
    public void CreerCoupeL(Point pointOrigine , Point pointDestination){
        //TODO coupe en L, attribut extraits du controleur
        assert pointOrigine != null;
        assert pointDestination != null;
        ElementCoupe e  = new ElementCoupe( pointOrigine, pointDestination,5.0f,0.3f,0,false,0.0f,0.0f,"CoupeenL",null);
         CoupeL coupe = new CoupeL(e);
         if(panneau.inPanneau(pointOrigine)&& panneau.inPanneau(pointDestination)){
             coupes.add(coupe);
         }
    }
    //Amen
    public void CreerCoupeRect(Point Origine, Point Destination){
        //TODO coupe rect, attribut extraits du controleur
        assert (Origine != null);
        assert (Destination != null);
        ElementCoupe e = new ElementCoupe(
                Origine, Destination, 5.0f,
                0.3f,0,false,0.0f, 0.0f,"Rect", null);
        CoupeRec coupe = new CoupeRec(e);
        if(panneau.inPanneau(Origine) && panneau.inPanneau(Destination)){
            coupes.add(coupe);
        }

    }

    //zied
    public void CreerCoupeBordure(float x1, float y1, float x2, float y2){
        //TODO creer une coupe bordure
        /*
        Point pointOrigine = new Point((int)x1, (int)y1);
        Point pointDestination = new Point((int)x2, (int)y2);
        ElementCoupe e = new ElementCoupe( // elle doit etre dans le cnc pas dans controleur
                pointOrigine, pointDestination, 5.0f, 0.3f, 0, false, 0.0f, 0.0f, "Bordure", null );
        */
        
    }

    //hedi+amen
    // TODO :changer ça en fnct creer coupeAXE, correction sur l'ajout du point origine et destination dans le element coupe
    public void CreerCoupeAxe(float x,  float y, boolean composante) {
        ElementCoupe e = null;
        CoupeAxe ma_coupe = null;
        Point pointOrigine;
        Point pointDestination;
        if (composante == true)
        {
        pointOrigine = new Point((int)x, (int) y); //change point
        pointDestination = new Point((int)x, 0);
        e = new ElementCoupe( // elle doit etre dans le cnc pas dans controleur
                pointOrigine, pointDestination, 5.0f, 0.3f, x, composante, 0.0f, 0.0f, "axe", null
        );
        }
        else{
            pointOrigine = new Point(0, (int)y); //change point
            pointDestination = new Point((int) panneau.getLargeur() + 130, (int)y);
            e = new ElementCoupe( // elle doit etre dans le cnc pas dans controleur
            pointOrigine, pointDestination, 5.0f, 0.3f, y, composante, 0.0f, 0.0f, "axe", null
            );
        }
        assert e != null : "l'element de la coupe ne peut pas etre invalide" ;
        ma_coupe = new CoupeAxe(e);
        AjouterCoupe(ma_coupe);
        if (panneau.inPanneau(pointOrigine)) //remove katia
            {AjouterCoupe(ma_coupe);}
        else {
            assert false : "La coupe est invalide et ne peut pas etre ajoutée.";//to change, throws you out of the app
        }
        
    }
    //non pour le moment
    // TODO: Rendre modifier apte a modifier toute coupe possible
    // cette fonction fait appel au divers coupes
    public void ModifierCoupe(float axe) {
            CoupeAxe coupe = (CoupeAxe) coupes.get(0);
            coupe.setAxe(axe);


    }
    
    public void ModifierCoupeRectL() {
        //verifier si ma coupe est modifiée lors de la modification d'un axe
    }
    //hedi
    // TODO: fnct invalide pour le reste du travail
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
 
    //amen
    // TODO: changer en ajoutant les uuid
    public void AjouterCoupe(Coupe coupe) {
        // vector of uuids
        Vector<UUID> uuids = new Vector<UUID>();
        for(Coupe c: coupes){
            uuids.add(coupe.getUUID());
        }
        do{
            coupe.setUUID(UUID.randomUUID());
        }while(uuids.contains(coupe.getUUID()));

        coupes.add(coupe);

    }
    //katia
    // juste a la sup svpl zied
    // TODO: changer ça , delete en se basant sur l 'UUID
   /* public void supprimerCoupe() {
        if(!coupes.isEmpty()) coupes.removeLast();

    }*/
    public void supprimerCoupe(UUID uuid) {
        try{
            for (int i=0; i<coupes.size(); i++){
                if (coupes.get(i).getUUID().equals(uuid)){
                    coupes.remove(i);
                    break;
                }
            }
        } catch (IndexOutOfBoundsException e){
            System.out.println("Erreur : y'a pas de coupe a suprimmer ");
        }
    }
}
