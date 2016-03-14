package network;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import data_type.DataToProcess;
import graph.AnalysisGUI;
import io.netty.channel.*;
import remote_sensor.QueuerManager;
import remote_sensor.RemoteSensor;
import remote_sensor.RemoteSensorManager;

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
        RemoteSensor remoteSensor = RemoteSensorManager.getInstance().addSensorChannel(ctx);
        QueuerManager.getInstance().addClient(remoteSensor);
        System.out.println(RemoteSensorManager.getInstance().getRemoteSensorsNamesList().size());
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
        if(json.has("operation") && ((json.get("operation").getAsString()).equals("data"))) {
            final DataToProcess obj = GSON.fromJson(json, DataToProcess.class);
            QueuerManager.getInstance().pushPacket(ctx.channel().id().asShortText(), obj);
//            System.out.println("REC Data Received " + ctx.channel().id().asShortText());
        }
        else    {
//            System.out.println("REC hello packet");
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }


    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        RemoteSensorManager.getInstance().getRemoteSensor(ctx.channel().id().asShortText()).setNetworkDead();
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