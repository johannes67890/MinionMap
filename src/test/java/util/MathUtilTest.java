package util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import parser.TagNode;

public class MathUtilTest {

    @Test
    public void clamp(){
        double test1 = MathUtil.clamp(5, 10, 20);
        double test2 = MathUtil.clamp(20, 5, 7);

        assertEquals(test1, 10);
        assertEquals(test2, 7);
    }

    @Test
    public void sublist(){
        ArrayList<TagNode> list = new ArrayList<>();
        list.add(new TagNode(0, 1));
        list.add(new TagNode(1, 2));
        list.add(new TagNode(2, 3));
        list.add(new TagNode(3, 4));

        list = MathUtil.sublist(0, 2, list);

        assertEquals(2, list.size());
    }

    @Test
    public void distancePointToLine(){
        TagNode node1 = new TagNode(0, 0);
        TagNode node2 = new TagNode(1, 1);
        TagNode node3 = new TagNode(1, 0);

        double test = MathUtil.distancePointToLine(node1, node2, node3);
        assertEquals(1, test);
    }

    @Test
    public void round(){
        double number = 0.1234;
        assertEquals(0.12, MathUtil.round(number, 2));
    }

}
