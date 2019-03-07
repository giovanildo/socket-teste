package br.com.giovanildo.main;

import java.io.InputStream;
import java.util.Scanner;
/**
 * 
 * esse tred faz toda a mensagem que chega do servidor 
 * ser impressa na tela
 *
 */
public class Recebedor implements Runnable {
	
	private InputStream servidor;
	private Scanner s;
	
	public Recebedor(InputStream servidor) {
		this.servidor = servidor;
	}

	public void run() {
		s = new Scanner(this.servidor);
		while(s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
	}
	
}
