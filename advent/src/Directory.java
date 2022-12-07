import java.util.ArrayList;

public class Directory {
    public Directory parent;
    public ArrayList<Directory> children = new ArrayList<>();
    public ArrayList<ElfFile> elfFiles = new ArrayList<>();
    public long fileSize = 0;
    public long totalSize = 0;
    String name;

    public Directory(Directory parent, String name){
        this.parent = parent;
        this.name = name;
    }

    public void addDirectory(Directory child){
        children.add(child);
    }

    public void addFile(ElfFile elfFile){
        elfFiles.add(elfFile);
    }

    public boolean doesFileExist(String name){
        for(ElfFile f: elfFiles){
            if(f.name.equals(name)) return true;
        }
        return false;
    }

    public boolean doesDirectoryExists(String name){
        for(Directory dir: children){
            if(dir.name.equals(name)) return true;
        }
        return false;
    }

    public Directory getDirectoryByName(String name){
        for(Directory dir : children){
            if(dir.name.equals(name)) return dir;
        }
        return null;
    }

    public void printDirectory(int indent){
        for(int i = 0 ; i < indent; i++) System.out.print("  ");
        System.out.println("- "+name+" (dir) Size = " + totalSize);
        indent++;
        for(Directory dir : children){
            dir.printDirectory(indent);
        }
        for(ElfFile file  : elfFiles){
            for(int i = 0 ; i < indent; i++) System.out.print("  ");
            file.print();
        }
    }

    public void calculateTotalSize(){
        calculateFileSize();
        totalSize = fileSize;
        for(Directory dir : children){
            dir.calculateTotalSize();
        }
        for(Directory dir : children){
            totalSize += dir.totalSize;
        }

    }

    public void calculateFileSize(){
        int fileSize = 0;
        for(ElfFile file  : elfFiles){
            fileSize += file.size;
        }
        this.fileSize = fileSize;

        for(Directory dir : children){
            dir.calculateFileSize();
        }
    }
}
