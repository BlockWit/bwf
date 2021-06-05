package com.blockwit.bwf.config;

import com.blockwit.bwf.service.chains.bsc.BSCChainScanner;
import com.blockwit.bwf.service.chains.eth.ETHChainScanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

@Component
public class AppContextListener implements ServletContextListener {

  @Autowired
  private BSCChainScanner bscChainScanner;

  @Autowired
  private ETHChainScanner ethChainScanner;

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    WebApplicationContext webApplicationContext = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    ethChainScanner.waitDestroy();
    bscChainScanner.waitDestroy();
  }

}
