import com.sun.tracing.dtrace.Attributes;
import org.testng.annotations.Test;

import java.util.*;

/**
 * Map实现类的结构：
 *    |----Map:双列数据，用于存储具有key-value对的数据   ---类似于高中的函数 y = f(x)
 *          |----HashMap:作为map的主要实现：线程不安全的，效率高； 存储null的key和value
 *              |----LinkedHashMap:保证在遍历map元素时可以按照添加的顺序实现遍历。
 *                  原因：在原有的HashMap底层结构上添加了一对指针，指向前一个和后一个元素
 *                  对于频繁的遍历操作，此类执行效率高于HashMap。
 *          |----TreeMap：保证按照添加的key-value进行排序，实现排序遍历。此时考虑key的自然排序或定制排序
 *                        底层使用的是红黑树
 *          |----HashTable:作为古老的实现类：线程安全的，效率低；不能存储null的key和value
 *              |----Properties:常用来处理配置文件。key和value都是String类型
 *
 *          HashMap的底层：数组+链表    （jdk7及以前）
 *                        数组+链表+红黑树 （jdk 8）
 *
 * 面试题：
 * 1、HashMap的底层实现原理？
 * 2、HashMap和Hashtable的异同
 * 3、CurrenyHashMap与Hashtable的异同？ （暂时不讲）
 *
 *
 * Map结构的理解：
 *      Map中的key：无序的、不可重复的、使用Set存储所有的key  --->key 所在的类要重写equals()和hashcode()方法（以HashMap为例）
 *      Map中的value:无序的、可重复的，使用Collection存储所有的value --->value所在的类要重写equals()（不需要通过valuec查因此不需要重写HashCode）
 *      一个键值对：key-value构成了一个Entry对象
 *      Map中的entry:无序的、不可重复的，使用Set存储所有的entry
 *
 * HashMap底层设计原理 以jdk7为例
 *      HashMap map = new HashMap();
 *      在实例化以后，底层创建了一个长度是16的一维数组Entry[] table.
 *      ...可能已经执行过多次put...
 *      map.put(key1, value1):
 *      首先，通过key1.HashCode计算key1的哈希值，此哈希值经过某种计算以后，得到在entry[]中的存放位置。
 *      如果此位置上的数据为空，此时key1-value1添加成功。 ---->情况1
 *      如果此位置上的数据不为空，（意味此位置上存在一个或多个数据（以链表形式存在）），比较key1和已经存在的一个或多个数据的哈希值：
 *          如果key1的哈希值与已经存在的数据的哈希值都不相同，此时key1-value1能够添加成功。---->情况2
 *          如果key1的哈希值与已经存在的某一个数据的哈希值相同，继续比较：调用key1所在类的equals（）方法，比较：
 *              如果equals（）返回false;此时key1-value1添加成功 ---->情况3
 *              如果equals()返回ture：使用value1替换相同key的value2值。（相当于具有修改能力）
 *      补充：关于情况2和情况3：此时key1-value1和原来的数据以链表的方式存储。
 *
 *      在不断的添加过程中，会涉及到扩容内容，当超出临界值时（且要存放的位置非空）会对数组进行扩容（重新放到新的数组） 默认的扩容方式：扩容为原来的2倍
 *
 *      jdk8相较于jdk7底层方面的不同：
 *      1、new HashMap():底层没有创建一个长度为16的数组
 *      2、jdk 8底层的数组是:Node[],而非entry[]
 *      3、首次调用put（）方法时，底层创建长度为16的数组
 *      4、jdk7底层结构只有：数组+链表。 jdk8中底层结构： 数组+链表+红黑树
 *          当数组的摸一个索引位置上的元素一链表的形式存在的个数>8 切当前数组的长度>64时，
 *          此时索引位置上的所有数据改为红黑树存储。
 * 四、LinkedHashMap的底层实现原理（了解）
 *  源代码中：
 *      static class Entry<K,V> extends HashMap.Node<K,V> {
 *          Entry<K,V> before, after; 能够记录添加的元素的先后顺序
 *          Entry(int hash, K key, V value, Node<K,V> next) {
 *              super(hash, key, value, next);
 *         }
 *     }
 *
 * 五、Map中定义的方法
 *   添加、删除、修改操作：
 *      Object put(Object key,Object value):将指定key-value添加到（或修改）当前map对象中
 *      void putAll(Map m):将m中的所有key-value对存放到当前Map中
 *      Object remove(Object key):移除指定key的key-value对，并返回value
 *      void clear():清空当前map中所有的数据
 *   元素查询的操作：
 *      Object get(Object key):获取指定key对应的value
 *      boolean containsKey(Object key):是否包含指定的key
 *      boolean containsValue(Object value):是否包含指定的value
 *      int size():返回map中key-value对的个数
 *      boolean isEmpty():判断当前map是否为空
 *      boolean equals(Object obj):判断当前map和参数对象obj是否相等
 *      元视图操作方法：
 *      Set keySet():返回当前所有key构成的Set集合
 *      Collection values():返回所有value构成的Collection集合
 *      Set.entrySet():返回所有key-value对构成的Set集合
 *
 *  总结：常用方法：
 *      添加： Object put(Object key,Object value):将指定key-value添加到（或修改）当前map对象中
 *      删除：Object remove(Object key):移除指定key的key-value对，并返回value
 *      修改： Object put(Object key,Object value):将指定key-value添加到（或修改）当前map对象中
 *      查询：Object get(Object key):获取指定key对应的value
 *      长度：int size():返回map中key-value对的个数
 *      遍历  keySet()/values()/entrySet()
 */
