package br.com.soulMove.soul_move_api.exception;

/** Login com e-mail ou senha errados. HTTP 401 */
public class CredenciaisInvalidasException extends RuntimeException {
    public CredenciaisInvalidasException() { super("E-mail ou senha inválidos."); }
}
