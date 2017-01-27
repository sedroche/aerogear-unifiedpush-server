package org.jboss.aerogear.unifiedpush.message.configuration;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

@Startup
@Singleton
/**
 * responsible for handling authentication challenges from outbound HTTP_PROXY
 */
public class ProxyConfiguration {

  @PostConstruct
  public void setupAuth(){
    System.out.println("*** setup auth called ***");
    Authenticator.setDefault(new Authenticator() {

      @Override protected PasswordAuthentication getPasswordAuthentication() {
        System.out.println("*** getPasswordAuthentication called ***");
        String proxyHost = System.getenv("HTTP_PROXY_HOST");
        String proxyUser = System.getenv("HTTP_PROXY_USER");
        String proxyPass = System.getenv("HTTP_PROXY_PASS");
        System.out.println("*** getPasswordAuthentication called ***" + proxyHost + "," + proxyPass + "," +proxyUser);
        String hostAskingForAuth = this.getRequestingHost();
        if(hostAskingForAuth.equals(proxyHost)){
          return new PasswordAuthentication(proxyUser,proxyPass.toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });
  }
}
