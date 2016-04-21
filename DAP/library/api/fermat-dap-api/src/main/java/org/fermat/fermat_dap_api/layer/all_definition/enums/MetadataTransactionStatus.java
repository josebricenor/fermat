package org.fermat.fermat_dap_api.layer.all_definition.enums;

import com.bitdubai.fermat_api.layer.all_definition.enums.interfaces.FermatEnum;
import com.bitdubai.fermat_api.layer.all_definition.exceptions.InvalidParameterException;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public enum MetadataTransactionStatus implements FermatEnum {

    //ENUM DECLARATION
    PENDING("PENDING"),
    SENT("SENT"),
    RECEIVE("RECEIVE"),
    CONFIRMED("CONFIRMED"),
    CANCELLED("CANCELLED")
    ;

    //VARIABLE DECLARATION

    private String code;

    //CONSTRUCTORS

    MetadataTransactionStatus(String code) {
        this.code = code;
    }

    //PUBLIC METHODS

    public static MetadataTransactionStatus getByCode(String code) throws InvalidParameterException {
        for (MetadataTransactionStatus fenum : values()) {
            if (fenum.getCode().equals(code)) return fenum;
        }
        throw new InvalidParameterException(InvalidParameterException.DEFAULT_MESSAGE, null, "Code Received: " + code, "This Code Is Not Valid for the MetadataTransactionStatus enum.");
    }

    //PRIVATE METHODS

    //GETTER AND SETTERS

    @Override
    public String getCode() {
        return code;
    }
}
