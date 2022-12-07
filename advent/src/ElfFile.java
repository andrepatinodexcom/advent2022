public class ElfFile {
    public int size;
    public String name;

    public ElfFile(int size, String name){
        this.size = size;
        this.name = name;
    }

    public void print(){
        System.out.println("- " + name + " (File, Size= " + size + ")");
    }
}
