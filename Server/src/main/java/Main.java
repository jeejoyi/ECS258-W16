import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import org.jfree.ui.RefineryUtilities;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Main {

    static final int PORT = 5005;

    public static void main(String[] args) throws Exception {


//        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
//        EventLoopGroup workerGroup = new NioEventLoopGroup();
//        try {
//            ServerBootstrap b = new ServerBootstrap();
//            b.group(bossGroup, workerGroup)
//                    .channel(NioServerSocketChannel.class)
//                    .handler(new LoggingHandler(LogLevel.INFO))
//                    .childHandler(new ChannelInitializerImpl());
//
//            b.bind(PORT).sync().channel().closeFuture().sync();
//        } finally {
//            bossGroup.shutdownGracefully();
//            workerGroup.shutdownGracefully();
//        }


        final String[] windowTitles = {"test"};
        final String[] plotTitles = {"A"};
        final String[] XAxisTitles = {"Time"};
        final String[] YAxisTitles = {"%"};
        final String[] seriesTitles = {"Usage"};
        final float[] maxYRange = {100};

        EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                final Plots analysisPlots = new Plots(windowTitles, plotTitles, XAxisTitles,
                        YAxisTitles, seriesTitles, maxYRange);

                Timer timer = new Timer(1000, new ActionListener() {


                    @Override
                    public void actionPerformed(ActionEvent e) {
                        analysisPlots.updateAllGraph();
                    }
                });
                timer.start();
            }
        });
    }
}
