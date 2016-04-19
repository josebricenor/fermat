package org.fermat.fermat_dap_api.layer.dap_metadata_transaction.exceptions;

import org.fermat.fermat_dap_api.layer.all_definition.exceptions.DAPException;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public class CantStartMetadataTransactionException extends DAPException {

    //VARIABLE DECLARATION
    public static final String DEFAULT_MESSAGE = "There was a problem while trying to start the metadata transaction.";

    //CONSTRUCTORS

    public CantStartMetadataTransactionException(Exception cause, String context, String possibleReason) {
        super(cause, context, possibleReason);
    }

    public CantStartMetadataTransactionException(Exception cause) {
        super(DEFAULT_MESSAGE, cause);
    }

    public CantStartMetadataTransactionException() {
        super(DEFAULT_MESSAGE);
    }

    //PUBLIC METHODS

    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
