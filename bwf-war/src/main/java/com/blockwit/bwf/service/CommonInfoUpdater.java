package com.blockwit.bwf.service;

import com.blockwit.bwf.model.chain.Chains;
import com.blockwit.bwf.model.tasks.SystemSchedTasks;
import com.blockwit.bwf.service.chains.ChainsInfo;
import com.blockwit.bwf.service.chains.bsc.service.BSCServicePool;
import com.blockwit.bwf.service.chains.common.utils.PerformSchedTask;
import com.blockwit.bwf.service.chains.common.utils.WithOptionsProfiled;
import com.blockwit.bwf.service.chains.eth.service.ETHServicePool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Service
public class CommonInfoUpdater {

  @Autowired
  private BSCServicePool bscServicePool;

  @Autowired
  private ETHServicePool ethServicePool;

  @Autowired
  private SchedTasksService schedTasksService;

  @Autowired
  ChainsInfo chainInfo;

  @Autowired
  OptionService optionService;

  private AtomicBoolean destroy = new AtomicBoolean(false);
  private AtomicBoolean destroyFinished = new AtomicBoolean(false);

  @PreDestroy
  public void waitDestroy() {
    log.info("Waiting for common updater processor destroy");
    destroy.set(true);
    while (!destroyFinished.get()) {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    log.info("Common updater processor destroy process finished");
  }

  @Scheduled(fixedDelay = 1000)
  protected void commonInfoUpdater() {
    PerformSchedTask.process(destroy,
        destroyFinished,
        schedTasksService,
        SystemSchedTasks.TASK_ID_COMMON_INFO_UPDATER,
        bscServicePool,
        ethServicePool, (bscService, ethService) -> {

          chainInfo.setBscFreeTokens(bscService.getFreeTokens().getOrElse(chainInfo.getBscFreeTokens()));
          chainInfo.setEthFreeTokens(ethService.getFreeTokens().getOrElse(chainInfo.getEthFreeTokens()));
          chainInfo.setBscAllTokens(bscService.getAllTokens().getOrElse(chainInfo.getBscAllTokens()));
          chainInfo.setEthAllTokens(ethService.getAllTokens().getOrElse(chainInfo.getEthAllTokens()));
          chainInfo.setBscInvokerBalance(bscService.getInvokerBalance().getOrElse(chainInfo.getBscInvokerBalance()));
          chainInfo.setEthInvokerBalance(ethService.getInvokerBalance().getOrElse(chainInfo.getEthInvokerBalance()));

          long bscHeight = bscService.getHeight().getOrElse(chainInfo.getBscHeight());
          long ethHeight = ethService.getHeight().getOrElse(chainInfo.getEthHeight());

          chainInfo.setEthHeight(ethHeight);
          chainInfo.setBscHeight(bscHeight);

          WithOptionsProfiled.process(optionService,
              List.of(Chains.Ethereum + OptionService.OPTION_Chain_CURRENT_BLOCK,
                  Chains.Ethereum + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT,
                  Chains.BinanceSmartChain + OptionService.OPTION_Chain_CURRENT_BLOCK,
                  Chains.BinanceSmartChain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT),
              options -> {

                Long ethForkProtect = (Long) options.get(Chains.Ethereum + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT).getPerformedValue();
                Long bscForkProtect = (Long) options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_FORK_PREVENT_BLOCKS_COUNT).getPerformedValue();
                Long ethCurBlock = (Long) options.get(Chains.Ethereum + OptionService.OPTION_Chain_CURRENT_BLOCK).getPerformedValue();
                Long bscCurBlock = (Long) options.get(Chains.BinanceSmartChain + OptionService.OPTION_Chain_CURRENT_BLOCK).getPerformedValue();

                if (bscHeight != 0) {
                  long bscTargetHeight = bscHeight - bscForkProtect;
                  if (bscTargetHeight > 0 && bscCurBlock > 0) {
                    chainInfo.setBscSyncProgress((int) (bscTargetHeight - bscCurBlock));
                  }
                }

                if (ethHeight != 0) {
                  long ethTargetHeight = ethHeight - ethForkProtect;
                  if (ethTargetHeight > 0 && ethCurBlock > 0) {
                    chainInfo.setEthSyncProgress((int) (ethTargetHeight - ethCurBlock));
                  }
                }

              });

        });
  }

}
