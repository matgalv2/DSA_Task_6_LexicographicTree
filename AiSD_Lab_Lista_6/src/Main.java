import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main
{
    public static void main(String[]args) throws FileNotFoundException
    {
        Tree tree = new Tree("Nouns", "dictionary.txt");
        Tree treeTest = new Tree(new Node("test"));

        ArrayList<String> lista = new ArrayList<>();
        lista.add("aaa");
        lista.add("aab");
        lista.add("abb");
        lista.add("de");
        lista.add("aab");
        lista.add("eggs");
        lista.add("eggat1");
        lista.add("eggat2");
        lista.add("deb");
        lista.add("eggat");
        lista.add("abba");
        treeTest.addWords(lista);


        treeTest.addWord("cel");
//        treeTest.preOrder(treeTest.getRoot());

//        tree.preOrder(tree.getRoot());

        //wyświetlenie wszystkich słów z podanym prefixem
        tree.printWithPrefix("corresponden");

        //sprawdzenie, czy dany wyraz jest w drzewie
        tree.is("correspondent");

        //wyświetlenie wielkości pliku
        tree.printSize("dictionary.txt");

        //wyświetlenie ilości słów w drzewie
        tree.numberOfWords(tree.getRoot());

        //wyświetlenie ilości node'ów w drzewie
        tree.numberOfAllNodes(tree.getRoot());

        //wyświetlenie ilości prefix'ów w drzewie
        tree.numberOfPrefixes(tree.getRoot());

        //wyświetlenie najpopularniejszego prefiksu o danej długośći
        tree.mostCommonPrefix(12);

        //wyświetlenie najpopularniejszego prefiksu o danej długośći lub większej
        tree.mostCommonPrefix2(13);
    }
}