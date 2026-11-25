package threads;

public class ThreadExamples {
    
    public void exampleGetCurrentThread(){
        Thread currentThread = Thread.currentThread();
        System.out.println("Current thread [name]: " + currentThread.getName());
        System.out.println("Current thread [id]: " + currentThread.getId());
        System.out.println("Current thread [priority]: " + currentThread.getPriority());
        System.out.println("Current thread [state]: " + currentThread.getState());
        System.out.println("Current thread [isAlive]: " + currentThread.isAlive());
        System.out.println("Current thread [isDaemon]: " + currentThread.isDaemon());
        System.out.println("Current thread [threadGroup]: " + currentThread.getThreadGroup());
    }

    public void exampleGetThreadStackTrace(){
        Thread currentThread = Thread.currentThread();
        StackTraceElement[] stackTrace = currentThread.getStackTrace();
        System.out.println("Current thread stack trace:");
        for (StackTraceElement element : stackTrace) {
            System.out.println(element);
        }
    }

    public void exampleRunnableThread(){
        System.out.println("Starting Runnable thread...");
        Runnable task = () -> {
            for (int i = 0; i < 5; i++) {
                System.out.println("Runnable thread executing: " + i);
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        System.out.println("Runnable thread started.");
    }

    public void exampleThreadChildren(){
        class ChildThread extends Thread {
            @Override
            public void run() {
                System.out.println("Child thread is running.");
            }
        }
        ChildThread childThread = new ChildThread();
        childThread.start();
    }

    public void exampleThreadSleep(){
        String name = Thread.currentThread().getName();
 
        System.out.println(name + " started...");
 
        System.out.println(name + " works"); 
         
        try{
            Thread.sleep(5000);
        }
        catch(InterruptedException ex){
            System.out.println(ex.getMessage());
        }
        System.out.println(name + " finished...");
    }

    public void exampleThreadJoin(){
        class TestThread extends Thread {
            @Override
            public void run() {
                String name = Thread.currentThread().getName();
                System.out.println(name + " started...");
                System.out.println(name + " works");
                try{
                    Thread.sleep(1000);
                }
                catch(InterruptedException ex){
                    System.out.println(ex.getMessage());
                }
                System.out.println(name + " finished...");
            }
        }

        Thread[] tasks = new Thread[3];
        for (int i = 0; i < tasks.length; i++) {
            tasks[i] = new TestThread();
            tasks[i].start();
        }

        for (int i = 0; i < tasks.length; i++) {
            try {
                tasks[i].join();
            } catch (InterruptedException ex) {
                System.out.println(ex.getMessage());
            }
        }

        System.out.println("Main thread finished...");
    }

    public void exampleThreadCompletion(){
        class TestThread extends Thread {

            private volatile boolean isActive;

            public void disable(){
                isActive = false;
            }

            TestThread(){
                isActive = true;
            }

            @Override
            public void run(){
                String taskName = Thread.currentThread().getName();
                System.out.printf("%s started... \n", taskName);
        
                int counter = 1;
        
                while(isActive){
        
                    System.out.println("Loop " + counter++);
        
                    try{
                        Thread.sleep(400);
                    }
                    catch(InterruptedException ex){
                        
                        System.out.printf("%s interrupted... \n", taskName);
                    }
                }
                System.out.printf("%s finished... \n", taskName);
            }
        }

        System.out.println("Main thread started...");
        TestThread task = new TestThread();
        new Thread(task, "TestThread").start();
        try{
            Thread.sleep(2000);
            task.disable();
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            System.out.println("Main thread interrupted...");
        }
        System.out.println("Main thread finished...");
    }

    public void exampleThreadInterrupt(){
        class TestThread extends Thread {
            @Override
            public void run() {
                String taskName = Thread.currentThread().getName();
                System.out.printf("%s started... \n", taskName);
        
        
                while(!Thread.currentThread().isInterrupted()){
        
                    System.out.println("Thread working...");
        
                    try{
                        Thread.sleep(400);
                    }
                    catch(InterruptedException ex){
                        System.out.printf("%s interrupted... \n", taskName);
                        Thread.currentThread().interrupt();
                    }
                }
                System.out.printf("%s finished... \n", taskName);
            }
        }

        System.out.println("Main thread started...");
        TestThread task = new TestThread();
        Thread thread = new Thread(task, "TestThread");
        thread.start();
        try{
            Thread.sleep(2000);
            thread.interrupt();
            Thread.sleep(1000);
        }
        catch(InterruptedException ex){
            System.out.println("Main thread interrupted...");
        }
        System.out.println("Main thread finished...");
    }

    public void exampleRaceCondition(){
        class BankAccount {
            private int balance = 1000;

            public void withdraw(int amount){
                if(balance >= amount){
                    System.out.println(Thread.currentThread().getName() + " готовится снять деньги...");
                    try {
                        Thread.sleep(100); // имитируем задержку (например, обработку данных)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    balance -= amount;
                    System.out.println(Thread.currentThread().getName() + " снял деньги. Остаток: " + balance);
                } else {
                    System.out.println(Thread.currentThread().getName() + " недостаточно средств для снятия.");
                }
            }
        }

        BankAccount account = new BankAccount();
        Runnable transaction = () -> account.withdraw(1000);
        new Thread(transaction, "Муж").start();
        new Thread(transaction, "Жена").start();
    }

    public void exampleSynchronizedBlock(){
        class BankAccount {
            private int balance = 1000;

            public void withdraw(int amount){
                if(balance >= amount){
                    System.out.println(Thread.currentThread().getName() + " готовится снять деньги...");
                    try {
                        Thread.sleep(100); // имитируем задержку (например, обработку данных)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    balance -= amount;
                    System.out.println(Thread.currentThread().getName() + " снял деньги. Остаток: " + balance);
                } else {
                    System.out.println(Thread.currentThread().getName() + " недостаточно средств для снятия.");
                }
            }
        }

        BankAccount account = new BankAccount();
        Runnable transaction = () -> {
            synchronized (account) {
                account.withdraw(1000);
            }
        };
        new Thread(transaction, "Муж").start();
        new Thread(transaction, "Жена").start();
    }

    public void exampleSynchronizedMethod(){
        class BankAccount {
            private int balance = 1000;

            public synchronized void withdraw(int amount){
                if(balance >= amount){
                    System.out.println(Thread.currentThread().getName() + " готовится снять деньги...");
                    try {
                        Thread.sleep(100); // имитируем задержку (например, обработку данных)
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    balance -= amount;
                    System.out.println(Thread.currentThread().getName() + " снял деньги. Остаток: " + balance);
                } else {
                    System.out.println(Thread.currentThread().getName() + " недостаточно средств для снятия.");
                }
            }
        }

        BankAccount account = new BankAccount();
        Runnable transaction = () -> {
            account.withdraw(1000);
        };
        new Thread(transaction, "Муж").start();
        new Thread(transaction, "Жена").start();
    }

    public void exampleWaitNotify(){
        class Store {
            private int productCount = 0;
            private final int MAX_PRODUCTS = 3;

            public synchronized void produce() throws InterruptedException {
                while (productCount >= MAX_PRODUCTS) {
                    wait();
                }
                productCount++;
                System.out.println("Produced a product. Total: " + productCount);
                notify();
            }

            public synchronized void consume() throws InterruptedException {
                while (productCount <= 0) {
                    wait();
                }
                productCount--;
                System.out.println("Consumed a product. Total: " + productCount);
                notify();
            }
        }

        class Producer extends Thread {
            private Store store;

            public Producer(Store store) {
                this.store = store;
            }

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 6; i++) {
                        store.produce();
                        Thread.sleep(500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        class Consumer extends Thread {
            private Store store;

            public Consumer(Store store) {
                this.store = store;
            }

            @Override
            public void run() {
                try {
                    for (int i = 0; i < 6; i++) {
                        store.consume();
                        Thread.sleep(800);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Store store = new Store();
        Producer producer = new Producer(store);
        Consumer consumer = new Consumer(store);
        producer.start();
        consumer.start();
    }

    public static void main(String[] args) {
        ThreadExamples main = new ThreadExamples();

        //main.exampleGetCurrentThread();
        // main.exampleGetThreadStackTrace();
        //main.exampleRunnableThread();
        // main.exampleThreadChildren();
        // main.exampleThreadSleep();
        // main.exampleThreadJoin();
        // main.exampleThreadCompletion();
        // main.exampleThreadInterrupt();
        // main.exampleRaceCondition();
        // main.exampleSynchronizedBlock();
        // main.exampleSynchronizedMethod();
         main.exampleWaitNotify();
    }
}
