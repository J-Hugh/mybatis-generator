package main;

import main.base.HandlerBase;
import main.base.impl.GenCodeHandler;

/**
 * 
 * @author wangzhichao
 *
 */
public class main {
	
	public static void main(String[] args){
		
		HandlerBase handler = new GenCodeHandler();
		try {
			handler.run();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
