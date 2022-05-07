package netty.chat.controller;

import netty.chat.server.NettyServer;
import org.springframework.stereotype.Controller;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Controller
public class NettyController {

    private static final int SERVER_PORT = 5010;

    @PostConstruct
    private void start() {
        new Thread(() -> {
            try {
                new NettyServer(SERVER_PORT).run();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    @PreDestroy
    private void destroy() {
    }

}
