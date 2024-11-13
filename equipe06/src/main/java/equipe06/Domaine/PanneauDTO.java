package equipe06.Domaine;
import equipe06.Domaine.Utils.ZoneInterdite;
import java.util.UUID;
import equipe06.Domaine.Panneau;

/**
 *
 * @author ziedd
 */
public class PanneauDTO {
    
    public float longueurDTO ;
    public float largeurDTO;
    public float profondeurDTO;
    public ZoneInterdite zoneInterditeDTO;
    public UUID UuidDTO;
    
    public PanneauDTO (Panneau panneau) {
        longueurDTO = panneau.getLongueur();
        largeurDTO = panneau.getLargeur();
        profondeurDTO = panneau.getProfondeur();
        zoneInterditeDTO = panneau.getZoneInterdite();      
    }
    
    
    public float getLongueur() { return longueurDTO; }
    public float getLargeur() { return largeurDTO; }
    public float getProfondeur() { return profondeurDTO; }
    public ZoneInterdite getZoneInterdite() { return zoneInterditeDTO; }
    public UUID getUUID() { return UuidDTO; }
    
}
