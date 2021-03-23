package com.innovat.authJwt.exception;

public class ExpiredSessionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8293626885694428335L;

	private static String msg = "La sessione è scaduta, si prega di effettuare il login.";
		
		public ExpiredSessionException() {
			super(msg);
		}
		
		public ExpiredSessionException(String msg) {
			super(msg);
		}
	
		
}
