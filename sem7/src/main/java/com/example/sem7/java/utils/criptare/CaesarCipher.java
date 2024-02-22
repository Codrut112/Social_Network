package com.example.sem7.java.utils.criptare;

public class CaesarCipher {
    public static String encrypt(String text, int shift) {
        StringBuilder result = new StringBuilder();

        for (char character : text.toCharArray()) {
            if (Character.isLetter(character)) {
                char base = Character.isUpperCase(character) ? 'A' : 'a';
                result.append((char) ((character - base + shift + 26) % 26 + base));
            } else {
                result.append(character);
            }
        }

        return result.toString();
    }

    public static String decrypt(String ciphertext, int shift) {
        return encrypt(ciphertext, -shift);
    }
}
