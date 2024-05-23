import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    static int[] numbers = new int[]{};

    public static void main(String[] args) {
        try {
            loadFile("");
            long min = _min();
            long max = _max();
            long sum = _sum();
            long mult = _mult();
            System.out.println("Min: " + min);
            System.out.println("Max: " + max);
            System.out.println("Sum: " + sum);
            System.out.println("Mult: " + mult);
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        } catch (WrongDataException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFile(String fileName) throws FileNotFoundException, WrongDataException {
        if (fileName.isEmpty()) {
            fileName = "numm.rtf";
        }
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        String numbersStr = scanner.nextLine();
        scanner.close();
        String[] numbersArr = numbersStr.split(" ");
        if (numbersArr.length > 1000000) {
            throw new WrongDataException("Too much numbers in a file.");
        }
        numbers = new int[numbersArr.length];
        for (int i = 0; i < numbersArr.length; i++) {
            numbers[i] = Integer.parseInt(numbersArr[i]);
        }
    }

    public static long _min() {
        int min = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] < min) {
                min = numbers[i];
            }
        }
        return min;
    }

    public static long _max() {
        int max = numbers[0];
        for (int i = 1; i < numbers.length; i++) {
            if (numbers[i] > max) {
                max = numbers[i];
            }
        }
        return max;
    }

    public static long _sum() {
        long sum = 0;
        for (int i : numbers) {
            sum += i;
        }
        return sum;
    }

    public static long _mult() {
        long mult = 1;
        for (long i : numbers) {
            mult *= i;
            if (mult == 0) {
                break;
            }
        }
        return mult;
    }
}
