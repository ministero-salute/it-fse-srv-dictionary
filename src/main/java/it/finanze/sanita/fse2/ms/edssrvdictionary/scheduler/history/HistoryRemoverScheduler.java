package it.finanze.sanita.fse2.ms.edssrvdictionary.scheduler.history;

import it.finanze.sanita.fse2.ms.edssrvdictionary.repository.entity.resources.ChunksIndexETY;
import it.finanze.sanita.fse2.ms.edssrvdictionary.service.IChangeSetSRV;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class HistoryRemoverScheduler {

    @Autowired
    private IChangeSetSRV service;

    @Scheduled(cron = "${scheduler.history-remover}")
    @SchedulerLock(name = "invokeHistoryRemoverScheduler" , lockAtMostFor = "60m")
    public void action() {
        log.info("START - HISTORY REMOVER SCHEDULER");
        log.info("Running scheduler to remove obsolete chunks");
        // Get removed chunks
        List<ChunksIndexETY> indexes = service.clearIndexes();
        // Log them
        for (ChunksIndexETY index : indexes) {
            log.info(
                "Removed resource {}/{}/{}",
                index.getMeta().getType(),
                index.getResource(),
                index.getVersion()
            );
        }
        log.info("Removed {} resources", indexes.size());
        log.info("END - HISTORY REMOVER SCHEDULER");
    }

}
