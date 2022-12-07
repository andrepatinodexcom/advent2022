import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args){
        readData();
    }

    public static void manageData(Scanner myReader){
        //Main Directory
        Directory root = new Directory(null, "root");
        Directory currentDirectory = root;
        int indent = 0;

        while (myReader.hasNextLine()) {
            String data = myReader.nextLine();
            if(data.contains("dir")){
                manageDir(currentDirectory, data);

            }
            if(Character.isDigit(data.toCharArray()[0])){
                manageFile(currentDirectory, data);
            }

            if(data.contains("$ cd")){
                //System.out.println("Changed Directories to: " + currentDirectory.name);
                currentDirectory = manageChangeDirectory(root, currentDirectory,data);
            }
        }
        root.calculateTotalSize();
        root.printDirectory(indent);
        System.out.println("Answer Part 1: " + part1(root,0));

        long freeSpace = 70000000 - root.totalSize;
        long neededSpace = 30000000;
        long spaceToDelete = neededSpace - freeSpace;
        System.out.println("Answer Part 2: " + part2(root, root.totalSize, spaceToDelete));
    }


    public static Directory manageChangeDirectory(Directory root, Directory currentDirectory, String input){
        String name = input.substring(input.indexOf("cd")+3);
        if(name.equals("/")) return root;
        if(name.equals("..")) return currentDirectory.parent;
        return currentDirectory.getDirectoryByName(name);
    }

    public static void manageFile(Directory currentDirectory, String input){
        int size = Integer.parseInt(input.substring(0,input.indexOf(" ")));
        String name = input.substring(input.indexOf(" ")+1);
        if(!currentDirectory.doesFileExist(name)){
            //System.out.println("Created new File ["+name+","+size+"] in directory ["+currentDirectory.name+"]");
            currentDirectory.addFile(new ElfFile(size,name));
        }
    }

    public static void manageDir(Directory currentDirectory, String input){
        String name = input.substring(input.indexOf(" ")+1);
        if(!currentDirectory.doesDirectoryExists(name)){
            //System.out.println("Created new Directory ["+name+"] in directory ["+currentDirectory.name+"]");
            currentDirectory.addDirectory(new Directory(currentDirectory,name));
        }
    }

    public static void readData(){
        try {
            File myObj = new File("/Users/ap1020/Desktop/input.txt");
            Scanner myReader = new Scanner(myObj);
            manageData(myReader);
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }


    //Returns the sum of all dir sizes below 100000 using directory as the root.
    public static int part1(Directory dir, int sum){
        if(dir.totalSize <= 100000){ sum += dir.totalSize; }
        for(Directory child: dir.children){
            sum = part1(child,sum);
        }
        return sum;
    }

    //Returns the smallest directory needed to reach 30000000 free space
    //Considering total filesystem size is 70000000
    public static long part2(Directory dir , long smallest, long spaceToDelete){
        if(dir.totalSize > spaceToDelete) { //If it is big enough to delete
            if(dir.totalSize < smallest) smallest = dir.totalSize;
            for(Directory child: dir.children){
                smallest = part2(child,smallest,spaceToDelete);
            }
        }//otherwise we dont care about it
        return smallest;
    }
}
