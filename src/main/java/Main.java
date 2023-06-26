import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static AtomicInteger threeLetters = new AtomicInteger(0);
    public static AtomicInteger fourLetters = new AtomicInteger(0);
    public static AtomicInteger fiveLetters = new AtomicInteger(0);
    public static void main(String[] args) {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        Thread palindromCheck = new Thread(() -> {
            for(String s : texts) {
                if(s.equals(new StringBuilder(s).reverse().toString())){
                    AddToCount(s);
                }
            }
        });
        palindromCheck.start();
        Thread oneLetterCheck = new Thread(() -> {
            for(String s : texts) {
                char firstLetter = s.charAt(0);
                boolean check = false;
                for(int i = 0; i < s.length(); i++){
                    check = firstLetter == s.charAt(i);
                }
                if(check){
                    AddToCount(s);
                }

            }
        });
        oneLetterCheck.start();
        Thread lettersToRise = new Thread(() -> {
           for(String s : texts){
               if(s.equals(s.chars().sorted().toString())) {
                   AddToCount(s);
               }
           }
        });
        lettersToRise.start();
        try {
            palindromCheck.join();
            oneLetterCheck.join();
            lettersToRise.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Красивых слов с длиной 3: " + threeLetters + " шт." +
                "\nКрасивых слов с длиной 4: " + fourLetters + " шт." +
                "\nКрасивых слов с длиной 5: " + fiveLetters + " шт.");


    }
    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
    public static void AddToCount(String word) {
        if(word.length() == 3) {
            threeLetters.getAndIncrement();
        } else if(word.length() == 4){
            fourLetters.getAndIncrement();
        } else if(word.length() == 5){
            fiveLetters.getAndIncrement();
        }
    }
}
