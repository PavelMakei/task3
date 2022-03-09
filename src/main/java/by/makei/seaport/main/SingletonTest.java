package by.makei.seaport.main;

import java.util.concurrent.atomic.AtomicReference;

public class SingletonTest {

    private static final AtomicReference<SingletonTest> INSTANCE = new AtomicReference<SingletonTest>();

    private SingletonTest(){
    }

    public static final SingletonTest getInstance(){
        while (true) {
            SingletonTest current = INSTANCE.get();
            if (current != null) {
                return current;
            }
            current = new SingletonTest();
            if (INSTANCE.compareAndSet(null, current)) {
                return current;
            }
        }
    }

    public static void main(String[] args) {
        SingletonTest singletonTest1 = SingletonTest.getInstance();
        SingletonTest singletonTest2 = SingletonTest.getInstance();
        SingletonTest singletonTest3 = SingletonTest.getInstance();
        SingletonTest singletonTest4 = SingletonTest.getInstance();
        SingletonTest singletonTest5 = SingletonTest.getInstance();
        SingletonTest singletonTest6 = SingletonTest.getInstance();
        SingletonTest singletonTest7 = SingletonTest.getInstance();
        SingletonTest singletonTest8 = SingletonTest.getInstance();
        SingletonTest singletonTest9 = SingletonTest.getInstance();



        System.out.println(singletonTest1 == singletonTest2 ||
                singletonTest1 == singletonTest3 ||
                singletonTest1 == singletonTest4 ||
                singletonTest1 == singletonTest5 ||
                singletonTest1 == singletonTest6 ||
                singletonTest1 == singletonTest7 ||
                singletonTest1 == singletonTest8 ||
                singletonTest1 == singletonTest9);
    }
}
