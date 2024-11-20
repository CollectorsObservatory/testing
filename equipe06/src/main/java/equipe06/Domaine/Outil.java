package equipe06.Domaine;
//@author amen
import java.util.UUID;


public class Outil {
    
    private String Nom;
    private UUID id;
    private float largeur_coupe;
    
    
    public Outil(String nom, float largeur_coupe) {
         /*if (nom == null || nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'outil ne peut pas etre invalide.");
        }
        if (id == null) {
            throw new IllegalArgumentException("L'ID de l'outil ne peut pas etre invalide.");
        }
        if (largeur_coupe <= 0) {
            throw new IllegalArgumentException("La largeur de coupe doit être superieure a zero.");
        }*/
        this.Nom = nom;
        //id to change
        this.id = UUID.randomUUID();
        this.largeur_coupe = largeur_coupe;
    }

    public float getLargeur_coupe() {
        return largeur_coupe;
    }
    public void setLargeur_coupe(float largeur_coupe) {
        if (largeur_coupe <= 0) {
            throw new IllegalArgumentException("La largeur de coupe doit etre superieure a zero.");
        }
        this.largeur_coupe = largeur_coupe;
    }
    public String getNom() {
        return Nom;
    }
    public void setNom(String nom) {
         if (nom == null|| nom.trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de l'outil ne peut pas invalide.");
        }
        this.Nom = nom;
    }
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
         if (id == null) {
            throw new IllegalArgumentException("L'ID de l'outil ne peut pas être null.");
        }
        this.id = id;
    }

}
