import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.netty.channel.*;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by fre on 2/16/16.
 */

@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private final static ConcurrentHashMap<String, Channel> remoteToSensor = new ConcurrentHashMap<>();

    private static final JsonParser parser = new JsonParser();
    private static final Gson GSON = new Gson();


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        remoteToSensor.put(ctx.name(), ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        final JsonObject json = (JsonObject) parser.parse(request);
        final DataToProcess obj = GSON.fromJson(json, DataToProcess.class);
        QueuerManager.getInstance().getQueuer().push(obj);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}