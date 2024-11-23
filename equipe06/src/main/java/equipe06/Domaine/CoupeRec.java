package equipe06.Domaine;

import equipe06.Domaine.Utils.ElementCoupe;

import java.awt.*;

public class CoupeRec extends Coupe{
    private Point PointOrigine;
    private Point PointDestination;
    private float BordureX;
    private float BordureY;

    //create new subclass Bordure
    public CoupeRec(ElementCoupe e) {
        super(e);
        assert e != null;
        
        // Transfert direct des valeurs de ElementCoupe
        this.BordureX = e.getBordureX();
        this.BordureY = e.getBordureY();
        PointOrigine = e.getPointOrigine();
        PointDestination = e.getPointDestination();

        // Validation avec des affichages
        System.out.println("Dans le constructeur de CoupeRec - BordureX après transfert : " + this.BordureX);
        System.out.println("Dans le constructeur de CoupeRec - BordureY après transfert : " + this.BordureY);
        System.out.println("Dans le constructeur de CoupeRec - BordureX après transfert : " + this.PointOrigine);
        System.out.println("Dans le constructeur de CoupeRec - BordureY après transfert : " + this.PointDestination);
        /*
        // Ajoutez des affichages ici pour valider les valeurs de ElementCoupe
        if(e.getPointDestination() != null && e.getPointOrigine()!= null) {
            PointOrigine = e.getPointOrigine();

            PointDestination = e.getPointDestination();
            assert PointDestination != null;
        }
        if(getBordureX()!=0 && getBordureY()!=0){
            assert getBordureX() >0;
            assert getBordureY() >0;
            BordureX = getBordureX();
            BordureY = getBordureY();
        }

*/
    }
    public Point getPointOrigine() {
        return PointOrigine;
    }
    public Point getPointDestination() {
        return PointDestination;
    }
    public float getBordureX() {
        return BordureX;
    }
    public float getBordureY() {
        return BordureY;
    }
    public void setPointOrigine(Point PointOrigine) {
        assert PointOrigine != null;
        this.PointOrigine = PointOrigine;
    }
    public void setPointDestination(Point PointDestination) {
        assert PointDestination != null;
        this.PointDestination = PointDestination;
    }
    public void setBordureX(float BordureX) {
        assert BordureX >= 0;
        this.BordureX = BordureX;
    }
    public void setBordureY(float BordureY) {
        assert BordureY >= 0;
        this.BordureY = BordureY;
    }

}
