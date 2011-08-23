package totalpos;

/**
 *
 * @author Saúl Hidalgo
 */
public class BankPOS {
    private String id;
    private String descripcion;
    private String lot;

    public BankPOS(String id, String descripcion, String lot) {
        this.id = id;
        this.descripcion = descripcion;
        this.lot = lot;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public String getId() {
        return id;
    }

    public String getLot() {
        return lot;
    }

}
