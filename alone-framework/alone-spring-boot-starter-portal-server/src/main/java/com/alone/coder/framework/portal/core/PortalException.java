package com.alone.coder.framework.portal.core;

public class PortalException extends Exception {

	public PortalException(String message) {
		super(message);
	}

	public PortalException(Throwable cause) {
		super(cause);
	}

	public PortalException(String message, Throwable cause) {
		super(message, cause);
	}

	private static final long serialVersionUID = 2201204523946051388L;

}
