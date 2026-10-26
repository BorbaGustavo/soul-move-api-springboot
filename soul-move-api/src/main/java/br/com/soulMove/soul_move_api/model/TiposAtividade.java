package br.com.soulMove.soul_move_api.model;

public enum TiposAtividade {
    RECICLAGEM,
    TRANSPORTE_PUBLICO,
    TRAJETO_CAMINHADA,
    TRAJETO_BIKE,
    REDUCAO_ENERGIA;

    /** Converte String do banco de volta para o enum. */
    public static TiposAtividade fromString(String valor) {
        if (valor == null) return null;
        try {
            return TiposAtividade.valueOf(valor.toUpperCase().replace(" ", "_"));
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
