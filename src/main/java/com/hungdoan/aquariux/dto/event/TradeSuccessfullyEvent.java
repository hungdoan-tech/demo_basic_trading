package com.hungdoan.aquariux.dto.event;

import com.hungdoan.aquariux.model.Trade;
import org.springframework.context.ApplicationEvent;

/**
 * Could be used to send notification or realtime stuff
 */
public class TradeSuccessfullyEvent extends ApplicationEvent {

    private final Trade trade;

    public TradeSuccessfullyEvent(Object source, Trade trade) {
        super(source);
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }
}
