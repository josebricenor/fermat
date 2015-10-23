package com.bitdubai.fermat_p2p_api.layer.p2p_communication.fmp;

public class MalformedFMPPacketException extends FMPException {
	
	/**
	 * autogenerated serialVersionUID
	 */
	private static final long serialVersionUID = -1102140907777389730L;

	public static final String DEFAULT_MESSAGE = "MALFORMED FMP PACKET";

	public MalformedFMPPacketException(final String message, final Exception cause, final String context, final String possibleReason){
		super(message, cause, context, possibleReason);
	}
	
}