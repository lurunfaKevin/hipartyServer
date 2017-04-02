package listener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import beans.Lab;
import beans.Room;
import beans.RoomUser;
import handler.UserHandler;
import utils.HibernateUtil;

/**
 * Application Lifecycle Listener implementation class ContextListener
 *
 */
@WebListener
public class ContextListener implements ServletContextListener {
	private NioSocketAcceptor acceptor = null;
    /**
     * Default constructor. 
     */
    public ContextListener() {

    }

    public void contextDestroyed(ServletContextEvent arg0)  { 
    	HibernateUtil.getSessionFactory().close();
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0)  {
		TestLab();
    	
    	acceptor=new NioSocketAcceptor();
    	acceptor.setReuseAddress(true);
    	acceptor.getSessionConfig().setReadBufferSize(2048);
    	acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 10);
    	acceptor.getFilterChain().addLast("logger", new LoggingFilter());
    	acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("utf-8"))));
    	acceptor.setHandler(new UserHandler());
    	
    	try {
			acceptor.bind(new InetSocketAddress(9999));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// TODO Auto-generated method stub
    }
    private void TestLab(){
    	System.out.println("初始化房间");
		RoomUser roomuser= new RoomUser();
		roomuser.setNickname("aarf");
		roomuser.setUserId("123");
//		roomuser.setIntroduction("agiaerg");
		Room room= new Room();
		room.setRoomId("1234");
		room.setRoomname("Test");
		room.setRoomnum(1);
		room.setUserlist(roomuser);
		Lab lab=Lab.getLab();
		lab.setList(room);
	}
}
