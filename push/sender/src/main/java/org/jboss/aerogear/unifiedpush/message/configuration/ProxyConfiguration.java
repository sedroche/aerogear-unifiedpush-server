package org.jboss.aerogear.unifiedpush.message.configuration;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;

@Startup
@Singleton
/**
 * responsible for handling authentication challenges from outbound HTTP_PROXY
 */
public class ProxyConfiguration {

  @PostConstruct
  public void setupAuth(){
    Authenticator.setDefault(new Authenticator() {

      @Override protected PasswordAuthentication getPasswordAuthentication() {
        String proxyHost = System.getenv("HTTP_PROXY_HOST");
        String proxyUser = System.getenv("HTTP_PROXY_USER");
        String proxyPass = System.getenv("HTTP_PROXY_PASS");
        String hostAskingForAuth = this.getRequestingHost();
        if(hostAskingForAuth.equals(proxyHost) && (! "".equals(proxyUser) && ! "".equals(proxyPass))){
          return new PasswordAuthentication(proxyUser,proxyPass.toCharArray());
        }
        return super.getPasswordAuthentication();
      }
    });
  }

  public static Proxy socks(){
    String socksHost = System.getenv("SOCKS_PROXY_HOST");
    String socksPort = System.getenv("SOCKS_PROXY_PORT");
    int port = Integer.parseInt(socksPort);
    return new Proxy(Proxy.Type.SOCKS,new InetSocketAddress(socksHost,port));
  }

  public static Boolean hasSocks(){
    String socksHost = System.getenv("SOCKS_PROXY_HOST");
    String socksPort = System.getenv("SOCKS_PROXY_PORT");
    return ((null != socksPort && ! "".equals(socksPort)) && (null != socksHost && ! "".equals(socksHost)));
  }
}
