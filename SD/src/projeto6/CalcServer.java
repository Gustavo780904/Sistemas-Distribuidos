package projeto6;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class CalcServer extends UnicastRemoteObject implements Calc {

	private static final long serialVersionUID = 1L;

	public CalcServer() throws RemoteException {
		super();
	}

	public static void main(String[] args) {
		try {
			LocateRegistry.createRegistry(2335);
			CalcServer f = new CalcServer();
			Naming.rebind("//localhost:2335/calc", f);
			System.out.println("Servidor Calculadora pronto");

		} catch (RemoteException re) {
			// TODO: handle exception
			System.out.println(" Exception in " + re);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public int getSoma(int a, int b) throws RemoteException {
		System.out.println(" Calculando a soma de " + a + " + " + b);
		return a + b;
	}

	public int getSubtracao(int a, int b) throws RemoteException {
		System.out.println(" Calculando a subtracao de " + a + " - " + b);
		return a - b;
	}

	public int getMultiplicacao(int a, int b) throws RemoteException {
		System.out.println(" Calculando a multiplicacao de " + a + " * " + b);
		return a * b;
	}

	public double getDivisao(int a, int b) throws RemoteException {
		System.out.println(" Calculando a divisao de " + a + " / " + b);
		return (double) a / b;
	}

}