public class MapTest {
    @Test
    public void test1(){
        Map map = new HashMap();
        // Hashtable()不能存储null的key和value
        //map = new Hashtable();
        map.put(null,123);

    }
// *   添加、删除、修改操作：
// *      Object put(Object key,Object value):将指定key-value添加到（或修改）当前map对象中
// *      void putAll(Map m):将m中的所有key-value对存放到当前Map中
// *      Object remove(Object key):移除指定key的key-value对，并返回value
// *      void clear():清空当前map中所有的数据
    @Test
    public void test2(){
        Map map = new HashMap();
        // Hashtable()不能存储null的key和value
        //map = new Hashtable();
        map.put("AA",123);
        map.put(44,123);
        map.put("CC",123);
        map.put("BB",123);
        //修改
        map.put("AA",27);
        System.out.println(map);
        Map map1 = new HashMap();
        map.put("CC",123);
        map.put("DD",123);
        map.putAll(map1);
        System.out.println(map);

        //remove(Object key)
        Object value = map.remove("CC");
        System.out.println(value);
        System.out.println(map);
        //clear
        map.clear();
        System.out.println(map.size());
    }
//*   元素查询的操作：
//            *      Object get(Object key):获取指定key对应的value
// *      boolean containsKey(Object key):是否包含指定的key
// *      boolean containsValue(Object value):是否包含指定的value
// *      int size():返回map中key-value对的个数
// *      boolean isEmpty():判断当前map是否为空
// *      boolean equals(Object obj):判断当前map和参数对象obj是否相等
    @Test
    public void test3() {
        Map map = new HashMap();
        // Hashtable()不能存储null的key和value
        //map = new Hashtable();
        map.put("AA", 13);
        map.put(44, 123);
        map.put("CC", 123);
        map.put("BB", 123);
        System.out.println(map.get(44));
        System.out.println(map.get("AA"));
        System.out.println(map.get(66));
        // boolean containsKey(Object key):是否包含指定的key
        System.out.println(map.containsKey(44));
        //boolean containsValue(Object value):是否包含指定的value
        System.out.println(map.containsValue(123));
        //map.size()
        System.out.println(map.size());
        //map.isEmpty()
        System.out.println(map.isEmpty());

    }
// *      元视图操作方法：
// *      Set keySet():返回当前所有key构成的Set集合
// *      Collection values():返回所有value构成的Collection集合
// *      Set.entrySet():返回所有key-value对构成的Set集合
    @Test
    public void test4() {
        Map map = new HashMap();
        // Hashtable()不能存储null的key和value
        //map = new Hashtable();
        map.put("AA", 13);
        map.put(44, 123);
        map.put("CC", 123);
        map.put("BB", 123);
        //遍历所有的key集
        Set set = map.keySet();
        Iterator iterator = set.iterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }

        //遍历所有的value
        Collection values = map.values();
        for(Object obj:values){
            System.out.println(obj);
        }

        //遍历所有的key-value
        Set entrySet = map.entrySet();
        Iterator iterator1 = entrySet.iterator();
        while(iterator1.hasNext()) {
            Object obj = iterator1.next();
            //entrySet集合中的元素都是entry
            Map.Entry entry = (Map.Entry) obj;
            System.out.println(entry.getKey() + "----->" + entry.getValue());
            }
        //方式二
        Set set2 = map.keySet();
        Iterator iterator2 = set.iterator();
        while(iterator2.hasNext()){
            System.out.println(iterator2.next() + "------>" + map.get(iterator2.next()));
        }




    }
}
