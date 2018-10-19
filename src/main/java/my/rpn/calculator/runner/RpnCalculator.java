package my.rpn.calculator.runner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Stack;

import javax.annotation.Resource;
import javax.sql.CommonDataSource;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteQueue;
import org.apache.ignite.configuration.CollectionConfiguration;
import org.springframework.beans.BeansException;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import my.rpn.calculator.constant.Constant;
import my.rpn.calculator.utils.IgniteUtils;

@Component
@Order(1)
public class RpnCalculator implements CommandLineRunner{

	@Resource
	private IgniteUtils igniteUtils;
	
	private Stack<String> stack = new Stack<>();

	@Override
	public void run(String... arg0) throws Exception {
		// TODO Auto-generated method stub
		Ignite ignite = igniteUtils.getInstance();
		IgniteQueue<String> wordQueue = ignite.queue("WordInput", 0, new CollectionConfiguration());
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				while(true) {
					String word = wordQueue.take();
					if (Constant.OPERATOR.contains(word)) {
						BigDecimal a = BigDecimal.valueOf(Double.parseDouble(stack.pop()));
						BigDecimal b = BigDecimal.valueOf(Double.parseDouble(stack.pop()));
						BigDecimal r = null;
						
					    switch (word) {
					    	case "+":
								r = a.add(b).setScale(10, BigDecimal.ROUND_HALF_DOWN);
								stack.push(""+r.doubleValue());
					    		break;    
					    	case "-":
								r = b.subtract(a).setScale(10, BigDecimal.ROUND_HALF_DOWN);
								stack.push(""+r.doubleValue());
					    		break;
					    	case "*":
								r = a.multiply(b).setScale(10, BigDecimal.ROUND_HALF_DOWN);
								stack.push(""+r.doubleValue());
					    		break;    
					    	case "/":
								r = b.divide(a).setScale(10, BigDecimal.ROUND_HALF_DOWN);
								stack.push(""+r.doubleValue());
					    		break;   
			    		}
					}else if (Constant.CLEAR.equals(word)){
						stack.clear();
					}else if(Constant.LINEEND.equals(word)) {
						for (String s : stack) {
							System.out.print(s + " ");
						}
						System.out.println();
					}else {
						stack.push(word);
					}
				}
			}
		}).start();
	}

	
	
}
