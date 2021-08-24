import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Tree
{
    private Node root;
    private int amountOfWords = 0;
    private int amountOfAllNodes = 0;
    private int amountOfPrefixes = 0;
    private int counterOfWords = 0;

    public Tree(Node root)
    {
        this.root = root;
    }

    public Node getRoot()
    {
        return root;
    }

    public Tree(String rootName, String fileName) throws FileNotFoundException
    {
        root = new Node(rootName);
        addWords(read(fileName));
    }

    public void setRoot(Node root) {
        this.root = root;
    }

//    public ArrayList<String> getWords() {
//        return words;
//    }
//
//    public void setWords(ArrayList<String> words) {
//        this.words = words;
//    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<String> read(String fileName) throws FileNotFoundException
    {
        ArrayList<String> words = new ArrayList<>();
        Scanner scanner = new Scanner(new File(fileName));

        while(scanner.hasNextLine())
        {
            words.add(scanner.nextLine());
        }
        return words;
    }

    public void preOrder(Node node) {
        System.out.println(node + " ");
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            preOrder(temp);
            temp = temp.getRightSibling();
        }
    }

    public void addWords(ArrayList<String> words)
    {
        Collections.sort(words);

        for(int i = 0; i < words.size(); i++)
        {
            // służy do zbudowania Stringa z nowym prefiksem
            String prefix = "";
            // służy do chwilowego przeniesienia node'a, który ma wspólny prefiks z nowym słowem
            Node tmp = null;
            // node, którego dzieci aktualnie przeszukuję(zaczynam od root'a)
            Node checking = root;

            //sprawdzenie, czy może dane słowo jest już w drzewie
            if (returnNodeWithPrefix(words.get(i)) != null)
            {
                returnNodeWithPrefix(words.get(i)).setCompleteWord(true);
            }
            boolean notAdded = true;

            for(int j = 0; j < words.get(i).length() && notAdded; j++)
            {
                prefix += words.get(i).charAt(j);
                // obsługuje  wchodzenie do prefiksów np. mamy node'a "a", a rozpatrujemy wyraz "and"
                if(isEqualPrefix(checking.getChildren(),prefix))
                {
                    // pętla jest za krótka wybrało abb na checking, ale już nie dodało do niego nic
                    checking = equalsPrefix(checking.getChildren(),prefix);
                }
                else
                {
                    if(isWithPrefix(checking.getChildren(),prefix))
                    {
                        tmp = startWithPrefix(checking.getChildren(), prefix);
                        int commonIndex = commonLetters(checking.getChildren(), prefix, words.get(i));

                        // obsługuje skoki prefiksu o ponad 1 np. z egg -> eggat
                        if(commonIndex != words.get(i).length()-1 && prefix.length()-1 != commonIndex)
                        {

                        }
                        // obsługuje przypadki, w których istnieje słowo, które ma część wspólną z rozpatrywanym wyrazem
                        // i wtedy tworzy nowego node'a z prefiksem
                        else if(commonIndex != words.get(i).length()-1)
                        {
                            prefix = createWord(words.get(i),commonIndex);
                            // podmianka z node'ami
                            Node node = new Node(prefix);
                            Node nodeWord = new Node(words.get(i), true);
                            checking.add(node);
                            checking.remove(tmp);
                            node.add(tmp);
                            tmp.setParent(node);
                            node.add(nodeWord);
                            notAdded = false;

                        }
                        else
                        {
                            notAdded = false;
                        }
                    }
                    //dodaje nowy wyraz jeśli nie ma już gdzie pójść
                    else
                    {
                        //
                        checking.add(words.get(i), true);
                        notAdded = false;
                    }
                }
            }
        }
    }

    private Node startWithPrefix(ArrayList<Node> children, String prefix)
    {
        for(Node node : children)
        {
            if(node.getWord().startsWith(prefix))
            {
                return node;
            }
        }
        return null;
    }

    private boolean isWithPrefix(ArrayList<Node> children, String prefix)
    {
        for(Node node : children)
        {
            if(node.getWord().startsWith(prefix))
            {
                return true;
            }
        }
        return false;
    }

    private Node equalsPrefix(ArrayList<Node> children, String prefix)
    {
        for(Node node : children)
        {
            if(node.getWord().equals(prefix))
            {
                return node;
            }
        }
        return null;
    }

    private boolean isEqualPrefix(ArrayList<Node> children, String prefix)
    {
        for(Node node : children)
        {
            if(node.getWord().equals(prefix))
            {
                return true;
            }
        }
        return false;
    }

    private int commonLetters(ArrayList<Node> children, String prefix, String fullWord)
    {
        Node tmp = startWithPrefix(children, prefix);
        if(tmp != null)
        {
            int min;
            int index = -2;

            if(tmp.getWord().length() >= fullWord.length())
                min = fullWord.length();
            else
                min = tmp.getWord().length();

            for (int i = 0; i < min; i++)
            {
                if(tmp.getWord().charAt(i) == fullWord.charAt(i))
                    index = i;
            }
            return index;
        }
        return -1;
    }

