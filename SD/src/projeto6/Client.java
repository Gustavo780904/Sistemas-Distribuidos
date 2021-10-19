package projeto6;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Client {
	public static void main(String args[]) {
		Calc calculator;
		try {
		calculator = (Calc) Naming.lookup("rmi://localhost:2335/calc");
		int a = 3; // n da serie
		int b = 3;
		int f = calculator.getSoma(a,b);
		System.out.println(" A soma eh: " + f);
		}
		catch(MalformedURLException e) {
		System.out.println( "nao eh um URI RMI valida");
		}
		catch(RemoteException re) {
		System.err.println("Objeto Remoto tratou a execucao " + re);
		}
		catch(NotBoundException e) {
		System.out.println( "Nao foi possivel achar o objeto remoto no servidor");
		}
		}
}
