package it.finanze.sanita.fse2.ms.edssrvdictionary.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import it.finanze.sanita.fse2.ms.edssrvdictionary.dto.MetadataResourceResponseDTO;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.ITerminologySRV;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;


@Slf4j
@Component
public class WebScrapingScheduler {

	@Autowired
	private ITerminologySRV terminologySRV;

	@Scheduled(cron = "${scheduler.web-scraping}")
	@SchedulerLock(name = "invokeWebScrapingScheduler" , lockAtMostFor = "60m")
	public void action() {
		log.info("START - WEB SCRAPING SCHEDULER");
		run();
		log.info("END - WEB SCRAPING SCHEDULER");
	}

	public MetadataResourceResponseDTO run() {
		return terminologySRV.callQueryToManageMetadataResource();
	}
}
