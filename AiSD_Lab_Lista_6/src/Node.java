import java.util.ArrayList;

public class Node
{
    private String word;
    private Node parent;
    private ArrayList <Node> children;
    private boolean completeWord;

    public Node(String word)
    {
        this.word = word;
        parent = null;
        children = new ArrayList<>();
        completeWord = false;
    }

    public Node(Node parent, String word)
    {
        this.word = word;
        this.parent = parent;
        children = new ArrayList<>();
        completeWord = false;
    }

    public Node(Node parent, String word, boolean completeWord)
    {
        this.word = word;
        this.parent = parent;
        children = new ArrayList<>();
        this.completeWord = completeWord;
    }

    public Node(String word, boolean completeWord)
    {
        this.word = word;
        parent = null;
        children = new ArrayList<>();
        this.completeWord = completeWord;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public ArrayList<Node> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<Node> children) {
        this.children = children;
    }

    public boolean isCompleteWord() {
        return completeWord;
    }

    public void setCompleteWord(boolean completeWord) {
        this.completeWord = completeWord;
    }


///////////////////////////////////////////////////

    public void add(String word, boolean completeWord)
    {
        children.add(new Node(this, word, completeWord));
    }

    public void add(Node node)
    {
        children.add(node);
        node.setParent(this);
    }

    public void remove(int index)
    {
        children.remove(index);
    }

    public void remove(Node node)
    {
        children.remove(node);
    }

    public void removeChildren()
    {
        children.clear();
    }

    public boolean isLeaf()
    {
        return children.isEmpty();
    }

    public Node getMostLeftChild()
    {
        if(!children.isEmpty())
            return children.get(0);
        return null;
    }

    public Node getRightSibling()
    {
        if(parent != null)
        {
            ArrayList<Node> list = parent.getChildren();
            int position = list.indexOf(this);
            if(parent.getChildren().size()>parent.getChildren().indexOf(this)+1)
            {
                return list.get(position+1);
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "Node{" +
                "word='" + word + '\'' +
                ", parent=" + parent +
                ", completeWord=" + completeWord +
                '}';
    }

    public boolean equals(Node node)
    {
        return word.equals(node.getWord());
    }
}