//    private void isWord(Node node)
//    {
//        for(String string : words)
//        {
//            if(string.equals(node.getWord()))
//                node.setCompleteWord(true);
//        }
//    }

    private String createWord(String word, int index)
    {
        String newWord = "";
        for (int i = 0; i <= index; i++)
        {
            newWord += word.charAt(i);
        }
        return newWord;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private Node returnNodeWithPrefix(String prefix)
    {
        Node checking = root;
        Node tmp;
        String prefixOfPrefix = "";

        for (int i = 0; i < prefix.length() ; i++)
        {
            prefixOfPrefix += prefix.charAt(i);

            if(isWithPrefix(checking.getChildren(),prefixOfPrefix))
            {
                tmp = startWithPrefix(checking.getChildren(), prefixOfPrefix);

                if(isEqualPrefix(checking.getChildren(), prefix))
                {
                    return equalsPrefix(checking.getChildren(), prefix);
                }
                else if(prefixOfPrefix.length() == tmp.getWord().length())
                {
                    checking = startWithPrefix(checking.getChildren(), prefixOfPrefix);
                }
            }
            else
            {
                return null;
            }
        }
        return null;
    }

    private void printWithPrefixRec(Node prefixRoot)
    {
        if(prefixRoot.isCompleteWord())
        {
            System.out.println(prefixRoot.getWord());
        }

        Node temp = prefixRoot.getMostLeftChild();
        while (temp != null)
        {
            printWithPrefixRec(temp);
            temp = temp.getRightSibling();
        }

    }

    public void printWithPrefix(String prefix)
    {
        Node prefixRoot = returnNodeWithPrefix(prefix);
        printWithPrefixRec(prefixRoot);
    }

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////   1.4   ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

    public boolean is(String word)
    {
        Node checking = root;
        Node tmp;
        String prefix = "";

        for (int i = 0; i < word.length() ; i++)
        {
            prefix += word.charAt(i);

            if(isWithPrefix(checking.getChildren(),prefix))
            {
                tmp = startWithPrefix(checking.getChildren(), prefix);

                if(isEqualPrefix(checking.getChildren(), word))
                {
                    System.out.println("Word '" + word + "' exists in register");
                    return true;
                }
                else if(prefix.length() == tmp.getWord().length())
                {
                    checking = startWithPrefix(checking.getChildren(), prefix);
                }
            }
            else
            {
                System.out.println("Word '" + word + "' doesn't exist in register");
                return false;
            }
        }
        System.out.println("Word '" + word + "' doesn't exist in register");
        return false;
    }
////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////   2.1   ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////
    // podaje w bajtach
    public void printSize(String fileName)
    {
        File file = new File(fileName);
        System.out.println("Size of the file: " + file.length()/1024  + "kB");
    }

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////   2.2   ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

    private void numberOfWordsRec(Node node)
    {
        if(node.isCompleteWord())
        {
            amountOfWords++;
        }
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            numberOfWordsRec(temp);
            temp = temp.getRightSibling();
        }

    }

    public int numberOfWords(Node node)
    {
        numberOfWordsRec(node);
        int counter = amountOfWords;
        System.out.println("Number of words: " + amountOfWords);
        amountOfWords = 0;
        return counter;
    }

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////   2.3   ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

    private void numberOfAllNodesRec(Node node)
    {
        amountOfAllNodes++;
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            numberOfAllNodesRec(temp);
            temp = temp.getRightSibling();
        }

    }

    public int numberOfAllNodes(Node node)
    {
        numberOfAllNodesRec(node);
        int counter = amountOfAllNodes;
        System.out.println("Number of all nodes (root was included): " + amountOfAllNodes);
        amountOfAllNodes = 0;
        return counter;
    }

    private void numberOfPrefixesRec(Node node)
    {
//        if(!node.isCompleteWord())
        if(!node.isLeaf())
        {
            amountOfPrefixes++;
        }
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            numberOfPrefixesRec(temp);
            temp = temp.getRightSibling();
        }

    }

    public int numberOfPrefixes(Node node)
    {
        numberOfPrefixesRec(node);
        int counter = amountOfPrefixes;
        System.out.println("Number of prefixes (some words can be prefixes too): " + (amountOfPrefixes - 1));
        amountOfPrefixes = 0;
        return counter;
    }

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////   1.2   ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Node> mostCommonPrefix(int min) {
        ArrayList<Node> list = new ArrayList<>();
        mostCommonPrefixRec(list, root, min);
        ArrayList<Node> max = new ArrayList<>();

        if (!list.isEmpty())
        {
            Node tmp = list.get(0);

            for (Node node : list) {
                if (numberOfWordsWithPrefix(tmp) < numberOfWordsWithPrefix(node))
                {
                    tmp = node;
                }
            }

            for (Node node : list)
            {
                if (numberOfWordsWithPrefix(tmp) == numberOfWordsWithPrefix(node))
                {
                    max.add(node);
                }
            }

            for (Node node : max)
            {
                 System.out.println(node);
            }
        }

        if(max.isEmpty())
            System.out.println("There are no words long enough.");

        return max;

    }

    private void mostCommonPrefixRec(ArrayList<Node> list, Node node, int min)
    {
        if(!node.isLeaf() && node.getWord().length() == min && !node.equals(root))
        {
            list.add(node);
        }
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            mostCommonPrefixRec(list, temp, min);
            temp = temp.getRightSibling();
        }
    }

    private void numberOfWordsWithPrefixRec(Node node)
    {
        if(node.isCompleteWord())
        {
            counterOfWords++;
        }
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            numberOfWordsWithPrefixRec(temp);
            temp = temp.getRightSibling();
        }
    }

    private int numberOfWordsWithPrefix(Node node)
    {
        numberOfWordsWithPrefixRec(node);
        int counter = counterOfWords;
        counterOfWords = 0;
        return counter;
    }

