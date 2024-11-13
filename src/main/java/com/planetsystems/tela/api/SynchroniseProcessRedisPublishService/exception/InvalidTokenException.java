package com.planetsystems.tela.api.SynchroniseProcessRedisPublishService.exception;

public class InvalidTokenException extends RuntimeException{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public InvalidTokenException(String message) {
        super(message);
    }
}
