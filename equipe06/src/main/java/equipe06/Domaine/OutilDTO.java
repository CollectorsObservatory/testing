/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;

/**
 *
 * @author hedib
 */
import java.util.UUID;

public class OutilDTO {
    private String NomDTO;
    private UUID idDTO;
    private float largeur_coupeDTO;
    
    public OutilDTO(Outil outil){
        this.NomDTO = outil.getNom();
        this.idDTO = outil.getId();
        this.largeur_coupeDTO = outil.getLargeur_coupe();
    }
    public float getLargeur_coupeDTO(){
        return largeur_coupeDTO;
    }
    public String getNomDTO(){
        return NomDTO;
    }
    public UUID getId(){
        return idDTO;
    }
}
