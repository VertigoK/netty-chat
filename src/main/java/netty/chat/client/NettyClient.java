package netty.chat.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyClient {

    private final String msg;
    private final String host;
    private final int port;

    public NettyClient(String msg, String host, int port) {
        this.msg = msg;
        this.host = host;
        this.port = port;
    }

    public void run() {
        //1. EventLoopGroup 생성
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            //2. Bootstrap 생성 및 구성
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(workerGroup);
            //Channel 생성시 사용할 class
            bootstrap.channel(NioSocketChannel.class);
            //TCP Channel 옵션 설정
            bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

            //3. ChannelInitializer 생성
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel channel) {
                    ChannelPipeline pipeline = channel.pipeline();
                    //Client Request를 처리할 Handler 등록
                    pipeline.addLast(new NettyClientHandler(msg));
                }
            });
            //Client connect
            //Channel 생성 후 기다림
            ChannelFuture future = bootstrap.connect(host, port).sync();
            //Channel이 닫힐 때까지 대기
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
        }
    }

}
