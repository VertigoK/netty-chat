package netty.chat.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    private final String msg;

    public NettyClientHandler(String msg) {
        this.msg = msg;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ByteBuf outBuffer = Unpooled.buffer();
        outBuffer.writeBytes(msg.getBytes());
        System.out.println("Client sent: " + msg);
        //write to buffer then flush
        ctx.writeAndFlush(outBuffer);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf inBuffer = (ByteBuf) msg;
        String messageReceived = inBuffer.toString(StandardCharsets.UTF_8);
        System.out.println("Client received: " + messageReceived);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
