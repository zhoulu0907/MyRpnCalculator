package my.rpn.calculator.utils;

import org.apache.ignite.Ignite;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class IgniteUtils implements ApplicationContextAware{

	private Ignite ignite;
	@Override
	public void setApplicationContext(ApplicationContext cnt) throws BeansException {
		// TODO Auto-generated method stub
		this.ignite = cnt.getBean(Ignite.class);
	}

	public Ignite getInstance() {
		return this.ignite;
	}
}
