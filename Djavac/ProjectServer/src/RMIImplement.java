import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;
import java.rmi.*;
import java.rmi.server.UnicastRemoteObject;
public class RMIImplement extends UnicastRemoteObject implements RMIInterface {
private static final long serialVersionUID = 1L;
protected RMIImplement() throws RemoteException {
super(); 
}
private final static int TotalSamples = 300000000;
private static int total = 0;
private final static Object lock = new Object();
private static int threadNumber = 2;
static Stream<Integer> parallelarray;
int denemeint = 100;
@Override
public int deneme(int x, int y){

{
		return x + y;
	}
}
@Override
public java.lang.String berke(){

{
		return "berke";
	}
}
@Override
public void piApproximation(){
{
		System.out.println("PI!!!");
		long startTime = System.nanoTime();
		total = 0;
		Integer[] Samples = new Integer[threadNumber];
		Arrays.fill(Samples, TotalSamples / threadNumber);
		parallelarray = Arrays.stream(Samples).sequential();
		if (threadNumber > 1) {
			parallelarray.forEach(s -> {
				int n = 0;
				for (int i = 0; i < s; ++i) {
					double x = Math.random();
					double y = Math.random();
					if (x * x + y * y <= 1) {
						n++;
					}
				}
				synchronized (lock) {
					total = total + n;
				}
			});
			long endTime = System.nanoTime();
			long duration = (endTime - startTime);
			System.out.println((duration / 1000000) + " ms");
			System.out.println("PI= " + total * 4.0 / TotalSamples);
		}
	}
}
@Override
public java.lang.String bubizimdeneme(){

{
		return "qweqweqweqwewqe NOLUR CALIS";
	}
}
@Override
public int asdqwe(int x, int y){

{
		return x * y * 2;
	}
}
@Override
public java.lang.String strDeneme(){

{
		return "asdqwe";
	}
}
@Override
public java.lang.String bubizimdene(){

{
		return "ALLAHIM NOLUR CALISSIN";
	}
}
@Override
public int carpmama(int x, int y){

{
		return x * y;
	}
}
@Override
public int carps(int x, int y){

{
		return x * y;
	}
}
@Override
public int carpim(int x, int y){

{
		return x * y;
	}
}
@Override
public int toplam(int x, int y){

{
		return x + y;
	}
}
@Override
public int halil(int x, int y){

{
		return x * y / 2;
	}
}
@Override
public int cikar(int x, int y){

{
		return x - y;
	}
}
@Override
public int halo(int x, int y){

{
		return 1994;
	}
}
@Override
public java.lang.String bubizimdeN(){

{
		return "GALIBA CALISTI";
	}
}
@Override
public int onbes(){

{
		int a = 3;
		int b = 5;
		return a*b;
	}
}
@Override
public int carpma(int x, int y){

{
		return x * y;
	}
}
@Override
public java.lang.String asdasd(){

{
		return "random";
	}
}
@Override
public int carp(int x, int y){

{
		return x * y;
	}
}
}
