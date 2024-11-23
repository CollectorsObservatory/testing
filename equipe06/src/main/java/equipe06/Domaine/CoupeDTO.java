/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equipe06.Domaine;
import equipe06.Domaine.Utils.ElementCoupe;

import java.awt.*;
import java.util.UUID;

/**
 *
 * @author hedib
 */

public  class CoupeDTO {

    // Attributs privés
    //private Outils outils;
    private float marge_profondeurDTO = 0.5f;
    private float ProfondeurDTO;
    private  float axeDTO;
    private boolean composanteDTO;
    private Point pointOrigineDTO;
    private Point pointDestinoDTO;
    private String TypeCoupeDTO;
    private float BordureXDTO;
    private float BordureYDTO;
    private UUID uuidDTO;
    private Outil outilDTO; //outilDTO

    public CoupeDTO(Coupe coupe){
        assert coupe != null;
        this.ProfondeurDTO = coupe.getMargeProfondeur();
        this.marge_profondeurDTO= coupe.getProfondeur();
        this.outilDTO = coupe.getOutil();    
        if(coupe instanceof CoupeAxe){
            this.axeDTO = ((CoupeAxe) coupe).getAxe();
            this.composanteDTO = ((CoupeAxe) coupe).getComposante();
        }
        if(coupe instanceof CoupeRec){
            this.BordureXDTO = ((CoupeRec) coupe).getBordureX();
            this.BordureYDTO = ((CoupeRec) coupe).getBordureY();
            this.pointOrigineDTO = ((CoupeRec) coupe).getPointOrigine();
            this.pointDestinoDTO = ((CoupeRec) coupe).getPointDestination();
        }
        if(coupe instanceof CoupeL){
            this.pointOrigineDTO = ((CoupeL) coupe).getPointOrigine();
            this.pointDestinoDTO = ((CoupeL) coupe).getPointDestination();
        }
        this.TypeCoupeDTO = coupe.getTypeCoupe();
    }
    
    public float getProfondeurDTO() {
        return ProfondeurDTO;
    }

     public float getMargeProfondeur() {
        return marge_profondeurDTO;
    }

    public float getAxeDTO(){
        assert axeDTO > 0;
        return this.axeDTO;
    }
    public boolean isComposanteDTO() {
        //assert axeDTO > 0;
        return composanteDTO;
    }
    public Point getPointOrigineDTO() {
        return pointOrigineDTO;
    }
    public Point getPointDestinoDTO() {
        return pointDestinoDTO;
    }
    public String getTypeCoupeDTO() {
        return TypeCoupeDTO;
    }
    public float getBordureXDTO() {
        return BordureXDTO;
    }
    public float getBordureYDTO() {
        return BordureYDTO;
    }
    public UUID getUuidDTO() {
        return uuidDTO;
    }
    public Outil getOutilDTO() {
        return outilDTO;
    }



}


