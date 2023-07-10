package it.finanze.sanita.fse2.ms.edssrvdictionary.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IChangeSetSRV;

@Configuration
@Profile("!" + Constants.Profile.TEST)
public class HistoryStorageCFG {

    @Autowired
    private IChangeSetSRV service;

    @EventListener(ApplicationStartedEvent.class)
    public void init() {
        service.initHistoryStorage();
    }

}
