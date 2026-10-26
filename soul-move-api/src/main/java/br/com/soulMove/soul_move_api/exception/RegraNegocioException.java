package br.com.soulMove.soul_move_api.exception;

/** Regra de negócio violada (saldo insuficiente, limite diário, e-mail repetido). HTTP 409 */
public class RegraNegocioException extends RuntimeException {
    public RegraNegocioException(String mensagem) { super(mensagem); }
}