package org.fermat.fermat_dap_plugin.layer.negotiation_transaction.direct_sell_negotiation.developer.bitdubai.version_1.structure.events;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;
import com.bitdubai.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;

/**
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 06/11/15.
 */
public class NegotiationDirectSellEventHandler implements FermatEventHandler {

    //VARIABLE DECLARATION
    private NegotiationDirectSellRecorderService recorderService;

    //CONSTRUCTORS

    public NegotiationDirectSellEventHandler(NegotiationDirectSellRecorderService recorderService) {
        this.recorderService = recorderService;
    }

    //PUBLIC METHODS

    /**
     * Throw the method <code>handleEvent</code> you can handle the fermat event.
     *
     * @param fermatEvent event to be handled.
     * @throws FermatException if something goes wrong.
     */
    @Override
    public void handleEvent(FermatEvent fermatEvent) throws FermatException {
        if (fermatEvent == null)
            throw new CantSaveEventException(null, "Handling the ReceivedNewDigitalAssetMetadataNotificationEvent", "Illegal Argument, this method takes an ReceivedNewDigitalAssetMetadataNotificationEvent and was passed an null");

        System.out.println("VAMM: DIRECT SELL NEGOTIATION RECEIVED A NEW EVENT!");
        System.out.println("VAMM: Type: " + fermatEvent.getEventType() + " - Source: " + fermatEvent.getSource());

        if (recorderService.getStatus() != ServiceStatus.STARTED) {
            throw new TransactionServiceNotStartedException();
        }
    }

    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}