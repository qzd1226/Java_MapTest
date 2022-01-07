import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Collections 是操作Collection 和Map的工具类
 *
 *  reverse(List):反转list中元素的顺序
 *  shuffle(List):对List集合元素进行随机排序
 *  sort(List):根据元素的自然数序对指定的List集合元素按升序排序
 *  sort(List,Comparator):根据指定的Comparator长生的顺序对List集合的元素进行排序
 *  swap(List, int,int):将制定list集合中的i处元素和j处元素进行交换
 *
 *  Object max(Collection):根据元素的自然顺序，返回给定集合中的最大元素
 *  Object max(Collection, Comparator):根据Comparator指定的排序，返回给定集合中的最大元素
 *  Object min(Collection):根据元素的自然顺序，返回给定集合中的最小元素
 *  Object min(Collection, Comparator):根据Comparator指定的排序，返回给定集合中的最小元素
 *  int frequency(Collection, Object):返回指定集合中指定元素出现的次数
 *  void copy(List dest,List src):将src中的内容复制到dest中
 *  boolean replaceAll(List, list, Object oldVal, Object newVal):使用新值替换掉旧值
 *
 *
 *
 * 面试题：Collections 和 Collection的区别？
 */
public class CollectionsTest {
    @Test
    public void test2(){
        List list = new ArrayList();
        list.add(123);
        list.add(456);
        list.add(765);
        list.add(765);
        list.add(765);
        list.add(-1);
        list.add(-97);
        list.add(0);
        //错误写法 报异常java.lang.IndexOutOfBoundsException: Source does not fit in dest
//        List dest = new ArrayList();
        List dest = Arrays.asList(new Object[list.size()]);
        Collections.copy(dest,list);
        System.out.println(dest);
        /**
         *
         * Collections 类中提供了多个synchronizedXxx()方法，
         * 该方法可以将指定集合包装成线程同步集合，从而可以解决线程兵法访问集合
         * 时的线程安全问题
         * 返回的list1即为线程安全的list
         */
        List list1 =  Collections.synchronizedList(list);
    }
    @Test
    public void test1(){
        List list = new ArrayList();
        list.add(123);
        list.add(456);
        list.add(765);
        list.add(765);
        list.add(765);
        list.add(-1);
        list.add(-97);
        list.add(0);
        System.out.println(list);
        Collections.reverse(list);
        System.out.println(list);
        Collections.shuffle(list);
        System.out.println(list);
        Collections.sort(list);
        System.out.println(list);
        Collections.swap(list,1,2);
        System.out.println(list);
        System.out.println(Collections.frequency(list, 765));
    }
}
