package org.fermat.fermat_dap_api.layer.all_definition.listeners;

import com.bitdubai.fermat_api.FermatException;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventHandler;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventMonitor;

import org.fermat.fermat_dap_api.layer.all_definition.enums.EventType;
import org.fermat.fermat_dap_api.layer.all_definition.events.AbstractDAPEvent;

/**
 * This class is the default event listener for mostly of DAP Events.
 * <p/>
 * Created by Víctor A. Mars M. (marsvicam@gmail.com) on 19/04/16.
 */
public class DefaultDAPEventListener<Z extends AbstractDAPEvent> implements FermatEventListener<Z, EventType> {

    //VARIABLE DECLARATION

    private FermatEventMonitor fermatEventMonitor;
    private EventType eventType;
    private FermatEventHandler<Z> fermatEventHandler;

    //CONSTRUCTORS
    public DefaultDAPEventListener(EventType eventType, FermatEventMonitor fermatEventMonitor) {
        this.eventType = eventType;
        this.fermatEventMonitor = fermatEventMonitor;
    }


    //PUBLIC METHODS

    /**
     * Throw the method <code>raiseEvent</code> you can raise the event to be listened.
     *
     * @param fermatEvent an instance of fermat event to be listened.
     */
    @Override
    public void raiseEvent(Z fermatEvent) {
        try {
            fermatEventHandler.handleEvent(fermatEvent);
        } catch (FermatException e) {
            fermatEventMonitor.handleEventException(e, fermatEvent);
        }
    }
    //PRIVATE METHODS

    //GETTER AND SETTERS

    /**
     * Throw the method <code>getEventType</code> you can get the information of the event type.
     *
     * @return an instance of a Fermat Enum.
     */
    @Override
    public EventType getEventType() {
        return eventType;
    }

    /**
     * Throw the method <code>setEventHandler</code> you can set a handler for the listener.
     *
     * @param fermatEventHandler handler for the event listener.
     */
    @Override
    public void setEventHandler(FermatEventHandler<Z> fermatEventHandler) {
        this.fermatEventHandler = fermatEventHandler;
    }

    /**
     * Throw the method <code>getEventHandler</code>  you can get the handler assigned to the listener.
     *
     * @return an instance of FermatEventHandler.
     */
    @Override
    public FermatEventHandler getEventHandler() {
        return fermatEventHandler;
    }

    //INNER CLASSES
}
