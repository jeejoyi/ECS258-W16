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

    private static final JsonParser parser = new JsonParser();
    private static final Gson GSON = new Gson();


    /**
     * Handler of a new connection
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        RemoteSensorManager.getInstance().addSensorChannel(ctx);
        System.out.println(RemoteSensorManager.getInstance().getRemoteSensorsNamesList().size());
        AnalysisGUI.getInstance().drawLayout();
    }

    /**
     * Handler of received messages
     *
     * @param ctx
     * @param request
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, String request) throws Exception {
        final JsonObject json = (JsonObject) parser.parse(request);
        final DataToProcess obj = GSON.fromJson(json, DataToProcess.class);
        QueuerManager.getInstance().pushPacket(ctx.channel().id().asLongText(), obj);
        System.out.println("Data Received " + ctx.channel().id().asLongText());
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    /**
     * Handler of a potential exception
     *
     * @param ctx
     * @param cause
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}