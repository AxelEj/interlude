package test.net;

import java.io.IOException;

import net.Controller;

public class Test {


	public static void main(String[] args) {
		Controller controller;
		try {
			controller = new Controller();
			
			controller.start();
			
			controller.register(new ClientSocket("127.0.0.1", 2106));
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) { }
			
			controller.register(new Server("127.0.0.1",2106));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
