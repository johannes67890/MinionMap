import parser.Tag;
import parser.TagBound;
import parser.TagNode;
import parser.TagRelation;
import parser.TagWay;
import parser.XMLReader;
import parser.XMLWriter;
import util.FileDistributer;

public class Debug {
    public static void main(String[] args) {
        System.out.println("Hello, World!");
        new XMLReader(FileDistributer.input.getFilePath());
        // for (Tag<?> n : XMLWriter.getContentFromBinaryFile()) {
        //     if(n instanceof TagRelation){
        //         System.out.println(n);
        //         break;
        //     }
        // }
        System.out.println(XMLWriter.readTagByIdFromBinaryFile(3193913l));
       

    }
}
