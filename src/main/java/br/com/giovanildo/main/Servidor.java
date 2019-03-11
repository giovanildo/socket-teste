package br.com.giovanildo.main;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
	private int porta;
	private List<Socket> listaClientes;
	private ServerSocket servidor;

	public static void main(String[] args) throws IOException {

		new Servidor(27289).executa();

	}

	public Servidor(int porta) {
		this.porta = porta;
		this.listaClientes = new ArrayList<Socket>();
		
	}

	private void executa() {

		try {
			servidor = new ServerSocket(this.porta);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Porta 27289 aberta");

		while (true) {
			// aceita um cliente
			Socket cliente = null;
			try {
				cliente = servidor.accept();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println("Nova conexão com o cliente " + cliente.getInetAddress().getHostAddress());
			// adiciona  o cliente a uma lista
			
			this.listaClientes.add(cliente);

			// cria tratador de cliente numa nova thread

			TrataCliente tc = null;
			try {
				tc = new TrataCliente(cliente.getInputStream(), this);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			new Thread(tc).start();

		}

	}

	public void distribuiMensagem(String msg) {
		// envia msg para todo mundo

		for (Socket cliente : this.listaClientes) {
			try {
				new PrintStream(cliente.getOutputStream()).println(cliente.getInetAddress().getHostAddress() + msg);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
