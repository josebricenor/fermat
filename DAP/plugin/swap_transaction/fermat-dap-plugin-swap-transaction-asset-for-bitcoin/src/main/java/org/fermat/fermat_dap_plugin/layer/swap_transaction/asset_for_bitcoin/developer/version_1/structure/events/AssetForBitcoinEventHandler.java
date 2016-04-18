package org.fermat.fermat_dap_plugin.layer.swap_transaction.asset_for_bitcoin.developer.version_1.structure.events;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.enums.ServiceStatus;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEvent;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.dmp_transaction.TransactionServiceNotStartedException;

import org.fermat.fermat_dap_api.layer.dap_transaction.common.exceptions.CantSaveEventException;

/**
 * Created by ?? (??@gmail.com) on ??/??/16.
 */
public class AssetForBitcoinEventHandler implements FermatEventHandler {

    //VARIABLE DECLARATION
    private AssetForBitcoinRecorderService recorderService;

    //CONSTRUCTORS

    public AssetForBitcoinEventHandler(AssetForBitcoinRecorderService recorderService) {
        this.recorderService = recorderService;
    }

    //PUBLIC METHODS
    @Override
    public void handleEvent(FermatEvent fermatEvent) throws FermatException {
        if (fermatEvent == null)
            throw new CantSaveEventException(null, "Handling an asset buyer event", "Illegal Argument, this method takes an fermatEvent and was passed an null");

        System.out.println("VAMM: ASSET BUYER RECEIVED A NEW EVENT!");
        System.out.println("VAMM: Type: " + fermatEvent.getEventType() + " - Source: " + fermatEvent.getSource());

        if (recorderService.getStatus() != ServiceStatus.STARTED) {
            throw new TransactionServiceNotStartedException();
        }
        recorderService.receiveNewEvent(fermatEvent);
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    //INNER CLASSES
}
