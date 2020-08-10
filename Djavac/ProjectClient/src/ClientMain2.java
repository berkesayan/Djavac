import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Stream;

public class ClientMain2 {
	private final static int TotalSamples = 300000000;
	private static int total = 0;
	private final static Object lock = new Object();
	private static int threadNumber = 2;
	static Stream<Integer> parallelarray;
	int denemeint = 100;

	public ClientMain2() {

	}

	public static void main(String[] args) throws SecurityException, ClassNotFoundException, FileNotFoundException {
		System.out.println(toplam(5, 3));
		System.out.println(deneme(5, 4));
		System.out.println(cikar(2020, 1994));
		System.out.println(carp(2020, 1995));
		System.out.println(carps(2020, 1995));
		System.out.println(asdqwe(2020, 1995));
		System.out.println(halil(2020, 1995));
		System.out.println(carpim(2020, 1995));
		System.out.println(carpma(2020, 1995));
		System.out.println(carpmama(2020, 1995));
		System.out.println(halo(2020, 1995));
		System.out.println(strDeneme());
		System.out.println(asdasd());
		System.out.println(bubizimdeneme());
		System.out.println(bubizimdene());
		System.out.println(bubizimdeN());
		System.out.println(berke());
		System.out.println(onbes());
		piApproximation();
	}

	@distributed
	public static void piApproximation() {
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

	@distributed
	public static int toplam(int x, int y) {
		return x + y;
	}

	@distributed
	public static int carpma(int x, int y) {
		return x * y;
	}

	@distributed
	public static int deneme(int x, int y) {
		return x + y;
	}

	@distributed
	public static int carpmama(int x, int y) {
		return x * y;
	}

	@distributed
	public static int cikar(int x, int y) {
		return x - y;
	}

	@distributed
	public static int carp(int x, int y) {
		return x * y;
	}

	@distributed
	public static int carps(int x, int y) {
		return x * y;
	}

	@distributed
	public static int asdqwe(int x, int y) {
		return x * y * 2;
	}

	@distributed
	public static int halil(int x, int y) {
		return x * y / 2;
	}

	@distributed
	public static int carpim(int x, int y) {
		return x * y;
	}

	@distributed
	public static int halo(int x, int y) {
		return 1994;
	}
	
	@distributed
	public static String strDeneme() {
		return "asdqwe";
	}
	
	@distributed
	public static String asdasd() {
		return "random";
	}

	@distributed
	public static String bubizimdeneme() {
		return "qweqweqweqwewqe NOLUR CALIS";
	}

		@distributed
	public static String bubizimdene() {
		return "ALLAHIM NOLUR CALISSIN";
	}

			@distributed
	public static String bubizimdeN() {
		return "GALIBA CALISTI";
	}
	@distributed
	public static String berke() {
		return "berke";
	}
	@distributed
	public static int onbes() {
		int a = 3;
		int b = 5;
		return a*b;
	}
}
