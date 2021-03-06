package br.com.giovanildo.main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;


public class Cliente {

	public static void main(String[] args) {
		new Cliente("127.0.0.1", 27289).executa();
		
	}
	
	private String host;
	private int porta;
	private Socket cliente;
	
	public Cliente(String host, int porta) {
		this.host = host;
		this.porta = porta;
	}
	
	public void executa() {
		
		//ping();
		
		try {
			cliente = new Socket(this.host, this.porta);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		
		System.out.println("O cliente se conectou ao servidor!");
		
		//thread para receber mensagens do servidor
		Recebedor r;
		try {
			r = new Recebedor(cliente.getInputStream());
			new Thread(r).start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//lê msgs do teclado e manda pro servidor
		Scanner teclado = new Scanner(System.in);
		PrintStream saida = null;
		try {
			saida = new PrintStream(cliente.getOutputStream());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		while(teclado.hasNextLine()) {
			saida.println(teclado.nextLine());
		}

		saida.close();
		teclado.close();
		
		try {
			cliente.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}

	private void ping() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while(true) {
					try {
						Thread.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					System.out.println(cliente.isConnected());
				}
			}
		});
		t.start();
	}
}
