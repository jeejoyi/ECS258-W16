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
        //start server thread
        (new Thread(new ServerInit())).start();

        //sleep for 1000
        try {
            Thread.sleep(1000);
        }catch (Exception e){

        }

        //now start GUI
        (new Thread(AnalysisGUI.getInstance())).start();
    }
}
