package it.finanze.sanita.fse2.ms.edssrvdictionary.service;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.response.changes.query.HistoryDTO;

import java.util.Date;

public interface IChangeSetSRV {

    HistoryDTO history(Date lastUpdate);

    long size();

}
