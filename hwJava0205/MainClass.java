package hwJava0205;

import java.util.Arrays;

public class MainClass {
    static final int SIZE = 10_000_000;

    public static void main(String[] args) {

        arrayOneTread();
        arrayTreads(5);

    }

    public static void arrayOneTread() {
        float arr[] = new float[SIZE];
        long l;
        for (int i = 0; i < SIZE; i++) arr[i] = 1;
        l = System.currentTimeMillis();
        for (int i = 0; i < SIZE; i++) {
            arr[i] = (float) (arr[i] * Math.sin(0.2f + i / 5) * Math.cos(0.2f + i / 5) * Math.cos(0.4f + i / 2));
        }
        System.out.println("Без разбивки, затраченное время = " + (System.currentTimeMillis() - l));
        //    System.out.println(Arrays.toString(arr));
    }


    public static void arrayTreads(int parts) {
        int h = SIZE / parts;
        float arr[] = new float[SIZE];
        float arrpart[][] = new float[parts][h];
        long l;

        for (int i = 0; i < SIZE; i++) arr[i] = 1;
        l = System.currentTimeMillis();

        Thread[] threads = new Thread[parts];
        for (int i = 0; i < parts; i++) {
            threads[i] = new Thread(new MyRunnable(arr, arrpart, i, h));
            threads[i].start();
        }
        for (int i = 0; i < parts; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("При разбики на " + parts + " части, затраченное время = " + (System.currentTimeMillis() - l));
        // System.out.println(Arrays.deepToString(arrpart));
        // System.out.println(Arrays.toString(arr));

    }

    static class MyRunnable implements Runnable {
        float[] arr, arrpart[];
        int i, h;

        public MyRunnable(float[] arr, float[][] arrpart, int i, int h) {
            this.arr = arr;
            this.arrpart = arrpart;
            this.i = i;
            this.h = h;
        }

        @Override
        public void run() {
            // System.out.println("Start " + i);
            System.arraycopy(arr, 0, arrpart[i], 0, h);
            for (int j = 0; j < arrpart[i].length; j++) {
                arrpart[i][j] = (float) (arr[j] * Math.sin(0.2f + (j + h * i) / 5) * Math.cos(0.2f + (j + h * i) / 5) * Math.cos(0.4f + (j + h * i) / 2));
            }
            System.arraycopy(arrpart[i], 0, arr, h * i, h);
            // System.out.println("End " + i);
        }
    }
}