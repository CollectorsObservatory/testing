package equipe06.Domaine;
import equipe06.Domaine.Utils.ElementCoupe;
import equipe06.Domaine.Coupe;
import java.awt.Point;
import java.awt.*;

public class CoupeL extends Coupe{
  private Point pointOrigine ;
  private Point pointDestination ;
    // Implémentation de la méthode abstraite coupe
    //@Override
    public CoupeL(ElementCoupe e){
        super(e);
        assert e != null:"L'element de coupe est invalide.";
        // on peux pas desiner la coupe en L si les 2 points sont identiques car on aura pas d'intersection
        assert pointDestination == pointOrigine:"Les points d'origine et de destination ne doivent pas être identiques.";
        this.pointOrigine  = e.getPointOrigine();
        this.pointDestination = e.getPointDestination();
    }
  public Point getPointOrigine() {
        return pointOrigine;
  }
  public void setPointOrigine(Point pointOrigine) {
        this.pointOrigine = pointOrigine;
  }
  public Point getPointDestination() {
        return pointDestination;
  }
  public void setPointDestination(Point pointDestination) {
        this.pointDestination = pointDestination;
  }
}

