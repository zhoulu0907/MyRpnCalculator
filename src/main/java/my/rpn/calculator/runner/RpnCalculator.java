package my.rpn.calculator.runner;

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

import my.rpn.calculator.constant.CommandConstant;
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
					if (CommandConstant.OPERATOR.contains(word)) {
					    int a = Integer.valueOf(stack.pop());    
					    int b = Integer.valueOf(stack.pop());    
					    switch (word) {
					    	case "+":     
					    		stack.push(String.valueOf(a + b));
					    		break;    
					    	case "-":     
					    		stack.push(String.valueOf(b - a));
					    		break;
					    	case "*":
					    		stack.push(String.valueOf(a * b));
					    		break;    
					    	case "/":     
					    		stack.push(String.valueOf(b / a));
					    		break;   
			    		}
					}else if(CommandConstant.LINEEND.equals(word)) {
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
