package enums;

/**
 * Tipos de unidad que puede tener la jerarquía de la biblioteca.
 * Regla: BIBLIOTECA -> AREA -> ESTANTE.
 */
public enum TipoUnidad {
    BIBLIOTECA,
    AREA,
    ESTANTE;

    /**
     * Indica si una unidad de este tipo puede tener como hijo una unidad del tipo dado.
     */
    public boolean puedeTenerHijo(TipoUnidad hijo) {
        if (hijo == null) {
            return false;
        }
        switch (this) {
            case BIBLIOTECA:
                return hijo == AREA;
            case AREA:
                return hijo == ESTANTE;
            default:
                return false;
        }
    }
}
