package com.example.Competative;

import java.util.HashMap;
import java.util.Map;

public class TrieTree {

        static class TrieNode {
            Map<Character, TrieNode> children = new HashMap<>();
            boolean isEndOfWord;
        }

        private final TrieNode root = new TrieNode();

        // Map obfuscated characters to real ones
        private static final Map<Character, Character> NORMALIZATION_MAP = Map.of(
                '@', 'a',
                '!', 'i',
                '1', 'l',
                '3', 'e',
                '$', 's',
                '0', 'o'
        );

        public void addWord(String word) {
            TrieNode node = root;
            for (char ch : word.toLowerCase().toCharArray()) {
                node = node.children.computeIfAbsent(ch, c -> new TrieNode());
            }
            node.isEndOfWord = true;
        }

        public boolean containsProfanity(String text) {
            text = text.toLowerCase();

            for (int i = 0; i < text.length(); i++) {
                TrieNode node = root;
                int j = i;

                while (j < text.length()) {
                    char ch = normalizeChar(text.charAt(j));
                    if (!node.children.containsKey(ch)) break;

                    node = node.children.get(ch);
                    if (node.isEndOfWord) return true;

                    j++;
                }
            }
            return false;
        }

        private char normalizeChar(char ch) {
            return NORMALIZATION_MAP.getOrDefault(ch, ch);
        }

        // Demo usage
        public static void main(String[] args) {
            TrieTree filter = new TrieTree();
            filter.addWord("shit");
            filter.addWord("hell");
            filter.addWord("fuck");

            String comment = "This is sh!t but not hellish.";
            boolean hasProfanity = filter.containsProfanity(comment);

            System.out.println("Contains profanity? " + hasProfanity);
        }
}
