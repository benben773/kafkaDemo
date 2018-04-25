package gwssi.kafka.demo.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

@WebServlet(urlPatterns = "/produce", description = "Servlet的说明")
public class ProduceServlet extends HttpServlet {

	private static Logger logger = LoggerFactory.getLogger(ProduceServlet.class);
	private static final long serialVersionUID = 1L;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@Override    
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
     
    doPost(req,resp);
     
    }
    @Override    
    protected void doPost(HttpServletRequest request, HttpServletResponse resp)throws ServletException, IOException {
     
    	try {
			request.setCharacterEncoding("UTF-8");
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}    	
    	String reqStr = request.getParameter("reqStr");   
    	logger.info(reqStr);
		kafkaTemplate.send("t1234", System.currentTimeMillis()+"",reqStr);
     
    }
    @Override
 	  public void init() throws ServletException {  
 	        super.init();         
 	        ServletContext application = this.getServletContext();  
 	        // 解决servlet用@Autowired自动注入service失败的问题  
 	        SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, application); 
 	            
 	    }  
    
}
