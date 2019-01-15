package com.demo;

import com.demo.Entity.Person;

import java.util.WeakHashMap;

public class AboutClass {

    public static void RunDemo() {
        RunWeakReferenceDemo();
    }

    //public到处可用，private是只有这个类内部可用，不写则表示package内可用（这个与C#不同）
    public static void RunWeakReferenceDemo() {
        //强引用
        var person = new Person(1, "yongfa365");

        //弱引用hashmap
        var whmap = new WeakHashMap<Person, Integer>();
        whmap.put(person, 111);
        System.out.println(whmap.size());

        //强引用实例设置为null后，WeakHashMap将在gc后自动移除弱引用项，所以size()变为0了
        //如果将上面的WeakHashMap替换为HashMap，此时即便person=null了，但其实例依然被HashMap强引用，所以gc也不会回收
        person = null;
        System.gc();
        System.out.println(whmap.size());

        //  ThreadLocal里的key就是弱引用，所以可以做到只要
    }

}
