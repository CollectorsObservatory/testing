package equipe06.Domaine.Utils;

public class ZoneInterditeDTO {
    private float longueurDTO;
    private float largeurDTO;
    private float DistanceToXDTO;
    private float DistanceToYDTO;

    public ZoneInterditeDTO(ZoneInterdite zoneInterdite) {
        if (zoneInterdite != null) {
            this.longueurDTO = zoneInterdite.getLongueur();
            this.largeurDTO = zoneInterdite.getLargeur();
            this.DistanceToXDTO = zoneInterdite.getDistanceToX();
            this.DistanceToYDTO = zoneInterdite.getDistanceToY();
        }
    }

    public float getLongueurDTO() {
        return longueurDTO;
    }

    public void setLongueurDTO(float longueurDTO) {
        this.longueurDTO = longueurDTO;
    }

    public float getLargeurDTO() {
        return largeurDTO;
    }

    public void setLargeurDTO(float largeurDTO) {
        this.largeurDTO = largeurDTO;
    }

    public float getDistanceToXDTO() {
        return DistanceToXDTO;
    }

    public void setDistanceToXDTO(float DistanceToXDTO) {
        this.DistanceToXDTO = DistanceToXDTO;
    }

    public float getDistanceToYDTO() {
        return DistanceToYDTO;
    }

    public void setDistanceToYDTO(float DistanceToYDTO) {
        this.DistanceToYDTO = DistanceToYDTO;
    }
}
