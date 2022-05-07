package netty.chat.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class NettyServer {

    private final int port;

    public NettyServer(int port) {
        this.port = port;
    }

    public void run() {
        //1. EventLoopGroup 생성
        //boss thread는 ServerSocket을 Listen
        //worker thread는 만들어진 Channel에서 넘어온 이벤트 처리
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //2. ServerBootstrap 생성 및 구성
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(bossGroup, workerGroup);
            //Channel 생성시 사용할 class
            bootstrap.channel(NioServerSocketChannel.class);
            //TCP Channel 옵션 설정
            bootstrap.option(ChannelOption.SO_BACKLOG, 128);
            //accept되어 생성되는 TCP Channel 옵션 설정
            bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);

            //3. ChannelInitializer 생성
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();
                    //Client Request를 처리할 Handler 등록
                    pipeline.addLast(new NettyServerHandler());
                }
            });

            //Channel 생성 후 기다림
            ChannelFuture future = bootstrap.bind(port).sync();
            //Channel이 닫힐 때까지 대기
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
