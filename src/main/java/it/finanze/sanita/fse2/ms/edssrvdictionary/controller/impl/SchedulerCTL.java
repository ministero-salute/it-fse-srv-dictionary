package it.finanze.sanita.fse2.ms.edssrvdictionary.controller.impl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import it.finanze.sanita.fse2.ms.edssrvdictionary.controller.ISchedulerCTL;
import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.exceptions.OperationException;
import it.finanze.sanita.fse2.ms.edssrvdictionary.scheduler.WebScrapingScheduler;

@RestController
public class SchedulerCTL implements ISchedulerCTL {

	@Autowired
	private WebScrapingScheduler scheduler;

	@Override
	public MetadataResourceResponseDTO runScheduler(HttpServletRequest request) throws OperationException{
		return scheduler.run();
	}

}