////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////   1.3   ///////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////

    public ArrayList<Node> mostCommonPrefix2(int min) {
        ArrayList<Node> list = new ArrayList<>();
        mostCommonPrefixRec(list, root, min);
        ArrayList<Node> max = new ArrayList<>();

        if (!list.isEmpty())
        {
            Node tmp = list.get(0);

            for (Node node : list)
            {
                if (numberOfWordsWithPrefix(tmp) < numberOfWordsWithPrefix(node))
                {
                 tmp = node;
                }
            }

            for (Node node : list)
            {
                if (numberOfWordsWithPrefix(tmp) == numberOfWordsWithPrefix(node))
                {
                   max.add(node);
                }
            }

            for (Node node : max)
            {
                System.out.println(node);
            }
        }
        if(max.isEmpty())
            System.out.println("There are no words long enough.");
        return max;
    }

    private void mostCommonPrefixRec2(ArrayList<Node> list, Node node, int min)
    {
        if(!node.isLeaf() && node.getWord().length() >= min && !node.equals(root))
        {
            list.add(node);
        }
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            mostCommonPrefixRec2(list, temp, min);
            temp = temp.getRightSibling();
        }
    }

    private void numberOfWordsWithPrefixRec2(Node node)
    {
        if(node.isCompleteWord())
        {
            counterOfWords++;
        }
        Node temp = node.getMostLeftChild();
        while (temp != null)
        {
            numberOfWordsWithPrefixRec2(temp);
            temp = temp.getRightSibling();
        }
    }

    private int numberOfWordsWithPrefix2(Node node)
    {
        numberOfWordsWithPrefixRec2(node);
        int counter = counterOfWords;
        counterOfWords = 0;
        return counter;
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////  dodatki  ////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    public void addWord(String word)
    {
        // służy do zbudowania Stringa z nowym prefiksem
        String prefix = "";
        // służy do chwilowego przeniesienia node'a, który ma wspólny prefiks z nowym słowem
        Node tmp = null;
        // node, którego dzieci aktualnie przeszukuję(zaczynam od root'a)
        Node checking = root;

        //sprawdzenie, czy może dane słowo jest już w drzewie
        if (returnNodeWithPrefix(word) != null)
        {
            returnNodeWithPrefix(word).setCompleteWord(true);
        }
        boolean notAdded = true;

        for(int j = 0; j < word.length() && notAdded; j++)
        {
            prefix += word.charAt(j);
            if(isEqualPrefix(checking.getChildren(),prefix))
            {
                checking = equalsPrefix(checking.getChildren(),prefix);
            }
            else
            {
                if(isWithPrefix(checking.getChildren(),prefix))
                {
                    tmp = startWithPrefix(checking.getChildren(), prefix);
                    int commonIndex = commonLetters(checking.getChildren(), prefix, word);

                    if(commonIndex != word.length()-1 && prefix.length()-1 != commonIndex)
                    {

                    }
                    else if(commonIndex != word.length()-1)
                    {
                        prefix = createWord(word,commonIndex);
                        Node node = new Node(checking, prefix);
                        Node nodeWord = new Node(node, word, true);
                        checking.add(node);
                        checking.remove(tmp);
                        node.add(tmp);
                        tmp.setParent(node);


                        node.getChildren().add(whereToPlace(node,word), nodeWord);
                        notAdded = false;


                    }
                    else
                    {
                        notAdded = false;
                    }
                }
                else
                {
//                    checking.add(word, true);
                    Node node = new Node(checking,word, true);
                    checking.getChildren().add(whereToPlace(checking, word), node);
                    notAdded = false;
                }
            }
        }
    }

    private int whereToPlace(Node parent, String word)
    {
        ArrayList<String> list = new ArrayList<>();
        list.add(word);
        for(Node node : parent.getChildren())
        {
           list.add(node.getWord());
        }
        Collections.sort(list);
        return list.indexOf(word);
    }
}